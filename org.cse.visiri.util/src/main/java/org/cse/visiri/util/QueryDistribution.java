package org.cse.visiri.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-03.
 */
public class QueryDistribution {

    private Map<Query,List<Query>> generatedQueries;//original query->rewrited query
    private Map<Query,String> queryAllocation;//query->IP


    public  QueryDistribution()
    {
        generatedQueries = new HashMap<Query,List<Query>>();
        queryAllocation=new HashMap<Query, String>();
    }

    public Map<Query, List<Query>> getGeneratedQueries() {
        return generatedQueries;
    }

    public Map<Query, String> getQueryAllocation() {
        return queryAllocation;
    }
}
