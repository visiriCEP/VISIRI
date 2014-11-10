package org.cse.visiri.util;

/**
 * Created by Geeth on 2014-11-10.
 */
public class Configuration {

    private static int nodeType;


    public static int getNodeType() {
        return nodeType;
    }

    public static void setNodeType(int nodeType) {
        Configuration.nodeType = nodeType;
    }
}
