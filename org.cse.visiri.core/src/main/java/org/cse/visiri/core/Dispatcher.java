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

package org.cse.visiri.core;

import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.EnvironmentChangedCallback;
import org.cse.visiri.engine.DirectPassEngine;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Dispatcher implements EnvironmentChangedCallback {
    private List<Query> queries;
    private EngineHandler engineHandler;
    private UtilizationUpdater utilizationUpdater;
    boolean started;
    private Map<String,List<Query>> changedQueries;
    private GUICallback guiCallback;

    public void initialize()
    {
        queries = new ArrayList<Query>();
        started = false;
        Environment.getInstance().setChangedCallback(this);
        Configuration.setNodeType(Environment.NODE_TYPE_DISPATCHER);
        Environment.getInstance().setNodeType(Environment.NODE_TYPE_DISPATCHER);

        utilizationUpdater = new UtilizationUpdater();
        utilizationUpdater.start();
        engineHandler = new EngineHandler("Dispatcher");
    }


    public void start() throws Exception{

        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_NODE_START);

    }

    public void stop() {
        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_NODE_STOP);
    }

    @Override
    public void queriesChanged() {

        String nodeID = Environment.getInstance().getNodeId();
        Map<String,List<Query>> nodequerymap = Environment.getInstance().getNodeQueryMap();
        List<Query> newQuerySet = nodequerymap.get(nodeID);
        if(newQuerySet == null)
        {
            newQuerySet = new ArrayList<Query>();
        }
        List<Query> addedQueries = new ArrayList<Query>(newQuerySet);
        addedQueries.removeAll(queries);

        System.out.println("Queries changed. added "+ addedQueries.size()+" queries" );
        for(Query q : addedQueries)
        {
            queries.add(q);
            engineHandler.addQuery(q);
        }
        if(guiCallback!=null) {
            guiCallback.queriesChanged();
        }
    }

    @Override
    public void nodesChanged() {

    }

    @Override
    public void bufferingStart() {
        List<String> bufList=Environment.getInstance().getBufferingEventList();
        System.out.println("buflist***"+bufList.size());
        for(String s:bufList){
            System.out.println(s);
        }
        System.out.println("***");
        engineHandler.eventServerBufferStart(bufList);
        guiCallback.bufferingStart();
    }

//    @Override
//    public void bufferingStop() {
//        engineHandler.eventServerBufferStop();
//    }

//    @Override
//    public void bufferingStateChanged() {
//        List<String> bufList=Environment.getInstance().getBufferingEventList();
//        engineHandler.eventServerBufferChanged(bufList);
//    }

    @Override
    public void eventSubscriberChanged() {

    }

    @Override
    public void startNode()  {

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(!started)
        {
            try {
                engineHandler.start();
                started = true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Disp started");
    }

    private void removeStreamDefinitions(String senderId){

        Set<StreamDefinition> completelyREmoveEventsSet=Environment.getInstance().getRemovablesToDispatcher();
//        engineHandler.dynamicRemoveEvents(senderId,completelyREmoveEventsSet);
    }


    @Override
    public void stopNode() {
        engineHandler.stop();
        utilizationUpdater.stop();
    }

    @Override
    public void newEnginesRecieved(String senderId) {
        removeStreamDefinitions(senderId);
        changedQueries=Environment.getInstance().getChangedQueries();
    }

    @Override
    public void dynamicCompleted() {
        if(changedQueries!=null) {
            //This should executed after Dynamic adjustments are done

            Set<String> nodeSet=changedQueries.keySet();

            for(String nodeId:nodeSet){
                List<Query> queryList=changedQueries.get(nodeId);
                for(Query query:queryList){
                    engineHandler.dynamicUpdateOutputEventReceiver(nodeId,query);
                }
            }


        }else{
            System.err.println("Changed queries are null");
        }
        //finally clear the environment
        engineHandler.eventServerBufferStop();
        Environment.getInstance().clearChangedQueries();
    }

    public List<Query> getQueries() {
        return queries;
    }

    public UtilizationUpdater getUtilizationUpdater() {
        return utilizationUpdater;
    }

    public EngineHandler getEngineHandler() {
        return engineHandler;
    }

    public void setGuiCallback(GUICallback guiCallback) {
        this.guiCallback=guiCallback;
    }
}
