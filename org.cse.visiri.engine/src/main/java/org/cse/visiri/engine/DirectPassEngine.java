package org.cse.visiri.engine;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;

import java.io.IOException;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
public class DirectPassEngine extends CEPEngine {

    private Query query;
    private OutputEventReceiver outputEventReceiver;

    public DirectPassEngine(Query query,OutputEventReceiver outputEventReceiver){
        this.query=query;
        this.outputEventReceiver=outputEventReceiver;
        this.start();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Object saveState() {
        return null;
    }

    @Override
    public void sendEvent(Event event) {
        try {
            try {
                outputEventReceiver.sendEvents(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restoreEngine() {

    }
}
