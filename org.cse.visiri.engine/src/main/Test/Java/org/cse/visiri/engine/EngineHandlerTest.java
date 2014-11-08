package org.cse.visiri.engine;

import junit.framework.TestCase;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

public class EngineHandlerTest extends TestCase {

    EngineHandler engineHandler;
    public void setUp() throws Exception {
        super.setUp();
       engineHandler=new EngineHandler();
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDefinition1);
        String queryString="from car select brand,Id insert into filterCar";

        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);


        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1",CEPEngine.ENGINE_TYPE_SIDDHI);

        engineHandler.addQuery(query1);
        engineHandler.start();
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