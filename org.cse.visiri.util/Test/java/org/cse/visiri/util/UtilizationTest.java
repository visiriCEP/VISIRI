package org.cse.visiri.util;

import junit.framework.TestCase;

/**
 * Created by Malinda Kumarasinghe on 2/13/2015.
 */
public class UtilizationTest extends TestCase {
    Utilization utilization;
    public void setUp() throws Exception {
        super.setUp();
        utilization=new Utilization();

    }

    public void testGetAverageSystemLoad() throws Exception {
        utilization.setAverageSystemLoad(500.0);
        assertEquals(500.0,utilization.getAverageSystemLoad());
    }

    public void testSetAverageSystemLoad() throws Exception {
        utilization.setAverageSystemLoad(4000.0);
        assertEquals(4000.0,utilization.getAverageSystemLoad());
    }

    public void testGetFreeMemoryPercentage() throws Exception {
        utilization.setFreeMemoryPercentage(50.0);
        assertEquals(50.0,utilization.getFreeMemoryPercentage());
    }

    public void testSetRecentCpuUsage() throws Exception {
        utilization.setRecentCpuUsage(40.0);
        assertEquals(40.0,utilization.getRecentCpuUsage());
    }

    public void testGetRecentCpuUsage() throws Exception {
        utilization.setRecentCpuUsage(120.0);
        assertEquals(120.0,utilization.getRecentCpuUsage());
    }

    public void testSetFreeMemoryPercentage() throws Exception {
        utilization.setFreeMemoryPercentage(20.0);
        assertEquals(20.0,utilization.getFreeMemoryPercentage());
    }

    public void testGetJVMCpuUtilization() throws Exception {
        utilization.setJVMCpuUtilization(65.0);
        assertEquals(65.0,utilization.getJVMCpuUtilization());
    }

    public void testGetOverallUtilizationValue() throws Exception {
        utilization.setFreeMemoryPercentage(77.0);
        assertEquals(100-utilization.getFreeMemoryPercentage(),utilization.getOverallUtilizationValue());
    }

    public void testSetJVMCpuUtilization() throws Exception {
        utilization.setJVMCpuUtilization(65.0);
        assertEquals(65.0,utilization.getJVMCpuUtilization());
    }
}
