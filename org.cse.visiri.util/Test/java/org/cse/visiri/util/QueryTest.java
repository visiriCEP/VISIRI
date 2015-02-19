package org.cse.visiri.util;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malinda Kumarasinghe on 2/12/2015.
 */
public class QueryTest extends TestCase {
    Query query;
    List<StreamDefinition> defs;
    StreamDefinition sd;
    public void setUp() throws Exception {
        super.setUp();
        defs = new ArrayList<StreamDefinition>();

        sd =new StreamDefinition("fire",null);
        sd.addAttribute("location", StreamDefinition.Type.STRING);
        sd.addAttribute("temperature", StreamDefinition.Type.DOUBLE);
        sd.addAttribute("casualties", StreamDefinition.Type.BOOLEAN);
        defs.add(sd);
        query=new Query("TEST QUERY",defs,sd,"id1",1,4.5D);

    }

    public void testGetQuery() throws Exception {

        assertEquals(query.getQuery(),"TEST QUERY");
    }

    public void testSetQuery() throws Exception {
        query.setQuery("TEST QUERY2");
        assertEquals(query.getQuery(),"TEST QUERY2");

    }

    public void testGetInputStreamDefinitionsList() throws Exception {
        assertEquals(query.getInputStreamDefinitionsList(),defs);
    }

    public void testSetInputStreamDefinitionsList() throws Exception {
        ArrayList<StreamDefinition> defs2 = new ArrayList<StreamDefinition>();
        query.setInputStreamDefinitionsList(defs2);
        assertEquals(query.getInputStreamDefinitionsList(),defs2);

    }

    public void testAddInputStreamDefinition() throws Exception {
        assertEquals(1,query.getInputStreamDefinitionsList().size());
        assertEquals(sd,query.getInputStreamDefinitionsList().get(0));
    }

    public void testGetOutputStreamDefinition() throws Exception {
        assertEquals(sd,query.getOutputStreamDefinition());
    }

    public void testSetOutputStreamDefinition() throws Exception {
        query.setOutputStreamDefinition(sd);
        assertEquals(sd,query.getOutputStreamDefinition());
    }

    public void testGetQueryId() throws Exception {
        query.setQueryId("0001");
        assertEquals("0001",query.getQueryId());
    }

}
