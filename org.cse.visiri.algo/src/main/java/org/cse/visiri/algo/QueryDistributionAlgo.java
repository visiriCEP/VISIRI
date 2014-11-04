package org.cse.visiri.algo;

import org.cse.visiri.util.Query;

import java.util.List;

/**
 * Created by Geeth on 2014-11-04.
 */
public abstract class QueryDistributionAlgo {

    abstract QueryDistribution getQueryDistribution(List<Query> queries);
}
