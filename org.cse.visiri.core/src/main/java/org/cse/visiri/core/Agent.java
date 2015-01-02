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
        System.out.println("Agent started . . .");
        while(true){


            if(!Environment.getInstance().checkTransferInprogress()){


                if(Environment.getInstance().checkDynamic()){
                        System.out.println("Dynamic adjustments started . . . ");
                        transferEngines();
                        Environment.getInstance().disableDynamic();
                      // break;
                }else{
                   // System.out.println("check . . .");
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

            Map<String,List<Query>> transferableEngines=engineHandler.getTransferableEngines();
            System.out.println("\nSelected nodes : "+transferableEngines.size());

            for(String ip : transferableEngines.keySet()){
                List<Query> queries=transferableEngines.get(ip);
                System.out.println("ip - "+ip+" : "+ queries.size());
            }

            Environment.getInstance().createNewTransferable(transferableEngines);

            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_ENGINE_PASS);
            System.out.println("\nENGINE_PASS Message sent");
    }

}
