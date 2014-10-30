package org.cse.visiri.communication.eventserver.server;

/**
 * Created by visiri on 10/28/14.
 */
public interface StreamCallback {

    void receive(String eventId, Object[] event);
}
