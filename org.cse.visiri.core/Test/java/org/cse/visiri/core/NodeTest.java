package org.cse.visiri.core;

import junit.framework.TestCase;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.CEPEngine;
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

        environment=Environment.getInstance();
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

        // query 1
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDefinition1);
        String queryString="from car select brand,Id insert into filterCar;";

        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);

        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1",CEPEngine.ENGINE_TYPE_SIDDHI);

        //query 2
        StreamDefinition def1=new StreamDefinition();
        def1.setStreamId("ABC");
        def1.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        def1.addAttribute("Att2", StreamDefinition.Type.FLOAT);

        List<StreamDefinition> inputStreamDefinitionList2=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList2.add(def1);
        //String queryString2="from  ABC [ Att1 >= 50 ] select Att1, Att2 insert into StockQuote;";
        String queryString2="from  ABC select Att1, Att2 insert into StockQuote;";
        StreamDefinition outputDef=new StreamDefinition();
        outputDef.setStreamId("StockQuote");
        outputDef.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        outputDef.addAttribute("Att2", StreamDefinition.Type.FLOAT);

        Query query2=new Query(queryString2,inputStreamDefinitionList2,outputDef,"2", CEPEngine.ENGINE_TYPE_SIDDHI);

        //--- add queries
        List<Query> queries = Arrays.asList(query1,query2);
        node.addQueries(queries);
        node.subscribeToStream("StockQuote",Environment.getInstance().getNodeId()+":6666" );

        System.out.println("Starting in 5 seconds");
        Thread.sleep(5*1000);

        node.start();

        System.out.print("enter to exit");
        sc.next();
    }

    public void testSubNode() throws Exception {
        node.initialize();
       // node.subscribeToStream("filterCar",Environment.getInstance().getNodeId()+":6666" );
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
