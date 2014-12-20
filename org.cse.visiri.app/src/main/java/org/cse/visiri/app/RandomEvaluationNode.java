package org.cse.visiri.app;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Node;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by visiri on 12/7/14.
 */
public class RandomEvaluationNode {

    public static void main(String[] args) throws Exception {

        Node node=new Node();

        node.initialize();

        Scanner sc = new Scanner(System.in);

        RandomEvaluation ev = new RandomEvaluation();
        List<Query> queryList= ev.getQueries();
        node.addQueries(queryList);
        HashMap<String,StreamDefinition> subscribeMap=new HashMap<String, StreamDefinition>();

        for(Query query:queryList){
            subscribeMap.put(query.getOutputStreamDefinition().getStreamId(),query.getOutputStreamDefinition());
        }

        Set<String> outputSet=subscribeMap.keySet();

        for(String outputStream:outputSet){
            node.subscribeToStream(outputStream,Environment.getInstance().getNodeId()+":6666");
        }

        System.out.println("Starting in 30 seconds");

        node.start();

        System.out.println("Started");
        sc.next();
    }
}
