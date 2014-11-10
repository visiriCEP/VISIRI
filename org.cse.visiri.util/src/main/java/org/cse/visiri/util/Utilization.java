package org.cse.visiri.util;

import java.io.Serializable;

/**
 * Created by visiri on 10/31/14.
 */
public class Utilization implements Serializable {

    private double cpuUtilization;

    public Utilization(){};

    public Utilization(double cpuUtilization){
        this.setCpuUtilization(cpuUtilization);
    }


    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }
}
