package org.cse.visiri.algo;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.List;

/**
 * Created by lasitha on 11/25/14.
 */
public abstract class DynamicQueryDistributionAlgo {
    public abstract QueryDistribution getRuntimeQueryDistribution(List<Query> queries);

}
