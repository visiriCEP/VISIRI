package org.cse.visiri.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geeth on 2014-12-01.
 */
public class DynamicQueryDistribution {

    private Map<Query,String> queryAllocation;//query->IP
    private Map<Query,String> queryRemoval;//query->IP

    public  DynamicQueryDistribution()
    {
        queryAllocation=new HashMap<Query, String>();
        queryRemoval=new HashMap<Query, String>();
    }

    public Map<Query, String> getQueryAllocation() {
        return queryAllocation;
    }

    public Map<Query, String> getQueryRemovals() {
        return queryRemoval;
    }

}
