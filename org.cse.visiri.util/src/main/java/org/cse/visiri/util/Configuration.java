package org.cse.visiri.util;

/**
 * Created by Geeth on 2014-11-10.
 */
public class Configuration {

    private static int nodeType;
    public static int INSTANT_EVENT_COUNT=5;
    public static int AGENT_UPDATE_PERIOD=10;
    public static Double UTILIZATION_THRESHOULD=5.5;


    public static int getNodeType() {
        return nodeType;
    }

    public static void setNodeType(int nodeType) {
        Configuration.nodeType = nodeType;
    }
}
