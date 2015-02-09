/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.util;


public class EventRateStore {
    //TODO check for accuracy of this results.
    private Long[] instantMap,avgMap;
    private int instantPos=-1;
    private int avgPos=-1;
    private int instantMapSize,avgMapSize;
    private static EventRateStore instance;

    public EventRateStore(){

        instantMapSize=Configuration.INSTANT_EVENT_COUNT;
        avgMapSize=Configuration.AVERAGE_EVENT_COUNT;

        instantMap=new Long[instantMapSize];
        avgMap=new Long[avgMapSize];

        long startTime=System.currentTimeMillis();

        for(int i=0;i<instantMapSize;i++){
            instantMap[i]=startTime;
        }

        for(int i=0;i<avgMapSize;i++){
            avgMap[i]=0L;
        }
        // initialIncrement();
    }

//    public void initialIncrement(){
//        Long mil=System.currentTimeMillis();
//
//        instantPos = (++instantPos >= instantMapSize) ? 0 : instantPos;
//        instantMap[instantPos]=mil;
//
//        avgPos = (++avgPos >= avgMapSize) ? 0 : avgPos;
//        avgMap[avgPos]=mil;
//
//    }

    public void increment(){
        Long mil=System.currentTimeMillis();

        instantPos = (++instantPos >= instantMapSize) ? 0 : instantPos;
        instantMap[instantPos]=mil;

        avgPos = (++avgPos >= avgMapSize) ? 0 : avgPos;
        avgMap[avgPos]=mil;

        if(avgPos%Configuration.EVENT_RATE_FREQ==0){
            //System.out.print(".");
            // System.out.println(message+" ## Ins : "+getInstantRate()+"\tAvg : "+getAverageRate());
        }
    }



    public double getInstantRate(){
        try{
            int idxLast=instantPos;
            int idxFirst=(instantPos + 1)%(instantMapSize);

            long last=instantMap[idxLast];
            long first=instantMap[idxFirst];

            double avg=((double)instantMapSize)/(double)(last-first+1);
            return avg*1000;
        }catch (ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }
    public double getAverageRate(){


        if(avgMap[0]==0)
                return 0;

        try {
            Long mil=System.currentTimeMillis();

            int idxLast = avgPos;
            int idxFirst = (avgPos + 1) % (avgMapSize);

           // long last = avgMap[idxLast];
            long last = mil;
            long first = avgMap[idxFirst];

            double avg = ((double) avgMapSize) / (double) (last - first + 1);
            return avg * 1000;
        }catch (ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

//    private void printMap(){
//        for(int x : historyMap.keySet()){
//            System.out.print(" " + historyMap.get(x));
//        }
//        System.out.println("\n");
//    }




    public static void main(String[] args){

        EventRateStore eventRateStore=new EventRateStore();

        // eventRateStore.getAverageRate();

        for(int i=0;i<50;i++){
            //eventRateStore.increment();
            System.out.println(eventRateStore.getAverageRate());
        }

        eventRateStore.increment();

        for(int i=0;i<5000;i++){
            //eventRateStore.increment();

            if(i%100==0)
                eventRateStore.increment();


            System.out.println(eventRateStore.getAverageRate());
        }
/*
        for(int i=0;i<5000000;i++){
            eventRateStore.increment(i+" dd");
            try {
                if(i>5 && i<10)    {
                   // Thread.sleep(50);
                }
                else {
                   // Thread.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

           // System.out.print(i+"\t"+eventRateStore.getAverageRate()*1000);
            //System.out.println("\t\t"+i+"\t"+eventRateStore.getInstantRate()*1000);

        }
*/
//        for(int i=0;i<25;i++){
//            eventRateStore.getAverageRate();
//        }

    }

}