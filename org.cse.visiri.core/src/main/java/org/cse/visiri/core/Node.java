package org.cse.visiri.core;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.UtilizationUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-05.
 */
public class Node {

    private List<Query> queries;
    private EngineHandler engineHandler;

    private UtilizationUpdater utilizationUpdater;

    public void start() throws Exception{

        Environment.getInstance().setNodeType(Environment.NODE_TYPE_PROCESSINGNODE);

        engineHandler = new EngineHandler();
        for(Query q : queries)
        {
            engineHandler.addQuery(q);
        }

        utilizationUpdater = new UtilizationUpdater();
        utilizationUpdater.start();

        engineHandler.start();

    }

    public void stop() {
        engineHandler.stop();
        utilizationUpdater.stop();
    }

    public void subscribeToStream(String eventID, String ip_port)
    {
        Map<String,List<String>> subMap = Environment.getInstance().getSubscriberMapping();
        if(! subMap.containsKey(eventID) )
        {
            subMap.put(eventID,new ArrayList<String>());
        }
        subMap.get(eventID).add(ip_port);

    }
    public void unsubscribeFromStream(String eventID, String ip_port)
    {
        Map<String,List<String>> subMap = Environment.getInstance().getSubscriberMapping();
        subMap.get(eventID).remove(ip_port);
    }

}
