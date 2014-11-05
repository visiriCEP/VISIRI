package org.cse.visiri.core;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-05.
 */
public abstract class Node {

    public static final int NODE_TYPE_PROCESSINGNODE = 1;
    public static final int NODE_TYPE_DISPATCHER = 2;


    protected List<Query> queries;
    protected EngineHandler engineHandler;

    public abstract void start();
    public abstract void stop();

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
