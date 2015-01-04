package org.cse.visiri.core;

import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.EnvironmentChangedCallback;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 11/5/2014.
 */
public class Dispatcher implements EnvironmentChangedCallback {
    private List<Query> queries;
    private EngineHandler engineHandler;
    private UtilizationUpdater utilizationUpdater;
    boolean started;

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
    }

    @Override
    public void nodesChanged() {

    }

    @Override
    public void bufferingStart() {
        List<String> bufList=Environment.getInstance().getBufferingEventList();
        engineHandler.eventServerBufferStart(bufList);
    }

    @Override
    public void bufferingStop() {
        engineHandler.eventServerBufferStop();
    }

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

    @Override
    public void stopNode() {
        engineHandler.stop();
        utilizationUpdater.stop();
    }

    @Override
    public void newEnginesRecieved() {

    }

    @Override
    public void dynamicCompleted() {
        //This wil executed after Dynamic adjustments are done
    }

}
