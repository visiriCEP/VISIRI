package org.cse.visiri.algo;


import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.*;

/**
 * Created by lasitha on 11/21/14.
 */
public class RoundRobinDistributionAlgo extends QueryDistributionAlgo{
    @Override
    public QueryDistribution getQueryDistribution(QueryDistributionParam param) {
        QueryDistribution dist = new QueryDistribution();


        Random randomizer = new Random();

        Map<String,List<Query>> nodeQueryTable = new HashMap<String, List<Query>>(param.getNodeQueryTable());//node IP-> list of queries
        List<String> nodeList = new ArrayList<String>(param.getNodeList());//list of ips of processing nodes
        List<String> dispatcherList = new ArrayList<String>(param.getDispatcherList());//dispatcher ip list

        List<Query> queryToQueryList=new LinkedList<Query>();

        List<Query> queries = param.getQueries();

        int querycount=0;
        while(querycount<queries.size()) {
            for (String str : nodeList) {
                System.out.println(str);
                Query tmpQ = queries.get(querycount);
                queryToQueryList.add(tmpQ);
                dist.getGeneratedQueries().put(tmpQ, queryToQueryList);
                dist.getQueryAllocation().put(tmpQ, str);
                querycount++;
            }
        }
        return dist;
    }
}
