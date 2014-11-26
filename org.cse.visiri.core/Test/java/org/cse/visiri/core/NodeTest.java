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

        Scanner sc = new Scanner(System.in);
       // System.out.println("");
       int command =   1;//sc.nextInt();

        if(command != 1)
        {
            return;
        }

//        // query 1
//        StreamDefinition inputStreamDefinition1=new StreamDefinition();
//        inputStreamDefinition1.setStreamId("car");
//        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
//        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
//        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);
//
//        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
//        inputStreamDefinitionList.add(inputStreamDefinition1);
//        String queryString="from car select brand,Id insert into filterCar;";
//
//        StreamDefinition outputStreamDefinition=new StreamDefinition();
//        outputStreamDefinition.setStreamId("filterCar");
//        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
//        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);
//
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
        outputStreamDefinition.addAttribute("Volume", StreamDefinition.Type.FLOAT);

        StreamDefinition outputStreamDefinition2=new StreamDefinition();
        outputStreamDefinition2.setStreamId("outStock2");
        outputStreamDefinition2.addAttribute("Index", StreamDefinition.Type.INTEGER);
        outputStreamDefinition2.addAttribute("Close", StreamDefinition.Type.FLOAT);
        outputStreamDefinition2.addAttribute("Low", StreamDefinition.Type.FLOAT);





        //query 2
//        StreamDefinition def1=new StreamDefinition();
//        def1.setStreamId("ABC");
//        def1.addAttribute("Att1", StreamDefinition.Type.INTEGER);
//        def1.addAttribute("Att2", StreamDefinition.Type.FLOAT);
//
//        List<StreamDefinition> inputStreamDefinitionList2=new ArrayList<StreamDefinition>();
//        inputStreamDefinitionList2.add(def1);
//        //String queryString2="from  ABC [ Att1 >= 50 ] select Att1, Att2 insert into StockQuote;";
//        String queryString2="from  ABC select Att1, Att2 insert into StockQuote;";
//        StreamDefinition outputDef=new StreamDefinition();
//        outputDef.setStreamId("StockQuote");
//        outputDef.addAttribute("Att1", StreamDefinition.Type.INTEGER);
//        outputDef.addAttribute("Att2", StreamDefinition.Type.FLOAT);
//

        String queryString="from stock " +
                "[ Open <= 50 ]#window.time(1 min) select Index, sum(Open) as sum " +
                "insert into outStock;";

        Query query1=new Query(queryString,defs,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString2="from stock " +
                "[ Open <= 50 ] select Index, Open" +
                "insert into outStock;";
        Query query2=new Query(queryString2,defs,outputStreamDefinition,"2", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString3="from stock " +
                "[ Open <= 50 and Open>25] select Open " +
                "insert into outStock;";
        Query query3=new Query(queryString3,defs,outputStreamDefinition,"3", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString4="from stock[Open > 25]#window.timeBatch( 5 minutes ) "+
                "Index, select max(Open) as maxPrice, avg(Open) as avgPrice, Volume"+
                "insert into outStock;";
        Query query4=new Query(queryString4,defs,outputStreamDefinition,"4", Configuration.ENGINE_TYPE_SIDDHI);


//        String queryString5="from stock[Open>25]#window.length(2000) join"+
//                "inputStream2#window.time(500)"+
//                "select *"+
//                "insert into outStock;";
//        Query query5=new Query(queryString5,defs,outputStreamDefinition,"5", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString5="from stock[Open > 25]#window.timeBatch( 1 hour ) "+
                "select max(Open) as maxPrice, avg(Open) as avgPrice, Volume"+
                "insert into outStock;";
        Query query5=new Query(queryString5,defs,outputStreamDefinition,"5", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString6="from stock[Open > 250]#window.length(1000) "+
                "Index, select max(Open) as maxPrice "+
                "insert into outStock2;";
        Query query6=new Query(queryString6,defs,outputStreamDefinition,"6", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString7=" from a1 = stock[Open >25] " +
                "       -> a2 = stock[High<50] " +
                "select a1.Index, a1.Open as action, b1.High as price\n" +
                "insert into outStock";
        Query query7=new Query(queryString7,defs,outputStreamDefinition,"7", Configuration.ENGINE_TYPE_SIDDHI);

        //--- add queries
        List<Query> queries = Arrays.asList(query1,query2,query3,query4,query5,query6,query7);
        node.addQueries(queries);
        node.subscribeToStream("outStock",Environment.getInstance().getNodeId()+":6666" );
        node.subscribeToStream("outStock2",Environment.getInstance().getNodeId()+":6666" );


        System.out.println("Starting in 5 seconds");
        Thread.sleep(5*1000);

        node.start();

        System.out.println("Started");
        sc.next();
    }

    public void testSubNode() throws Exception {
        node.initialize();
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
       while(node.recievedEvent==0){}
       assertEquals(Environment.EVENT_TYPE_QUERIES_CHANGED,node.recievedEvent);

        node.recievedEvent=0;
        environment.sendEvent(Environment.EVENT_TYPE_BUFFERINGSTATE_CHANGED);
        while(node.recievedEvent==0){}
        assertEquals(Environment.EVENT_TYPE_BUFFERINGSTATE_CHANGED,node.recievedEvent);

        node.recievedEvent=0;
        environment.sendEvent(Environment.EVENT_TYPE_EVENTSUBSCIBER_CHANGED);
        while(node.recievedEvent==0){}
        assertEquals(Environment.EVENT_TYPE_EVENTSUBSCIBER_CHANGED,node.recievedEvent);

//        node.recievedEvent=0;
//        environment.sendEvent(Environment.EVENT_TYPE_NODES_CHANGED);
//        while(node.recievedEvent==0){}
//        assertEquals(Environment.EVENT_TYPE_NODES_CHANGED,node.recievedEvent);

//        node.recievedEvent=0;
//        environment.sendEvent(Environment.EVENT_TYPE_NODE_START);
//        while(node.recievedEvent==0){}
//        assertEquals(Environment.EVENT_TYPE_NODE_START,node.recievedEvent);

//        node.recievedEvent=0;
//        environment.sendEvent(Environment.EVENT_TYPE_NODE_STOP);
//        while(node.recievedEvent==0){}
//        assertEquals(Environment.EVENT_TYPE_NODE_STOP,node.recievedEvent);
    }

    public void testNodesChanged() throws Exception {

    }

    public void testBufferingStateChanged() throws Exception {

    }

    public void testEventSubscriberChanged() throws Exception {

    }
}
