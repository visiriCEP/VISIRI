package org.cse.visiri.communication.eventserver.server;

import org.cse.visiri.util.Event;

/**
 * Created by visiri on 10/28/14.
 */
public interface StreamCallback {

    void receive(Event event);
}
