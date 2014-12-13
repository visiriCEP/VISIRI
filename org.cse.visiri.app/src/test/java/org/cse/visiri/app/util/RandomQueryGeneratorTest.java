package org.cse.visiri.app.util;

import junit.framework.TestCase;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;

/**
 * Created by Geeth on 2014-12-11.
 */
public class RandomQueryGeneratorTest extends TestCase {
    RandomQueryGenerator qg;
    public void setUp() throws Exception {
        super.setUp();
        qg = new RandomQueryGenerator(3);
    }

    public void testGenerateDefinitions() throws Exception {
        List<StreamDefinition> defs = qg.generateDefinitions(15,2,6);
        assertEquals(15,defs.size());
        for(StreamDefinition d: defs)
        {
            assertTrue(d.getAttributeList().size() <= 6);
            assertTrue(d.getAttributeList().size() >= 2);

            System.out.print(d.getStreamId() + ": ");
            for(StreamDefinition.Attribute  a : d.getAttributeList())
            {
                System.out.print(a.getName() + ",");
            }
            System.out.println();
        }
    }

    public void testGenereateQueries() throws Exception
    {
        List<StreamDefinition> indefs = qg.generateDefinitions(150,2,6);
        List<StreamDefinition> outdefs = qg.generateDefinitions(100,2,4);

        List<Query> queries = qg.generateQueries(200,indefs,outdefs);

        for(Query q: queries)
        {
            System.out.print(q.getQueryId() +":\n" );
            for(StreamDefinition  d :q.getInputStreamDefinitionsList())
            {
                System.out.print(d.getStreamId() + " ");
            }

            System.out.println(" -> " + q.getOutputStreamDefinition().getStreamId());
            System.out.println(q.getQuery());
            System.out.println();
        }
        assertEquals(200,queries.size());
    }
}
