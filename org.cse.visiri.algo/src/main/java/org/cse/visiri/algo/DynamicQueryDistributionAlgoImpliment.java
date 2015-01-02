package org.cse.visiri.algo;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.DynamicQueryDistribution;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.Utilization;

import java.util.*;

/**
 * Created by Geeth on 2014-12-01.
 */
public class DynamicQueryDistributionAlgoImpliment extends DynamicQueryDistributionAlgo{

    public final double costThreshold = 10;
    public final double utilizationThreshold = 10;

    private final int seed= 1;
    private Random randomizer = new Random(seed);

    @Override
    public Map<String,List<Query> > getQueryDistribution(List<Query> queries) {
        Map<String,List<Query>> dist = new HashMap<String,List<Query>>();
        for(Query q: queries)
        {
            String node = getQueryDistribution(q);
            List<Query> nodeQuery;
            if(!dist.containsKey(node))
            {
                dist.put(node, new ArrayList<Query>());
            }
            nodeQuery = dist.get(node);

            nodeQuery.add(q);
        }

        return dist;
    }

    @Override
    public String getQueryDistribution(Query query) {
        DynamicQueryDistribution dist = new DynamicQueryDistribution();
        Environment env = Environment.getInstance();

        Map<String,List<Query>> nodeQueryTable = new HashMap<String, List<Query>>(env.getNodeQueryMap());
        List<String> nodeList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_PROCESSINGNODE));
        // Map<String,Utilization> utilizations = new HashMap<String, Utilization>(env.getNodeUtilizations());
        Map<String,Set<String>> nodeEventTypes = new HashMap<String, Set<String>>();
        Map<String,Double> costs = new HashMap<String, Double>();
        List<String> dispatcherList = new ArrayList<String>(env.getNodeIdList(Environment.NODE_TYPE_DISPATCHER));

        for(String str: nodeList)
        {
            //calculate costs of each node
            double cost = 0.0;

            if(!nodeQueryTable.containsKey(str))
            {
                nodeQueryTable.put(str,new ArrayList<Query>());
            }

            for(Query q: nodeQueryTable.get(str))
            {
                cost += q.getCost();
            }
            costs.put(str,cost);

            // calculate event type counts
            Set<String> eventTypes = new HashSet<String>();
            List<Query> existingQueries = nodeQueryTable.get(str);

            for(Query q: existingQueries)
            {
                for(StreamDefinition def : q.getInputStreamDefinitionsList())
                {
                    eventTypes.add(def.getStreamId());
                }
            }
            nodeEventTypes.put(str,eventTypes);

        }
        //store types of events in dispatcher list
        for(String str: dispatcherList)
        {
            if(!nodeQueryTable.containsKey(str))
            {
                nodeQueryTable.put(str,new ArrayList<Query>());
            }

            Set<String> eventTypes = new HashSet<String>();
            List<Query> existingQueries = nodeQueryTable.get(str);
            for(Query q: existingQueries)
            {
                for(StreamDefinition def : q.getInputStreamDefinitionsList())
                {
                    eventTypes.add(def.getStreamId());
                }
            }
            nodeEventTypes.put(str,eventTypes);
        }

        Set<String> candidateNodes = new HashSet<String>(nodeList);

        String thisNode = env.getNodeId();
        candidateNodes.remove(thisNode);

        //minimum utilization
        Map<String, Utilization> utilizations = env.getNodeUtilizations();
        double minUtil = Double.MAX_VALUE;
        for(String node : candidateNodes)
        {
            double util = utilizations.get(node).getFreeMemoryPercentage();
            util = 100 - util;
            if(util < minUtil)
            {
                minUtil = util;
            }
        }

        //filter ones above threshold
        for(Iterator<String> iter = candidateNodes.iterator() ; iter.hasNext() ;)
        {
            String nodeId = iter.next();
            double util = 100 - utilizations.get(nodeId).getFreeMemoryPercentage();
            if(util  > minUtil + utilizationThreshold)
            {
                iter.remove();
            }
        }


        //minimum total cost
        double minCost = Double.MAX_VALUE;
        for(String node : candidateNodes)
        {
            double cost = costs.get(node);
            if(cost < minCost)
            {
                minCost = cost;
            }
        }

        //filter ones above threshold
        for(Iterator<String> iter = candidateNodes.iterator() ; iter.hasNext() ;)
        {
            String nodeId = iter.next();
            if(costs.get(nodeId) > minCost + costThreshold)
            {
                iter.remove();
            }
        }

        Set<String> usedEventTypes = new HashSet<String>();
        for(StreamDefinition def : query.getInputStreamDefinitionsList())
        {
            usedEventTypes.add(def.getStreamId());
        }

        //number of common event streams
        int commonMax = 0;
        List<String> maximumCommonEventNodes = new ArrayList<String>();
        //find nodes with maximum common event
        for(String node: candidateNodes)
        {
            //find common count
            Set<String> curTypes = new HashSet<String>(usedEventTypes);
            curTypes.retainAll(nodeEventTypes.get(node));
            int count = curTypes.size();

            if(count == commonMax)
            {
                maximumCommonEventNodes.add(node);
            }
            else if(count > commonMax)
            {
                commonMax = count;
                maximumCommonEventNodes.clear();
                maximumCommonEventNodes.add(node);
            }
        }

        candidateNodes.retainAll(maximumCommonEventNodes);

        // TODO : EVENT RATES

        // TODO : EVENT TYPES

        int randIndex = randomizer.nextInt(candidateNodes.size());
        String targetNode = candidateNodes.toArray(new String[candidateNodes.size()])[randIndex];

        return targetNode;
    }
}
