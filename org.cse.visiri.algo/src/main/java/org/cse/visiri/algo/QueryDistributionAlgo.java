package org.cse.visiri.algo;

import org.cse.visiri.util.QueryDistribution;

/**
 * Created by Geeth on 2014-11-04.
 */
public abstract class QueryDistributionAlgo {

    public final static int SCTXPF_ALGO=0;
    public final static int SCTXPF_PLUS_ALGO=1;
    public final static int ROUNDROBIN_ALGO=2;
    public final static int RANDOM_DISTRIBUTOR_ALGO=3;

    public abstract QueryDistribution getQueryDistribution(QueryDistributionParam param);
}
