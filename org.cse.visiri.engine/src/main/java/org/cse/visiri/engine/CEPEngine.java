package org.cse.visiri.engine;

import org.cse.visiri.util.Event;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public abstract class CEPEngine {

    public abstract void start();
    public abstract void stop();
    public abstract Object saveState();
    public abstract void sendEvent(Event e);
    public abstract void restoreEngine();
}
