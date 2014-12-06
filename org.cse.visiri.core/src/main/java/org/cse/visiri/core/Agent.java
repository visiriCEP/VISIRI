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

            //Double utilizationLevel=utilizationUpdater.getUtilizationLevel();
            Utilization utilization=utilizationUpdater.update();
            double utilizationLevel=utilization.getOverallUtilizationValue();
            windowQueue.add(utilizationLevel);
            double utilizationLevelAvg=windowQueue.getAverage();
            if(utilizationLevelAvg>=Configuration.UTILIZATION_THRESHOULD){
//                transferEngines();
            }

            try {
                sleep(Configuration.AGENT_UPDATE_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transferEngines(){
            Map<String,String> transferableEngines=engineHandler.getTransferableEngines();

            //TODO should Add inter node communications
            for(String engine : transferableEngines.keySet()){
                environment.sendEngine(engine,transferableEngines.get(engine));
            }

        //Notification to dispatcher
        throw new UnsupportedOperationException();
    }

}
