package org.cse.visiri.communication;

import org.cse.visiri.util.Configuration;

/**
 * Created by Malinda Kumarasinghe on 11/21/2014.
 */
public class Agent extends Thread {

    //private UtilizationUpdater utilizationUpdater;
    private Environment environment;

    public Agent(){
        environment.getInstance();
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

}
