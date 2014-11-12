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

        for(int i=0;i<eventCount;i++){
            historyMap.put(i,startTime);
        }

    }
    public void increment(){
        Long mil=System.currentTimeMillis();
        //count++;

        int pos=((int)count++)%eventCount;

        historyMap.put(pos,mil);


       // System.out.println("Pos="+pos+"  "+mil);
        getAverageRecent();
        printMap();
    }
    public double getAverageAll(){
        long mil=System.currentTimeMillis();
        return count/(mil-startTime);
    }
    public double getAverageRecent(){
        int front = ((int)count-1)%eventCount;
        int last = (front+1)%eventCount;

        long gg=historyMap.get(front)-historyMap.get(last);
        
        System.out.println("front="+front+"   last="+last   +"   gap = "+gg);
        return 0.0;
    }

    private void printMap(){
        for(int x : historyMap.keySet()){
            System.out.print(" " + historyMap.get(x));
        }
        System.out.println("\n");
    }




    public static void main(String[] args){
        int sle=100;
        EventRateStore eventRateStore=new EventRateStore(6);
        eventRateStore.increment();
        eventRateStore.increment();

        for(int i=0;i<15;i++){
            eventRateStore.increment();
            try {
                if(i==5)    {
                    sle=500;
                    System.out.println("sleeep");
                }
                else    sle=100;

                Thread.sleep(sle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //eventRateStore.increment();
    }

}
