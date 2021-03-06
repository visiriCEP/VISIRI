/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.algo;


import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;


import java.util.*;



/**
A modified version of SCTXPF algorithm which takes query cost into account
and considers event types instead of used event attributes
**/
public class SCTXPFPlusDistributionAlgo extends QueryDistributionAlgo {

    public final double costVariability = 400;
    public final double queryVariability = 80;
    public final double utilizationThreshold = 10;

    private final int randSeed = 1;

    @Override
    public QueryDistribution getQueryDistribution(QueryDistributionParam param) {

        QueryDistribution dist = new QueryDistribution();


        Random randomizer = new Random(randSeed);



        Map<String,List<Query>> nodeQueryTable = new HashMap<String, List<Query>>(param.getNodeQueryTable());
        List<String> nodeList = new ArrayList<String>(param.getNodeList());
        // Map<String,Utilization> utilizations = new HashMap<String, Utilization>(env.getNodeUtilizations());
        Map<String,Set<String>> nodeEventTypes = new HashMap<String, Set<String>>();
        Map<String,Double> costs = new HashMap<String, Double>();
        List<String> dispatcherList = new ArrayList<String>(param.getDispatcherList());
        List<Query> queries = param.getQueries();


        // Store types of events in node list and their costs
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

        // ************* ALLOCATE QUERIES ************
        for(int qIndex =0 ; qIndex < queries.size(); qIndex++)
        {
            Query q = queries.get(qIndex);
            //take all nodes as possible candidates
            Set<String> candidateNodes = new HashSet<String>(nodeList);


            //---------- STEP 1------------
            //filter out nodes having too many queries
            {
                //Collection<List<Query>> querySets = nodeQueryTable.values();

                int minQueries = 10000000;

                for(String cand : candidateNodes)
                {
                    int thisCount = nodeQueryTable.get(cand).size();
                    if(minQueries > thisCount)
                    {
                        minQueries = thisCount;
                    }
                }

                //filter ones above threshold
                for (Iterator<String> iter = candidateNodes.iterator(); iter.hasNext(); ) {
                    String nodeId = iter.next();
                    if ( nodeQueryTable.get(nodeId).size() > minQueries + queryVariability) {
                        iter.remove(); // Error line
                    }
                }
            }


            //------------ STEP 2---------
            //minimum total cost
            double minCost = Double.POSITIVE_INFINITY;
            //find min cost
            for(String candidate : candidateNodes)
            {
                double thisCost = costs.get(candidate);
                if(minCost > thisCost)
                {
                    minCost = thisCost;
                }
            }

            //filter ones above threshold
            for(Iterator<String> iter = candidateNodes.iterator() ; iter.hasNext() ;)
            {
                String nodeId = iter.next();
                if(costs.get(nodeId) > minCost + costVariability)
                {
                    iter.remove();
                }
            }

            //types of events used
            //------------- STEP 3 ------------------
            Set<String> usedEventTypes = new HashSet<String>();
            for(StreamDefinition def : q.getInputStreamDefinitionsList())
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

            //select one node randomly
            int randIndex = randomizer.nextInt(candidateNodes.size());
            String targetNode = candidateNodes.toArray(new String[candidateNodes.size()])[randIndex];


            // *********** Add to distribution *************

            List<Query> derivedQueries = new ArrayList<Query>();
            Query nodeQuery = new Query(q,true);

            nodeQuery.setEngineId(Configuration.ENGINE_TYPE_SIDDHI);
            dist.getQueryAllocation().put(nodeQuery,targetNode);
            derivedQueries.add(nodeQuery);

            //add to dispatchers
            Query dispQuery = new Query( "", null,null,"tempquery", Configuration.ENGINE_TYPE_DIRECT,1.0);

            for(String disp : dispatcherList)
            {
                Set<String> evtTypes = nodeEventTypes.get(disp);
                for(StreamDefinition def : q.getInputStreamDefinitionsList()) {
                    String evtType = def.getStreamId();
                    if (!evtTypes.contains(evtType))
                    {
                        Query newQ = new Query(dispQuery,true);
                        StreamDefinition newDef = new StreamDefinition(def);
                        newQ.addInputStreamDefinition(newDef);
                        newQ.setOutputStreamDefinition(newDef);

                        dist.getQueryAllocation().put(newQ,disp);
                        derivedQueries.add(newQ);
                        evtTypes.add(evtType);
                    }
                }
            }

            dist.getGeneratedQueries().put(q,derivedQueries);

            // update calculated tables for allocation of next queries
            nodeQueryTable.get(targetNode).add(q);
            nodeEventTypes.get(targetNode).addAll(usedEventTypes);

            //System.out.println(qIndex);

            costs.put(targetNode, costs.get(targetNode) +q.getCost());
        }

        return dist;
    }

}
