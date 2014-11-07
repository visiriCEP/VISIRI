package org.cse.visiri.core;

import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.UtilizationUpdater;

import java.util.List;

/**
 * Created by Malinda Kumarasinghe on 11/5/2014.
 */
public class Dispatcher {
    private List<Query> queries;
    private EngineHandler engineHandler;
    private UtilizationUpdater utilizationUpdater;

    private void createStreamDefNodeMap(){
     //   Map<String, List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
    }

    public void start() throws Exception{

        engineHandler = new EngineHandler();
        for(Query q : queries)
        {
            engineHandler.addQuery(q);
        }

        utilizationUpdater = new UtilizationUpdater();
        utilizationUpdater.start();

        engineHandler.start();

    }

    public void stop() {
        engineHandler.stop();
        utilizationUpdater.stop();
    }
}
