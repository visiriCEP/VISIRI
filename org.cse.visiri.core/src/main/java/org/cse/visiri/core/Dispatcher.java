package org.cse.visiri.core;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.EventServer;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.engine.OutputEventReceiver;
import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 11/5/2014.
 */
public class Dispatcher {
    private List<Query> queries;
    private EngineHandler engines;
    private EventServer eventServer;
    private OutputEventReceiver output;

    private void createStreamDefNodeMap(){
        Map<String, List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
    }

}
