package org.cse.visiri.util;

import junit.framework.TestCase;

/**
 * Created by Malinda Kumarasinghe on 2/12/2015.
 */
public class EventRateStoreTest extends TestCase {
    EventRateStore eventRateStore;
    public void setUp() throws Exception {
        super.setUp();
        eventRateStore=new EventRateStore();

    }

    public void tearDown() throws Exception {

    }

    public void testIncrement() throws Exception {
        assertEquals(0.0,eventRateStore.getAverageRate());
        assertEquals(0.0,eventRateStore.getInstantRate());

    }

    public void testGetInstantRate() throws Exception {
        eventRateStore.increment();
        assertNotNull(eventRateStore.getInstantRate());

    }

    public void testGetAverageRate() throws Exception {
        eventRateStore.increment();
        assertNotNull(eventRateStore.getInstantRate());
    }


}
