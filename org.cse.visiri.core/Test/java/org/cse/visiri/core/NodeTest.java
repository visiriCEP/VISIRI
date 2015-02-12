package org.cse.visiri.core;

import junit.framework.TestCase;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.CEPEngine;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Malinda Kumarasinghe on 11/10/2014.
 */
public class NodeTest extends TestCase {

    Environment environment;
    Node node;
    public void setUp() throws Exception {
        super.setUp();

      //  environment=Environment.getInstance();
        node=new Node();


    }

    public void tearDown() throws Exception {

    }

    public void testMainNode() throws Exception {
        node.initialize();
        int command =   1;//sc.nextInt();

        if(command != 1)
        {
            return;
        }

        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("stock");
        inputStreamDefinition1.addAttribute("Index", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Open", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("High", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Low", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Close", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Date", StreamDefinition.Type.STRING);
        defs.add(inputStreamDefinition1);

        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("outStock");
        outputStreamDefinition.addAttribute("Index", StreamDefinition.Type.INTEGER);
        outputStreamDefinition.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("High", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Low", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Close", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        outputStreamDefinition.addAttribute("Date", StreamDefinition.Type.STRING);

        StreamDefinition outputStreamDefinition2=new StreamDefinition();
        outputStreamDefinition2.setStreamId("outStock2");
        outputStreamDefinition2.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinition2.addAttribute("High", StreamDefinition.Type.FLOAT);

        String queryString="from stock " +
                "select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";

        Query query1=new Query(queryString,defs,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString2="from stock " +
                "[ Open <= 50 ] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query query2=new Query(queryString2,defs,outputStreamDefinition,"2", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString3="from stock " +
                "[ Open <= 50 and Open>25] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query query3=new Query(queryString3,defs,outputStreamDefinition,"3", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString4="from stock[Open > 25]#window.timeBatch( 10 seconds ) "+
                " select max(Open) as Open, avg(Open) as High "+
                "insert into outStock2;";
        Query query4=new Query(queryString4,defs,outputStreamDefinition2,"4", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString5="from stock[Open > 25]#window.timeBatch( 1 hour ) "+
                "select max(Open) as Open, avg(Open) as High "+
                " insert into outStock2;";
        Query query5=new Query(queryString5,defs,outputStreamDefinition2,"5", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString6="from stock[Open > 250]#window.length(1000) "+
                "select max(Open) as Open, avg(High) as High "+
                "insert into outStock2;";
        Query query6=new Query(queryString6,defs,outputStreamDefinition2,"6", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString7=" from a1 = stock[Open >25] " +
                "       -> a2 = stock[High<50] " +
                "select a1.Open as Open, a2.High as High " +
                "insert into outStock2";
        Query query7=new Query(queryString7,defs,outputStreamDefinition2,"7", Configuration.ENGINE_TYPE_SIDDHI);

        List<Query> queries = Arrays.asList(query1,query2,query3,query4,query5,query6,query7);
        node.addQueries(queries);
        node.subscribeToStream("outStock",Environment.getInstance().getNodeId()+":6666" );
        node.subscribeToStream("outStock2",Environment.getInstance().getNodeId()+":6666" );

    }

    public void testSubNode() throws Exception {
        node.initialize();
        node.start();
        System.out.println("Subnode started");
        Scanner sc = new Scanner(System.in);
        sc.next();
       // node.subscribeToStream("filterCar",Environment.getInstance().getNodeId()+":6666" );
    }

    public void testOnMessage() throws Exception {

        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_QUERIES_CHANGED);
        Scanner sc=new Scanner(System.in);
        sc.next();

    }
    public void testSubscribeToStream() throws Exception {

    }

    public void testUnsubscribeFromStream() throws Exception {

    }

    public void testQueriesChanged() throws Exception {
       try{
           node.start();
       }
       catch (NullPointerException e){

       }
       environment.sendEvent(Environment.EVENT_TYPE_QUERIES_CHANGED);

    }

    public void testNodesChanged() throws Exception {

    }

    public void testBufferingStateChanged() throws Exception {

    }

    public void testEventSubscriberChanged() throws Exception {

    }
}
