package org.cse.visiri.communication;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 11/3/2014.
 */
public class EventClient {
    List<StreamDefinition> streamTypes;
    Map<String,StreamDefinition> streamIdMap;

    public EventClient(String ip,int port){}
    public void start(){}
    public void stop(){}
    public void sendEvent(Event ev){}

}
