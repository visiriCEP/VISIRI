package org.cse.visiri.engine;

import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.*;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
public class EngineHandler {
    private Map<String,CEPEngine> queryEngineMap;
    private Map<String,List<CEPEngine>> eventEngineMap;
    private OutputEventReceiver outputEventReceiver;
    private Map<String,StreamDefinition> streamDefinitionMap;
    private EventServerConfig eventServerConfig;

    public EngineHandler(){

        this.queryEngineMap=new HashMap<String, CEPEngine>();
        this.eventEngineMap=new HashMap<String, List<CEPEngine>>();
        this.outputEventReceiver=new OutputEventReceiver();
        this.streamDefinitionMap=new HashMap<String, StreamDefinition>();
        this.eventServerConfig=new EventServerConfig(7211);
    }

    public void start() throws Exception {

        List<StreamDefinition> streamDefinitionList=new ArrayList<StreamDefinition>();
        streamDefinitionMap.keySet().toArray();

        ///streamDefinitionMap
        for(int i=0;i<streamDefinitionMap.size();i++){
           // streamDefinitionList.add()
        }
        EventServer eventServer=new EventServer(eventServerConfig,streamDefinitionList,new StreamCallback() {
            @Override
            public void receive(Event event) {
                List<CEPEngine> cepEngineList=eventEngineMap.get(event.getStreamId());
                for(int i=0;i<cepEngineList.size();i++){
                    cepEngineList.get(i).sendEvent(event);
                }
            }
        });

        eventServer.start();


    }

    public void stop(){

    }

    public void sendEvents(){

    }

    public void addQuery(Query query){

       CEPEngine cepEngine=CEPFactory.createEngine(query.getEngineId(), query);
       queryEngineMap.put(query.getQueryId(),cepEngine);

       List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
       for(int i=0;i<inputStreamDefinitionList.size();i++){
           StreamDefinition streamDefinition=inputStreamDefinitionList.get(i);
           setEnginesToEvents(streamDefinition.getStreamId(),cepEngine);
           streamDefinitionMap.put(streamDefinition.getStreamId(), streamDefinition);
       }
    }
    public void removeQuery(String queryID){


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
}
