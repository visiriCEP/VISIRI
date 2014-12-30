package org.cse.visiri.communication;

import org.cse.visiri.util.Query;

/**
 * Created by Geeth on 2014-10-31.
 */
public interface EnvironmentChangedCallback {

    public void queriesChanged();
    public void nodesChanged();
    public void bufferingStart();
    public void bufferingStop();
    public void eventSubscriberChanged();
    public void startNode();
    public void stopNode();
    public void newEnginesRecieved();
}
