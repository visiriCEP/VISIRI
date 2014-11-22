package org.cse.visiri.util;

import java.io.Serializable;

/**
 * Created by visiri on 10/31/14.
 */
public class Utilization implements Serializable {

    private double JVMCpuUtilization;
    private double freeMemoryPercentage;
    private double averageSystemLoad;

    public Utilization(){};

    public double getAverageSystemLoad() {
        return averageSystemLoad;
    }

    public void setAverageSystemLoad(double averageSystemLoad) {
        this.averageSystemLoad = averageSystemLoad;
    }

    public double getFreeMemoryPercentage() {
        return freeMemoryPercentage;
    }

    public void setFreeMemoryPercentage(double freeMemoryPercentage) {
        this.freeMemoryPercentage = freeMemoryPercentage;
    }


    public Utilization(double cpuUtilization){
        this.setJVMCpuUtilization(cpuUtilization);
    }


    public double getJVMCpuUtilization() {
        return JVMCpuUtilization;
    }

    public void setJVMCpuUtilization(double JVMCpuUtilization) {
        this.JVMCpuUtilization = JVMCpuUtilization;
    }
}
