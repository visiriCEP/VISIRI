package org.cse.visiri.algo.util;

import com.sun.management.OperatingSystemMXBean;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Utilization;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.lang.management.ManagementFactory;

/**
 * Created by visiri on 10/31/14.
 */
public class UtilizationUpdater {
        private Utilization utilization;


    public UtilizationUpdater(){
        this.utilization=new Utilization();
    }

    public void start(){

        Environment.getInstance().setNodeUtilizations(utilization);

    }

    public void update(){

    }

    public  double getUtilizationLevel(){
        throw new UnsupportedOperationException();
    }


    public void stop(){

    }

    private void updateUsingMXBeans(){
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        System.out.println(bean.getProcessCpuLoad()*100); //Returns the "recent cpu usage" for the Java Virtual Machine process
        System.out.println(bean.getSystemCpuLoad()*100); //Returns the "recent cpu usage" for the whole system
        System.out.println(bean.getSystemLoadAverage()); //Returns the system load average for the last minute

        double free=bean.getFreePhysicalMemorySize();
        double tot=bean.getTotalPhysicalMemorySize();

        double perc=(free/tot)*100;     //free memory percentage
        System.out.println(perc);

    }

    private void updateUsingSigar()  {
        Sigar sigar=new Sigar();
        Mem mem=null;
        try {
             mem=sigar.getMem();
        } catch (SigarException e) {
            e.printStackTrace();
        }

        System.out.println("Actual total free system memory: "
                + mem.getActualFree() / 1024 / 1024+ " MB");
        System.out.println("Actual total used system memory: "
                + mem.getActualUsed() / 1024 / 1024 + " MB");
        System.out.println("Total free system memory ......: " + mem.getFree()
                / 1024 / 1024+ " MB");
        System.out.println("System Random Access Memory....: " + mem.getRam()
                + " MB");
        System.out.println("Total system memory............: " + mem.getTotal()
                / 1024 / 1024+ " MB");
        System.out.println("Total used system memory.......: " + mem.getUsed()
                / 1024 / 1024+ " MB");

        System.out.println(mem.getFreePercent());
    }

    private void updateUsingRuntime(){

        Runtime runtime=Runtime.getRuntime();

        System.out.println(runtime.totalMemory()); //Returns the total amount of memory in the Java virtual machine.
        System.out.println(runtime.freeMemory());//Returns the amount of free memory in the Java Virtual Machine.
        System.out.println(runtime.maxMemory()); //Returns the maximum amount of memory that the Java virtual machine will attempt to use.

    }

}
