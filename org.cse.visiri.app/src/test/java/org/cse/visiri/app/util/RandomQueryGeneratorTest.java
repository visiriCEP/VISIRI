package org.cse.visiri.app.util;

import junit.framework.TestCase;
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
}
