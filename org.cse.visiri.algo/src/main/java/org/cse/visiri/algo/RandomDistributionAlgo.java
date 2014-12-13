package org.cse.visiri.algo;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

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

        Map<String,Set<String>> nodeEventTypes = new HashMap<String, Set<String>>();


        // Store types of events in node list and their costs
        for(String str: nodeList)
        {
            if(!nodeQueryTable.containsKey(str))
            {
                nodeQueryTable.put(str,new ArrayList<Query>());
            }


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

        for(Query q:queries){
            int randomNum=randomizer.nextInt(nodeList.size());
            //queryToQueryList.add(q);

            String targetNode = nodeList.get(randomNum);
            dist.getQueryAllocation().put(q,targetNode );

            Query nodeQuery = new Query(q,true);

            nodeQuery.setEngineId(Configuration.ENGINE_TYPE_SIDDHI);
          //  dist.getQueryAllocation().put(nodeQuery,targetNode);
            queryToQueryList.add(nodeQuery);

            //add to dispatchers
            Query dispQuery = new Query( "", null,null,"tempquery", Configuration.ENGINE_TYPE_DIRECT);


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
                        queryToQueryList.add(newQ);
                        evtTypes.add(evtType);
                    }
                }
            }
            dist.getGeneratedQueries().put(q, queryToQueryList);

        }

        return dist;
    }
}
