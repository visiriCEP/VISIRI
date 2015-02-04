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
    private double memCount;
    private int count;

    public Agent(EngineHandler engineHandler,UtilizationUpdater utilizationUpdater){
        environment = Environment.getInstance();
        this.engineHandler=engineHandler;
        //this.utilizationUpdater=new UtilizationUpdater();
        this.utilizationUpdater=utilizationUpdater;
        this.utilizationUpdater.start();
        utilizationLevelQueue=new ArrayDeque<Double>();
        windowQueue=new WindowQueue(6);
        this.memCount=0;
        this.count=0;
    }

    public void run(){
        System.out.println("Agent started . . .");
        while(true){


            if(!Environment.getInstance().checkTransferInprogress()){

                 checkUtilization();

            }
            try {
                sleep(Configuration.AGENT_UPDATE_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUtilization(){
        Utilization utilization=utilizationUpdater.update();

        double utilizationLevel=utilization.getOverallUtilizationValue();
        memCount+=utilizationLevel;
        count++;
        windowQueue.add(utilizationLevel);
        double utilizationLevelAvg=windowQueue.getAverage();


        if(utilizationLevelAvg>=Configuration.UTILIZATION_THRESHOULD){
                       transferEngines();
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

//                    for(StreamDefinition streamDefinition : query.getInputStreamDefinitionsList()){
//                        environment.getInstance().getBufferingEventList().add(streamDefinition.getStreamId());
//                        System.out.println("Buffering list "+streamDefinition.getStreamId());
//                    }

                }

            }

            Environment.getInstance().createNewTransferable(transferableEngines);
            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_ENGINE_PASS);
            System.out.println("\nENGINE_PASS Message sent");
    }

    public double getOverallAverageMemory(){
        return memCount/count;
    }

}
