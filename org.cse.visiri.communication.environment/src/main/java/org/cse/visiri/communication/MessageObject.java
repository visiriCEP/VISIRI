package org.cse.visiri.communication;

/**
 * Created by Malinda Kumarasinghe on 11/21/2014.
 */
public class MessageObject {
    private int eventType;
    private String persistedEngine;
    private String destination;

    public MessageObject(int eventType,String persistedEngine,String destination){
        this.eventType=eventType;
        this.persistedEngine=persistedEngine;
        this.destination=destination;
    }

    public MessageObject(int eventType){
        this.eventType=eventType;

    }

    public int getEventType() {
        return eventType;
    }

    public String getPersistedEngine() {
        return persistedEngine;
    }
    public String getDestination(){
        return destination;
    }

}
