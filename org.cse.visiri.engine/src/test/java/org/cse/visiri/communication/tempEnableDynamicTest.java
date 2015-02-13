package org.cse.visiri.communication;

import junit.framework.TestCase;

/**
 * Created by Malinda Kumarasinghe on 2/12/2015.
 */
public class tempEnableDynamicTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void testMain() throws Exception {
        Environment environment=Environment.getInstance();
        tempEnableDynamic.main(null);
        Thread.sleep(5000);
        assertEquals(environment.checkDynamic(),new Boolean(true));
    }
}
