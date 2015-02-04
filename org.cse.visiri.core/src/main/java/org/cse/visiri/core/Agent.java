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

    private void checkEventRate(){
        if(Environment.getInstance().checkDynamic2()) {
            Map<String, Double> rates = Environment.getInstance().getNodeEventRates();
            if (rates.get(Environment.getInstance().getNodeId()) > Configuration.MAX_EVENT_RATE) {
               if(Environment.getInstance().checkDynamic2()) {
                  Environment.getInstance().disableDynamic2();
                  transferEngines();
               }
            }
        }
    }

    private void checkManualDynamic(){
        if(Environment.getInstance().checkDynamic()){
            Environment.getInstance().disableDynamic();
            System.out.println("Dynamic adjustments started . . . ");
            transferEngines();
        }
    }

    public void run(){
        System.out.println("Agent started . . .");
        while(true){
            if(!Environment.getInstance().checkTransferInprogress()){
//                checkManualDynamic();
                checkEventRate();
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

            TransferbleEngine TREngines=engineHandler.getTransferableEngines();
            Map<String,List<Query>> transferableEngines=TREngines.getNodeTransferbleQueryMap();
            Set<StreamDefinition> removableEngines=TREngines.getCompletlyRemovedEvents();
            Environment.getInstance().addRemovablesToDispatcher(removableEngines);
            System.out.println("\nSelected nodes : "+transferableEngines.size());

            for(String ip : transferableEngines.keySet()){
                List<Query> queries=transferableEngines.get(ip);
                System.out.println("ip - "+ip+" : "+ queries.size());

                for(Query query : queries){
                    System.out.println(query.getQueryId());
                }
            }

            Environment.getInstance().createNewTransferable(transferableEngines);
            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_ENGINE_PASS);
            System.out.println("\nENGINE_PASS Message sent");
    }

}
