package org.cse.visiri.algo;

import org.cse.visiri.util.Query;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-03.
 */
public class QueryDistribution {

    private Map<Query,List<Query>> generatedQueries;
    private Map<Query,String> queryAllocation;


    public  QueryDistribution()
    {
        generatedQueries = new HashMap<Query,List<Query>>();
    }

    public Map<Query, List<Query>> getGeneratedQueries() {
        return generatedQueries;
    }

    public Map<Query, String> getQueryAllocation() {
        return queryAllocation;
    }
}
