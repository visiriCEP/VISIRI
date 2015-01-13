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
    private String from;

    public MessageObject(int eventType,Query query,String from,String destination){
        this.from=from;
        this.eventType=eventType;
        this.query=query;
        this.destination=destination;
    }

    public MessageObject(int eventType,String from){
        this.eventType=eventType;
        this.from=from;

    }

    public int getEventType() {
        return eventType;
    }
    public String getFrom(){
        return this.from;
    }

    public Query getPersistedEngine() {
        return query;
    }
    public String getDestination(){
        return destination;
    }

}
