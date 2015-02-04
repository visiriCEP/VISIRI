package org.cse.visiri.algo.util;

import junit.framework.TestCase;
import org.cse.visiri.util.Utilization;

public class UtilizationUpdaterTest extends TestCase {

    UtilizationUpdater utilizationUpdater;

    public void testStart() throws Exception {
        utilizationUpdater=new UtilizationUpdater();
        utilizationUpdater.start();

        Utilization utilization=utilizationUpdater.update();

        System.out.println("JVM usage : " +utilization.getJVMCpuUtilization());
        System.out.println("Average CPU load : "+ utilization.getAverageSystemLoad());
        System.out.println("memory usage persentage : "+(100-utilization.getFreeMemoryPercentage()));
        System.out.println("Recent CPU usage : "+utilization.getRecentCpuUsage());

    }

    public void testUpdate() throws Exception {

    }

    public void testStop() throws Exception {

    }
}