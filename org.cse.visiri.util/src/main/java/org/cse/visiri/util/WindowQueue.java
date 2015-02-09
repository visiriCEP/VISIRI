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
