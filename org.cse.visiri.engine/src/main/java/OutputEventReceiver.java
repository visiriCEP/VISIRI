/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
public class OutputEventReceiver {
    private Map<Stirng,EventClient> destinationToClientMap;
    public void receiveEvent(){}
    public void stop(){};
    public void start(){};

    public Map<stirng, EventClient> getDestinationToClientMap() {
        return destinationToClientMap;
    }

    public void setDestinationToClientMap(Map<stirng, EventClient> destinationToClientMap) {
        this.destinationToClientMap = destinationToClientMap;
    }
}
