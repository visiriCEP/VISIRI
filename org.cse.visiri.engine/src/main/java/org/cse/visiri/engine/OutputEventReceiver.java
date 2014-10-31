package org.cse.visiri.engine;

import org.cse.visiri.communication.eventserver.client.EventClient;

import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public class OutputEventReceiver {
   private Map<String,EventClient> destinationToClientMap;
    public void receiveEvent(){}
    public void stop(){};
    public void start(){};

    public Map<String, EventClient> getDestinationToClientMap() {
        return destinationToClientMap;
    }

    public void setDestinationToClientMap(Map<String, EventClient> destinationToClientMap) {
        this.destinationToClientMap = destinationToClientMap;
    }
}

