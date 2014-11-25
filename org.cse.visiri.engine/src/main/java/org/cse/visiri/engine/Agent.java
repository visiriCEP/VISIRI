package org.cse.visiri.engine;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.DynamicQueryDistribution;
import org.cse.visiri.util.QueryDistribution;

/**
 * Created by Malinda Kumarasinghe on 11/21/2014.
 */
public class Agent extends Thread {

    //private UtilizationUpdater utilizationUpdater;
    private Environment environment;
    private EngineHandler engineHandler;

    public Agent(EngineHandler engineHandler){
        environment = Environment.getInstance();
        this.engineHandler=engineHandler;
        throw new UnsupportedOperationException();
    }

    public void run(){
        while(true){

            Double utilizationLevel=0.0;
            //Double utilizationLevel=utilizationUpdater.getUtilizationLevel();

            if(utilizationLevel>=Configuration.UTILIZATION_THRESHOULD){

            }

            try {
                sleep(Configuration.AGENT_UPDATE_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transferEngine(){
            DynamicQueryDistribution transferableEngines=engineHandler.getTransferableEngines();

            //Should Add internode communications
            for(String engine : transferableEngines.getEngineAllocation().keySet()){
                environment.sendEngine(engine,transferableEngines.getEngineAllocation().get(engine));
            }


    //Notification to dispatcher

        throw new UnsupportedOperationException();
    }

}
