package org.cse.visiri.communication.eventserver.server;

/**
 * Created by visiri on 10/28/14.
 */
public class EventServerConfig {

    private int numberOfThreads=10;
    private int port;

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
}
