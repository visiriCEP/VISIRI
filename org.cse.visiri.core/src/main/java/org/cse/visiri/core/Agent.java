package org.cse.visiri.core;

import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.*;

import java.util.*;

/**
 * Created by Malinda Kumarasinghe on 11/21/2014.
 */
public class Agent extends Thread {

    private UtilizationUpdater utilizationUpdater;
    private Environment environment;
    private EngineHandler engineHandler;
    private Queue<Double> utilizationLevelQueue;
    private WindowQueue windowQueue;

    public Agent(EngineHandler engineHandler){
        environment = Environment.getInstance();
        this.engineHandler=engineHandler;
        this.utilizationUpdater=new UtilizationUpdater();
        this.utilizationUpdater.start();
        utilizationLevelQueue=new ArrayDeque<Double>();
        windowQueue=new WindowQueue(6);
    }

    public void run(){
        while(true){


            if(!Environment.getInstance().checkTransferInprogress()){

            }

            //Double utilizationLevel=utilizationUpdater.getUtilizationLevel();
//            Utilization utilization=utilizationUpdater.update();
//            double utilizationLevel=utilization.getOverallUtilizationValue();
//            windowQueue.add(utilizationLevel);
//            double utilizationLevelAvg=windowQueue.getAverage();
//            if(utilizationLevelAvg>=Configuration.UTILIZATION_THRESHOULD){
////                transferEngines();
//            }
//
//            try {
//                sleep(Configuration.AGENT_UPDATE_PERIOD);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void transferEngines(){
            Map<String,List<Query>> transferableEngines=engineHandler.getTransferableEngines();
            Environment.getInstance().createNewTransferable(transferableEngines);

            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_ENGINE_PASS);
//            //TODO should Add inter node communications
//            for(Query query : transferableEngines.keySet()){
//                environment.sendEngine(query,transferableEngines.get(query));
//            }
//
//        //Notification to dispatcher
 //       throw new UnsupportedOperationException();
    }

}
