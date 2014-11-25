package org.cse.visiri.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 11/25/2014.
 */
public class DynamicQueryDistribution {

    private Map<String,String> engineAllocation;//query->IP

    public  DynamicQueryDistribution(){
        engineAllocation=new HashMap<String, String>();
    }

    public Map<String, String> getEngineAllocation() {
        return engineAllocation;
    }
}
