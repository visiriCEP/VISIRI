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

import java.io.Serializable;


public class Utilization implements Serializable {

    private double JVMCpuUtilization;
    private double freeMemoryPercentage;
    private double averageSystemLoad;
    private double overallUtilizationValue;
    private double recentCpuUsage;

    public Utilization(){};

    public Utilization(double cpuUtilization){
        this.setJVMCpuUtilization(cpuUtilization);
    }

    public double getAverageSystemLoad() {
        return averageSystemLoad;
    }

    public void setAverageSystemLoad(double averageSystemLoad) {
        this.averageSystemLoad = averageSystemLoad;
    }

    public double getFreeMemoryPercentage() {
        return freeMemoryPercentage;
    }

    public void setRecentCpuUsage(double recentCpuUsage){
        this.recentCpuUsage=recentCpuUsage;
    }

    public double getRecentCpuUsage(){
        return this.recentCpuUsage;
    }

    public void setFreeMemoryPercentage(double freeMemoryPercentage) {
        this.freeMemoryPercentage = freeMemoryPercentage;
    }

    public double getJVMCpuUtilization() {
        return JVMCpuUtilization;
    }

    private void calculateOverallUtilizationValue(){

        this.overallUtilizationValue=(100-freeMemoryPercentage); //this value should be model with results
    }

    public double getOverallUtilizationValue(){
        //calculateOverallUtilizationValue();
        //return overallUtilizationValue;
        return (100-freeMemoryPercentage);
    }

    public void setJVMCpuUtilization(double JVMCpuUtilization) {
        this.JVMCpuUtilization = JVMCpuUtilization;
    }
}
