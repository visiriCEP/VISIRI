package org.cse.visiri.algo;

import org.cse.visiri.algo.util.costmodelcalc.CostModelCalculator;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.CEPEngine;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

import java.util.*;

/**
 * Created by lasitha on 11/21/14.
 */
public class RoundRobinDistributionAlgo extends QueryDistributionAlgo{
    @Override
    public QueryDistribution getQueryDistribution(List<Query> queries) {
        QueryDistribution dist = new QueryDistribution();
        Environment env = Environment.getInstance();

        Random randomizer = new Random();

        Map<String,List<Query>> nodeQueryTable = new HashMap<String, List<Query>>(env.getNodeQueryMap());//node IP-> list of queries
        List<String> nodeList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_PROCESSINGNODE));//list of ips of processing nodes
        List<String> dispatcherList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_DISPATCHER));//dispatcher ip list

        List<Query> queryToQueryList=new LinkedList<Query>();

        int querycount=0;
        for(String str: nodeList) {
            Query tmpQ = queries.remove(querycount);
            queryToQueryList.add(tmpQ);
            dist.getGeneratedQueries().put(tmpQ, queryToQueryList);
            dist.getQueryAllocation().put(tmpQ, str);
            querycount++;
        }

        return dist;
    }
}
