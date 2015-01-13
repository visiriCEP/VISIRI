package org.cse.visiri.engine;

import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.io.IOException;
import java.util.*;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public class OutputEventReceiver {
   private Map<String,EventClient> destinationToClientMap;
   private Map<String,List<EventClient>> eventToClientsMap;
    public void receiveEvent(){}
    public void stop(){};
    public void start(){};

    public OutputEventReceiver(){
        this.destinationToClientMap=new HashMap<String, EventClient>();
        this.eventToClientsMap =new HashMap<String, List<EventClient>>();
    }

    public void addDestinationIp(String nodeIp,EventClient eventClient){

        destinationToClientMap.put(nodeIp,eventClient);

        List<StreamDefinition> streamDefinitionList=eventClient.getStreamDefinitionsList();

        for(int i=0;i<streamDefinitionList.size();i++){
            String streamId=streamDefinitionList.get(i).getStreamId();
            if(eventToClientsMap.containsKey(streamId)){
                List<EventClient> eventClientList= eventToClientsMap.get(streamId);
                eventClientList.add(eventClient);
            }else{
                List<EventClient> eventClientList=new ArrayList<EventClient>();
                eventClientList.add(eventClient);
                eventToClientsMap.put(streamId, eventClientList);
            }
        }

    }

    public Map<String, EventClient> getDestinationToClientMap() {
        return destinationToClientMap;
    }

    public void setDestinationToClientMap(Map<String, EventClient> destinationToClientMap) {
        this.destinationToClientMap = destinationToClientMap;
    }

    public void removeEventsFromClient(String nodeId,Set<StreamDefinition> streamDefinitionSet){
        EventClient eventClient=destinationToClientMap.get(nodeId);
        eventClient.removeStreamDefinition(streamDefinitionSet);

    }

    public void addDynamicStreamDefinition(String nodeId,List<StreamDefinition> streamDefinitionList){

       //TODO have to remove the streamDefinition from previous EventClient if that event will not be sent to that node

        for(StreamDefinition streamDefinition:streamDefinitionList) {
            EventClient eventClient = destinationToClientMap.get(nodeId);
            eventClient.addStreamDefinition(streamDefinition);

            List<EventClient> eventClientList = eventToClientsMap.get(streamDefinition.getStreamId());
            if (!eventClientList.contains(eventClient)) {
                eventClientList.add(eventClient);
                eventToClientsMap.put(streamDefinition.getStreamId(), eventClientList);
            }
        }
    }

    public void sendEvents(Event event) throws IOException, InterruptedException {
       // System.out.println(event.getStreamId()+ " : "+event.getData()); //for test
        List<EventClient> eventClientList= eventToClientsMap.get(event.getStreamId());
        if(eventClientList!=null) {
            for (int i = 0; i < eventClientList.size(); i++) {
                EventClient eventClient = eventClientList.get(i);
                eventClient.sendEvent(event);
            }
        }else{
            System.out.println("eventClientList is null for "+event.getStreamId());
            throw new NullPointerException("eventClientList is null");
        }

    }

    public Map<String, List<EventClient>> getEventToClientsMap() {
        return eventToClientsMap;
    }
}

