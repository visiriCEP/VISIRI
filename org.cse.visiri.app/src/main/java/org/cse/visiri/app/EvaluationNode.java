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

package org.cse.visiri.app;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Node;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.*;


public class EvaluationNode {

    public static void main(String[] args) throws Exception {
        Environment environment;
        Evaluation evaluation=new Evaluation();
        Node node=new Node();

        node.initialize();

        Scanner sc = new Scanner(System.in);
        // System.out.println("");
        int command =   1;//sc.nextInt();

        if(command != 1)
        {
            return;
        }

//        List<Query> debsQueryList=evaluation.getDEBSQueries();
//        List<Query> stockQueryList=evaluation.getStockQueries();
//        List<Query> singleQ=evaluation.getQuery();
        List<Query> fireQueries=evaluation.getFireQueries();

        List<Query> queryList= new ArrayList<Query>();
//        queryList.addAll(debsQueryList);
//        queryList.addAll(stockQueryList);
//        queryList.addAll(singleQ);
        queryList.addAll(fireQueries);

        node.addQueries(queryList);
        HashMap<String,StreamDefinition> subscribeMap=new HashMap<String, StreamDefinition>();

        for(Query query:queryList){
            subscribeMap.put(query.getOutputStreamDefinition().getStreamId(),query.getOutputStreamDefinition());

        }

        Set<String> outputSet=subscribeMap.keySet();

        for(String outputStream:outputSet){
            node.subscribeToStream(outputStream,Environment.getInstance().getNodeId()+":6666");
        }


        System.out.println("Starting in 5 seconds");
        Thread.sleep(5*1000);

        node.start();

        System.out.println("Started");
        sc.next();
    }
}
