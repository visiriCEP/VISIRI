package org.cse.visiri.core;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.EnvironmentChangedCallback;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.algo.util.UtilizationUpdater;

import java.util.ArrayList;
import java.util.List;

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
        engineHandler = new EngineHandler();
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
        List<Query> newQuerySet = Environment.getInstance().getNodeQueryMap().get(nodeID);
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
    public void bufferingStateChanged() {

    }

    @Override
    public void eventSubscriberChanged() {

    }

    @Override
    public void startNode() {
        System.out.println("Disp started");
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
    }

    @Override
    public void stopNode() {
        engineHandler.stop();
        utilizationUpdater.stop();
    }
}
