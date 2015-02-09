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


import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.*;


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
