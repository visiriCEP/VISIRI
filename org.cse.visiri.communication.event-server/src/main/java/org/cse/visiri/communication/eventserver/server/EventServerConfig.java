package org.cse.visiri.communication.eventserver.server;

/**
 * Created by visiri on 10/28/14.
 */
public class EventServerConfig {

    private int numberOfThreads=25;
    private int port;
    private int eventRateStoreFrequency = 10000;

    public EventServerConfig(int port) {
        this.port = port;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getPort() {
        return port;
    }

    public int getEventRateStoreFrequency() {
        return eventRateStoreFrequency;
    }

    public void setEventRateStoreFrequency(int eventRateStoreFrequency) {
        this.eventRateStoreFrequency = eventRateStoreFrequency;
    }
}
