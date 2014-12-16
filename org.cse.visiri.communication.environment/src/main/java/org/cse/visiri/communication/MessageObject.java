package org.cse.visiri.communication;

import org.cse.visiri.util.Query;

import java.io.Serializable;

/**
 * Created by Malinda Kumarasinghe on 11/21/2014.
 */
public class MessageObject implements Serializable {
    private int eventType;
    private Query query;
    private String destination;

    public MessageObject(int eventType,Query query,String destination){
        this.eventType=eventType;
        this.query=query;
        this.destination=destination;
    }

    public MessageObject(int eventType){
        this.eventType=eventType;

    }

    public int getEventType() {
        return eventType;
    }

    public Query getPersistedEngine() {
        return query;
    }
    public String getDestination(){
        return destination;
    }

}
