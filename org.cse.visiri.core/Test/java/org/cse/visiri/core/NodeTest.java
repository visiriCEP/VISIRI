package org.cse.visiri.core;

import junit.framework.TestCase;
import org.cse.visiri.communication.Environment;

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

    public void testStart() throws Exception {

    }

    public void testStop() throws Exception {

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
    }

    public void testNodesChanged() throws Exception {

    }

    public void testBufferingStateChanged() throws Exception {

    }

    public void testEventSubscriberChanged() throws Exception {

    }
}
