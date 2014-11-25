package org.cse.visiri.algo;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.*;

/**
 * Created by lasitha on 11/24/14.
 */
public class RandomDistributionAlgo extends QueryDistributionAlgo {

    @Override
    public QueryDistribution getQueryDistribution(List<Query> queries) {
        QueryDistribution dist = new QueryDistribution();
        Environment env = Environment.getInstance();

        Random randomizer = new Random();

        Map<String,List<Query>> nodeQueryTable = new HashMap<String, List<Query>>(env.getNodeQueryMap());//node IP-> list of queries
        List<String> nodeList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_PROCESSINGNODE));//list of ips of processing nodes
        List<String> dispatcherList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_DISPATCHER));//dispatcher ip list

        List<Query> queryToQueryList=new LinkedList<Query>();

        for(Query q:queries){
            int randomNum=randomizer.nextInt(nodeList.size());
            queryToQueryList.add(q);
            dist.getGeneratedQueries().put(q, queryToQueryList);
            dist.getQueryAllocation().put(q, nodeList.get(randomNum));
        }

        return dist;
    }
}
