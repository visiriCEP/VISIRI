package org.cse.visiri.engine;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;
import org.wso2.siddhi.core.SiddhiManager;

/**
 * Created by visiri on 11/3/14.
 */
public class SiddhiCEPEngine extends CEPEngine {

    public final static int ENGINE_TYPE_SIDDHI=1;
    private SiddhiManager siddhiManager;
    private Query query;

    public SiddhiCEPEngine(Query query){
        this.siddhiManager=new SiddhiManager();
        this.query=query;

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
    public void sendEvent(Event e) {

    }
}
