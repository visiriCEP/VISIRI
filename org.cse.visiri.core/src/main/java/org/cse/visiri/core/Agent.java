package org.cse.visiri.core;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println("Agent started");
        while(true){


            if(!Environment.getInstance().checkTransferInprogress()){


                if(Environment.getInstance().checkDynamic()){
                       transferEngines();
                       break;
                }
                /*
                    Utilization utilization=utilizationUpdater.update();

                    double utilizationLevel=utilization.getOverallUtilizationValue();
                    windowQueue.add(utilizationLevel);
                    double utilizationLevelAvg=windowQueue.getAverage();

                    if(utilizationLevelAvg>=Configuration.UTILIZATION_THRESHOULD){
                       transferEngines();
                    }
*/
            }
            try {
                sleep(Configuration.AGENT_UPDATE_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transferEngines(){
            System.out.println("----Started Transferring Engines");
            Map<String,List<Query>> transferableEngines=engineHandler.getTransferableEngines();
            Environment.getInstance().createNewTransferable(transferableEngines);

            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_ENGINE_PASS);
            System.out.println("----EVENT_TYPE_ENGINE_PASS Message sent");
    }

}
