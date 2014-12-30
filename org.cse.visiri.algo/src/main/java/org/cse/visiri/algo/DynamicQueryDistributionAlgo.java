package org.cse.visiri.algo;


import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by lasitha on 11/25/14.
 */
public abstract class DynamicQueryDistributionAlgo
{

    public abstract Map<Query,String> getQueryDistribution(List<Query> queries);
    public abstract String getQueryDistribution(Query q) ;
}
