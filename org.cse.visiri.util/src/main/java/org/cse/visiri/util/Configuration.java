package org.cse.visiri.util;

/**
 * Created by Geeth on 2014-11-10.
 */
public class Configuration {

    private static int nodeType;
    public final static int INSTANT_EVENT_COUNT=100;
    public final static int AVERAGE_EVENT_COUNT=20000;
    public final static int AGENT_UPDATE_PERIOD=2000;
    public final static Double UTILIZATION_THRESHOULD=800.0;
    public final static int ENGINE_TYPE_DIRECT=0;
    public final static int ENGINE_TYPE_SIDDHI=1;

    public final static int EVENT_RATE_FREQ=50000;


    public static int getNodeType() {
        return nodeType;
    }

    public static void setNodeType(int nodeType) {
        Configuration.nodeType = nodeType;
    }
}
