package org.cse.visiri.core;

import org.cse.visiri.algo.*;
import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.EnvironmentChangedCallback;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-05.
 */

//TODO how to receive Siddhi persist and restore
public class Node implements EnvironmentChangedCallback{

    private List<Query> queries;
    private EngineHandler engineHandler;
    public int recievedEvent=0;     // For testing only
    private UtilizationUpdater utilizationUpdater;
    private boolean started ;
    private Agent agent;


    public  void initialize()
    {
        queries = new ArrayList<Query>();
        started = false;
        Environment.getInstance().setChangedCallback(this);
        Configuration.setNodeType(Environment.NODE_TYPE_PROCESSINGNODE);
        Environment.getInstance().setNodeType(Environment.NODE_TYPE_PROCESSINGNODE);

        utilizationUpdater = new UtilizationUpdater();
        utilizationUpdater.start();
        engineHandler = new EngineHandler("Node");
        agent=new Agent(engineHandler);
    }

    public void start() throws Exception{
        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_NODE_START);
        agent.start();
    }

    public void stop() {
        engineHandler.stop();
        utilizationUpdater.stop();
        agent.stop();
    }

    public void addQueries(List<Query> queries)
    {
        QueryDistributionAlgo algo = AlgoFactory.createAlgorithm(QueryDistributionAlgo.SCTXPF_PLUS_ALGO);
        QueryDistribution dist = algo.getQueryDistribution(queries);
        Environment.getInstance().addQueryDistribution(dist);

        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_QUERIES_CHANGED);
    }

    public void subscribeToStream(String eventID, String ip_port)
    {
        Map<String,List<String>> subMap = Environment.getInstance().getSubscriberMapping();
        List<String> subs= new ArrayList<String>();
        if( subMap.containsKey(eventID) )
        {
            subs = subMap.get(eventID);
        }
        subs.add(ip_port);
        subMap.put(eventID, subs);

    }
    public void unsubscribeFromStream(String eventID, String ip_port)
    {
        Map<String,List<String>> subMap = Environment.getInstance().getSubscriberMapping();
        subMap.get(eventID).remove(ip_port);
    }

    @Override
    public void queriesChanged() {
        recievedEvent=1;
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
        recievedEvent=2;
    }

    @Override
    public void bufferingStateChanged() {
        recievedEvent=3;
    }

    @Override
    public void eventSubscriberChanged() {
        recievedEvent=4;
    }

    @Override
    public void startNode() {
        recievedEvent=5;
        System.out.println("Node started");

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
    public void stopNode()
    {
        recievedEvent=6;
        engineHandler.stop();
        utilizationUpdater.stop();
    }

    @Override
    public void newEngineRecieved(String persistedEngine) {
        try {
            engineHandler.dynamicAddQuery(persistedEngine);
        } catch (Exception e) {
            System.err.println("Failed to add new Engine");
        }
    }

}
