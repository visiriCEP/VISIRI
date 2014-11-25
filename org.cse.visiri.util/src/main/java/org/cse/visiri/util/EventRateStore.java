package org.cse.visiri.util;

import java.util.HashMap;

/**
 * Created by Malinda Kumarasinghe on 11/12/2014.
 */
public class EventRateStore {

    private HashMap<Integer,Long> instantMap,avgMap;
    private int instantPos=-1;
    private int avgPos=-1;
    private int instantMapSize,avgMapSize;

    public EventRateStore(){

        instantMapSize=Configuration.INSTANT_EVENT_COUNT;
        avgMapSize=Configuration.AVERAGE_EVENT_COUNT;

        instantMap=new HashMap<Integer, Long>();
        avgMap=new HashMap<Integer, Long>();

        long startTime=System.currentTimeMillis();

        for(int i=0;i<instantMapSize;i++){
            instantMap.put(i,startTime);
        }

        for(int i=0;i<avgMapSize;i++){
            avgMap.put(i,startTime);
        }

    }
    public void increment(){
        Long mil=System.currentTimeMillis();

        instantPos = (++instantPos >= instantMapSize) ? 0 : instantPos;
        instantMap.put(instantPos,mil);

        avgPos = (++avgPos >= avgMapSize) ? 0 : avgPos;
        avgMap.put(avgPos,mil);
    }
    public double getInstantRate(){

        int idxLast=instantPos;
        int idxFirst=(instantPos + 1)%(instantMapSize);

        long last=instantMap.get(idxLast);
        long first=instantMap.get(idxFirst);

        double avg=((double)instantMapSize)/(double)(last-first+1);
        return avg;

    }
    public double getAverageRate(){
        int idxLast=avgPos;
        int idxFirst=(avgPos + 1)%(avgMapSize);

        long last=avgMap.get(idxLast);
        long first=avgMap.get(idxFirst);

        double avg=((double)avgMapSize)/(double)(last-first+1);
        return avg;
    }

//    private void printMap(){
//        for(int x : historyMap.keySet()){
//            System.out.print(" " + historyMap.get(x));
//        }
//        System.out.println("\n");
//    }



/*
    public static void main(String[] args){

        EventRateStore eventRateStore=new EventRateStore();

        for(int i=0;i<25;i++){
            eventRateStore.increment();
            try {
                if(i>5 && i<10)    {
                    Thread.sleep(50);
                }
                else {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(i+"\t"+eventRateStore.getAverageRate()*1000);
            System.out.println("\t\t"+i+"\t"+eventRateStore.getInstantRate()*1000);

        }

    }
*/
}
