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
        this.utilizationUpdater=utilizationUpdater;
        this.utilizationUpdater.start();
        utilizationLevelQueue=new ArrayDeque<Double>();
        windowQueue=new WindowQueue(6);
        this.memCount=0;
        this.count=0;
    }

    private void updateEventRate(){
        Double x=engineHandler.getEventRateStore().getAverageRate();
        System.out.println("Rate : "+ x);
       Environment.getInstance().setNodeEventRate(x);

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

    private void checkEventComparison(){

        if(Environment.getInstance().checkDynamic2()){
            Map<String, Double> ratesMap = Environment.getInstance().getNodeEventRates();
            double sum=0;

            List<Double> eventRateArray= (List<Double>) ratesMap.values();

            for(Double value:eventRateArray){
                sum+=value;
            }

            double averageValue=sum/eventRateArray.size();
            double rateGap=ratesMap.get(Environment.getInstance().getNodeId())-averageValue;

            System.out.println("My Rate="+ratesMap.get(Environment.getInstance().getNodeId()));
            System.out.println("Avg Rate="+averageValue);
            System.out.println("Average rate gap = "+rateGap);

            if(rateGap>Configuration.MAX_EVENT_RATE_GAP){
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

            updateEventRate();
            if(!Environment.getInstance().checkTransferInprogress()){

                 //checkUtilization();
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
//                       transferEngines();
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
