/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.engine;

import org.cse.visiri.algo.DynamicQueryDistributionAlgoImpliment;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.*;

import java.util.*;
import java.util.concurrent.*;


public class EngineHandler {
    private Map<String,CEPEngine> queryEngineMap;
    private Map<String,List<CEPEngine>> eventEngineMap;
    private OutputEventReceiver outputEventReceiver;
    private Map<String,StreamDefinition> streamDefinitionMap;
    private EventServerConfig eventServerConfig;
    private List<Query> myQueryList;
    private EventServer eventServer;
    private TransferbleQuery transferbleQuery;
    private String identifier;

    private BlockingQueue blockingQueue;
    private final int queueCapacity = 100*1000;

    private EventRateStore eventRateStore;


    public EngineHandler(String identifier){
        this.identifier=identifier;
        this.queryEngineMap=new HashMap<String, CEPEngine>();
        this.eventEngineMap=new HashMap<String, List<CEPEngine>>();
        this.outputEventReceiver=new OutputEventReceiver();
        this.streamDefinitionMap=new HashMap<String, StreamDefinition>();
        this.eventServerConfig=new EventServerConfig(7211);
        this.myQueryList=new ArrayList<Query>();
        this.transferbleQuery=new TransferbleQuery();
//        this.blockingQueue=new ArrayBlockingQueue(queueCapacity);
        this.blockingQueue=new LinkedBlockingDeque();

        eventRateStore=new EventRateStore();
    }

    public void start() throws Exception {

        try {
            this.configureOutputEventReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StreamDefinition> streamDefinitionList=new ArrayList<StreamDefinition>();
        Set<String> streamIdSet=streamDefinitionMap.keySet();

        for(String streamId: streamIdSet){
            streamDefinitionList.add(streamDefinitionMap.get(streamId));
        }

    // Event server creating
        eventServer=new EventServer(eventServerConfig,streamDefinitionList,new StreamCallback() {

           @Override
            public void receive(final Event event) {
                eventRateStore.increment();
               try {
                   blockingQueue.put(event);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
        },identifier);
        System.out.println("ES trying to start. . .");

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                try {
                    eventServer.start();
                } catch (Exception e) {
                    System.err.println("Error : Unable to start EventServer");
                }

            }
        });
        t.start();
        this.sendFromBlockingQueueStarts();

    }

    //private method for releasing the blocking queue and send events
    private void sendFromBlockingQueueStarts(){

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Event event = new Event();
                    try {
                        event = (Event) blockingQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    List<CEPEngine> cepEngineList = eventEngineMap.get(event.getStreamId());
                    for (int i = 0; i < cepEngineList.size(); i++) {
                        cepEngineList.get(i).sendEvent(event);
                    }

                }
            }
        });
        t.start();
    }


    public TransferbleEngine getTransferableEngines(){
        //1. get transferable queries from TransferableQueries class
        //2. run Dynamic distribution algorithm to get query distribution algorithm
        //3. call "removeEngine" method for all transferable engines
        //4. return new dynamic query distribution

        String myNode= Environment.getInstance().getNodeId();
        Map<String,List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();

        List<Query> queryList=nodeQueryMap.get(myNode);
        double[] eventRates=new double[queryList.size()];
        int i=0;
        for(Query query:queryList){
            SiddhiCEPEngine cepEngine=(SiddhiCEPEngine)queryEngineMap.get(query.getQueryId());
            eventRates[i]=cepEngine.getAvgEventRate();
            i++;
        }

        List<Query> transferbleQueryList=transferbleQuery.detectTransferbleQuery(eventRates,queryList);

        DynamicQueryDistributionAlgoImpliment dynamicQueryDistribution=new DynamicQueryDistributionAlgoImpliment();

        Map<String,List<Query>> nodeTransferbleQueryMap=dynamicQueryDistribution.getQueryDistribution(transferbleQueryList);
        Set<StreamDefinition> completelyRemovedEvents=new HashSet<StreamDefinition>();
        for(Query query:transferbleQueryList){
            SiddhiCEPEngine siddhiCEPEngine=(SiddhiCEPEngine)queryEngineMap.get(query.getQueryId());

            //Add siddhiEngine to buffering list and remove engine from engineHandler
            addToBufferingList(siddhiCEPEngine);

            //persist the siddhi engine
            //TODO use revision key


           // String revisionKey=(String)siddhiCEPEngine.saveState();
            siddhiCEPEngine.stop();

            //removing query from all the lists and maps in EngineHandler
            queryList.remove(query);
            nodeQueryMap.put(myNode,queryList);
            this.removeQueries(query,siddhiCEPEngine);

            //in order to get to know the event types which are completely removed from this node
            for(StreamDefinition streamDefinition:query.getInputStreamDefinitionsList()){
                List<CEPEngine> cepEngineList=eventEngineMap.get(streamDefinition.getStreamId());
                if(cepEngineList.isEmpty()){
                    completelyRemovedEvents.add(streamDefinition);
                }
            }

        }


        //Sending buffering message to dispatcher
        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_BUFFERING_START);
        TransferbleEngine transferbleEngine=new TransferbleEngine(nodeTransferbleQueryMap,completelyRemovedEvents);

        return transferbleEngine;

    }

    //siddhi engine is added to buffereing list
    private void addToBufferingList(SiddhiCEPEngine siddhiCEPEngine){

        List<String> bufferingList=Environment.getInstance().getBufferingEventList();
        List<StreamDefinition> strmDefs=siddhiCEPEngine.getInputStreamDefinitionList();

        for(StreamDefinition strdrf : strmDefs){
            bufferingList.add(strdrf.getStreamId());
           // Environment.getInstance().getBufferingEventList().add(strdrf.getStreamId());
        }

        System.out.println("Transferable queries to Buffering list");
    }

    public void stop(){
    }

    public void sendEvents(){

    }

    //method to add query to the Engine handler
    public void addQuery(Query query){

       CEPEngine cepEngine=CEPFactory.createEngine(query.getEngineId(), query,outputEventReceiver);
       queryEngineMap.put(query.getQueryId(),cepEngine);

    //   System.out.print("Adding new query :");

       List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
       for(int i=0;i<inputStreamDefinitionList.size();i++){
           StreamDefinition streamDefinition=inputStreamDefinitionList.get(i);
           setEnginesToEvents(streamDefinition.getStreamId(),cepEngine);
           streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
   //        System.out.print(" In:"+inputStreamDefinitionList.get(i).getStreamId());
       }

   //     System.out.println(" Out:"+query.getOutputStreamDefinition().getStreamId());
        this.myQueryList.add(query);
    }

    public void eventServerBufferStart(List<String> bufferingEventList){
        eventServer.bufferingStart(bufferingEventList);
    }
    public void eventServerBufferStop(){
        Environment.getInstance().getBufferingEventList().clear();
        eventServer.bufferingStop();
    }

    public void addQueryList(List<Query> queryList){
        for(Query query: queryList){
            addQuery(query);

//            CEPEngine cepEngine=CEPFactory.createEngine(query.getEngineId(), query,outputEventReceiver);
//            queryEngineMap.put(query.getQueryId(),cepEngine);
//
//            List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
//            for(int i=0;i<inputStreamDefinitionList.size();i++){
//                StreamDefinition streamDefinition=inputStreamDefinitionList.get(i);
//                setEnginesToEvents(streamDefinition.getStreamId(),cepEngine);
//                streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
//            }
//
//            this.myQueryList.add(query);
        }
    }

    public void addNewQueries(List<Query> queries){
        int n=0;
        for(Query query : queries){
            try {
                this.dynamicAddQuery(query,++n);
            } catch (Exception e) {
                System.err.println("Dynamic : Failed to add new query" );
                e.printStackTrace();
            }
        }
        Environment.getInstance().clearNewAddedQueries();

        if(!Environment.getInstance().checkTransferInprogress()){
            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_DISPATCHER_NOTIFICATION);
            System.out.println("Dynamic completed message sent");
        }
        System.out.println("\nQuery adding completed");
    }

    private void dynamicAddQuery(Query query,int n) throws Exception {

        CEPEngine cepEngine=CEPFactory.createEngine(query.getEngineId(), query,outputEventReceiver);
        System.out.println("query "+n+"is about to restore");
      //  cepEngine.restoreEngine();
        queryEngineMap.put(query.getQueryId(),cepEngine);


        List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
        for(int i=0;i<inputStreamDefinitionList.size();i++){
            StreamDefinition streamDefinition=inputStreamDefinitionList.get(i);
            setEnginesToEvents(streamDefinition.getStreamId(),cepEngine);
            streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
            eventServer.addStreamDefinition(streamDefinition);
        }
        this.myQueryList.add(query);

        Map<String,List<String>> destinationNodeMap;

        //Have to check about whether dispatcher or processing node
        if(Environment.getInstance().getNodeType()==Environment.NODE_TYPE_DISPATCHER){
            destinationNodeMap=Environment.getInstance().getEventNodeMapping();
        }else {
            destinationNodeMap=Environment.getInstance().getSubscriberMapping();
        }

        StreamDefinition outputStreamDefiniton=query.getOutputStreamDefinition();
        String streamId=outputStreamDefiniton.getStreamId();
        List<String> nodeIpList=destinationNodeMap.get(streamId);

        Map<String,List<EventClient>> eventToClientsMap=outputEventReceiver.getEventToClientsMap();

        if(!eventToClientsMap.containsKey(streamId)){
            for(String nodeIp: nodeIpList){
                List<StreamDefinition> streamDefinitionList=new ArrayList<StreamDefinition>();
                streamDefinitionList.add(outputStreamDefiniton);
                if(!nodeIp.contains(":")){
                    nodeIp=nodeIp+":"+EventServer.DEFAULT_PORT;
                }
                EventClient eventClient=new EventClient(nodeIp,streamDefinitionList);
                outputEventReceiver.addDestinationIp(nodeIp,eventClient);
            }
        }

        System.out.print(n + " ");

    }

    public void dynamicRemoveQuery(Query query){

        CEPEngine siddhiCepEngine=(SiddhiCEPEngine)this.queryEngineMap.get(query.getQueryId());
        this.queryEngineMap.remove(query.getQueryId());

        //TODO have to implement persistent siddhi instance
    }

    private void removeQueries(Query query,CEPEngine cEPEngine){
        queryEngineMap.remove(query.getQueryId());
        System.out.println("my query list size before removedd = "+myQueryList.size());
        myQueryList.remove(query);
        System.out.println("Query "+query.getQueryId()+" is removed");
        System.out.println("my query list size after removedd = "+myQueryList.size());
        List<StreamDefinition> inputStreams=query.getInputStreamDefinitionsList();

        for(StreamDefinition streamDefinition:inputStreams){
            List<CEPEngine> cepEngineList=eventEngineMap.get(streamDefinition.getStreamId());
            cepEngineList.remove(cEPEngine);
            eventEngineMap.put(streamDefinition.getStreamId(),cepEngineList);
        }

    }

    public Map<String, CEPEngine> getQueryEngineMap() {
        return queryEngineMap;
    }


    public Map<String, List<CEPEngine>> getEventEngineMap() {
        return eventEngineMap;
    }


    public OutputEventReceiver getOutputEventReceiver() {
        return outputEventReceiver;
    }

    private void setEnginesToEvents(String streamId,CEPEngine cepEngine){

        if(eventEngineMap.containsKey(streamId)){
            eventEngineMap.get(streamId).add(cepEngine);
        }else{
            List<CEPEngine> cepEngineList=new ArrayList<CEPEngine>();
            cepEngineList.add(cepEngine);
            eventEngineMap.put(streamId,cepEngineList);
        }
    }

    public void dynamicRemoveEvents(String nodeId,Set<StreamDefinition> streamDefinitionSet){
        this.outputEventReceiver.removeEventsFromClient(nodeId,streamDefinitionSet);
    }

    public void dynamicUpdateOutputEventReceiver(String nodeId,Query query){
            this.outputEventReceiver.addDynamicStreamDefinition(nodeId,query.getInputStreamDefinitionsList());
    }

    private void configureOutputEventReceiver() throws Exception {
        //String myIp= Environment.getInstance().getNodeId();
        //List<Query> myQueryList=Environment.getInstance().getNodeQueryMap().get(myIp);
        List<StreamDefinition> ouputStreamDefinitionList=new ArrayList<StreamDefinition>();
        Map<String,List<StreamDefinition>> nodeStreamDefinitionListMap=new HashMap<String, List<StreamDefinition>>();

        for(Query q : myQueryList){
            ouputStreamDefinitionList.add(q.getOutputStreamDefinition());
        }

        Map<String,List<String>> destinationNodeMap;

        //Have to check about whether dispatcher or processing node
        if(Environment.getInstance().getNodeType()==Environment.NODE_TYPE_DISPATCHER){
            destinationNodeMap=Environment.getInstance().getEventNodeMapping();
        }else {
            destinationNodeMap=Environment.getInstance().getSubscriberMapping();
        }


        for(StreamDefinition streamDefinition : ouputStreamDefinitionList){
            String streamId = streamDefinition.getStreamId();
            List<String> nodeIpList=destinationNodeMap.get(streamId);
            if(nodeIpList != null) {

                for (String nodeIp : nodeIpList) {
                    if (nodeStreamDefinitionListMap.containsKey(nodeIp)) {
                        List<StreamDefinition> streamDefinitionList = nodeStreamDefinitionListMap.get(nodeIp);
                        streamDefinitionList.add(streamDefinition);
                    } else {
                        List<StreamDefinition> streamDefinitionList = new ArrayList<StreamDefinition>();
                        streamDefinitionList.add(streamDefinition);
                        nodeStreamDefinitionListMap.put(nodeIp, streamDefinitionList);
                    }
                }
            }
        }

        Set<String> nodeIpSet=nodeStreamDefinitionListMap.keySet();
        for (String nodeIp :  nodeIpSet){
            String fullIp = nodeIp;
            if(!nodeIp.contains(":")){
                fullIp=nodeIp+":"+EventServer.DEFAULT_PORT;
            }
            EventClient eventClient=new EventClient(fullIp,nodeStreamDefinitionListMap.get(nodeIp));
            outputEventReceiver.addDestinationIp(nodeIp,eventClient);
        }
    }

    public EventRateStore getEventRateStore() {
        return eventRateStore;
    }

    public List<Query> getMyQueryList() {
        return myQueryList;
    }
}
