package org.cse.visiri.engine;

import junit.framework.TestCase;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineHandlerTest extends TestCase {

    EngineHandler engineHandler;
    public void setUp() throws Exception {
        super.setUp();
        engineHandler=new EngineHandler("EngineHandlerTest");
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

        Map<String, List<String>> mp = Environment.getInstance().getSubscriberMapping();
        List<String> subs = new ArrayList<String>();
        subs.add("localhost:6666");
        mp.put("filterCar",subs);
        List<String> subs2=new ArrayList<String>();
        subs2.add("localhost:6666");
        mp.put("StockQuote",subs2);


        Environment.getInstance().setNodeType(Environment.NODE_TYPE_PROCESSINGNODE);

        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);

        engineHandler.addQuery(query1);


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

        Query query2=new Query(queryString2,inputStreamDefinitionList2,outputDef,"2",Configuration.ENGINE_TYPE_SIDDHI);

        engineHandler.addQuery(query2);

    }

    public void testStart() throws Exception {
            engineHandler.start();
    }

//    public void testStop() throws Exception {
//
//    }
//
//    public void testSendEvents() throws Exception {
//
//    }
//
//    public void testAddQuery() throws Exception {
//
//    }
//
//    public void testRemoveQuery() throws Exception {
//
//    }
//
//    public void testGetQueryEngineMap() throws Exception {
//
//    }
//
//    public void testGetEventEngineMap() throws Exception {
//
//    }
//
//    public void testGetOutputEventReceiver() throws Exception {
//
//    }
}