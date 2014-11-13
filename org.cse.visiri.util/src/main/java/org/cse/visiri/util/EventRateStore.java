package org.cse.visiri.util;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Malinda Kumarasinghe on 11/12/2014.
 */
public class EventRateStore {

    private long instantEventCount;
    private double count;
    private long startTime;
    private HashMap<Integer,Long> historyMap;

    public EventRateStore(int instantEventCount){
        this.instantEventCount=instantEventCount;
        count=0;
        historyMap=new HashMap<Integer, Long>();
        startTime=System.currentTimeMillis();

        for(int i=0;i<instantEventCount;i++){
            historyMap.put(i,startTime);
        }

    }
    public void increment(){
        Long mil=System.currentTimeMillis();
        int pos=((int)count++)%((int)instantEventCount);
        historyMap.put(pos,mil);
    }
    public double getAverageRate(){
        long mil=System.currentTimeMillis();
        double avg=count/(mil-startTime);
       // System.out.println("AVg="+avg);
        return avg;
    }
    public double getInstantRate(){
        int front = ((int)count-1)%((int)instantEventCount);
        int last = (front+1)%((int)instantEventCount);
        long gg=historyMap.get(front)-historyMap.get(last);
        float rate=(long)((float)instantEventCount)/(float)(gg+1);
        return rate;
    }

    private void printMap(){
        for(int x : historyMap.keySet()){
            System.out.print(" " + historyMap.get(x));
        }
        System.out.println("\n");
    }




    public static void main(String[] args){

//        EventRateStore eventRateStore=new EventRateStore(4);
//
//        for(int i=0;i<25;i++){
//            eventRateStore.increment();
//            try {
//                if(i>5 && i<10)    {
//                    Thread.sleep(100);
//                }
//                else {
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("\n"+i);
//            eventRateStore.getAverageRate();
//            eventRateStore.getInstantRate();
//        }

    }

}
