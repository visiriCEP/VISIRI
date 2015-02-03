package org.cse.visiri.app;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Node;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.*;

/**
 * Created by visiri on 12/7/14.
 */
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
        List<Query> singleQ=evaluation.getQuery();

        List<Query> queryList= new ArrayList<Query>();
//        queryList.addAll(debsQueryList);
//        queryList.addAll(stockQueryList);
        queryList.addAll(singleQ);

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
