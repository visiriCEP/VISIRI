package org.cse.visiri.util;

import java.io.Serializable;

/**
 * Created by visiri on 10/31/14.
 */
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
