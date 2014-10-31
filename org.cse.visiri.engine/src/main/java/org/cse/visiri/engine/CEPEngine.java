package org.cse.visiri.engine;

import org.cse.visiri.util.Event;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public abstract class CEPEngine {

    public final static int ENGINE_TYPE_DIRECT=0;
    public final static int ENGINE_TYPE_SIDDHI=1;

    public abstract void start();
    public abstract void stop();
    public abstract Object saveState();
    public abstract void sendEvent(Event e);
}
