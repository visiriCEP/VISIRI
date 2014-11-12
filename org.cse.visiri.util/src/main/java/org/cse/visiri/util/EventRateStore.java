package org.cse.visiri.util;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Malinda Kumarasinghe on 11/12/2014.
 */
public class EventRateStore {
    private int period;
    private int eventCount;
    private int hCount;
    private double count;
    private long startTime;
    private HashMap<Integer,Long> historyMap;
   // private Calendar calendar;

    public EventRateStore(int eventCount){
        this.eventCount=eventCount;
        count=0;
      //calendar = Calendar.getInstance();
        historyMap=new HashMap<Integer, Long>();
        startTime=System.currentTimeMillis();
    }
    public void increment(){
        Long mil=System.currentTimeMillis();
        //count++;

        int pos=((int)count++)%eventCount;

        historyMap.put(pos,mil);


        System.out.println("Pos="+pos+"  "+mil);
    }
    public double getAverage(){
        long mil=System.currentTimeMillis();
        return count/(mil-startTime);
    }





    public static void main(String[] args){
        EventRateStore eventRateStore=new EventRateStore(4);
        eventRateStore.increment();

        eventRateStore.increment();

        for(int i=0;i<10;i++){
            eventRateStore.increment();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        eventRateStore.increment();
    }

}
