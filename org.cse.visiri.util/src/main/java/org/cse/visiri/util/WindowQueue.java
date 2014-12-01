package org.cse.visiri.util;

/**
 * Created by visiri on 11/25/14.
 */
public class WindowQueue {

    private int size;
    private double[] valueArray;
    private int pos;

    public WindowQueue(int size){

        this.size=size;
        valueArray=new double[size];
        pos=0;
    }

    public void add(double val){
        if(pos==size) {
            pos = 0;
        }
        valueArray[pos]=val;
        pos++;
    }

    public double getAverage(){
        double sum=0;
        for(int i=0;i<size;i++){
            sum+=valueArray[i];
        }

        return sum/size;
    }

}
