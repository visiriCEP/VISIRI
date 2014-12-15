package org.cse.visiri.engine;

import com.hazelcast.core.Hazelcast;
import junit.framework.TestCase;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.persistence.PersistenceStore;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

public class SiddhiDistributedPersistenceHandlerTest extends TestCase {

    public void testMainPersistentStore() throws Exception{
        int count;
        long lastValue;
        long firstValue;
        final boolean eventArrived;

        PersistenceStore persistenceStore = new SiddhiDistributedPersistenceHandler();
        String revision;

        String streamDefinition = "define stream cseStream ( symbol string, price float, volume int )";
        String query = "from cseStream[price>10]#window.length(10) " +
                "select symbol, price, sum(volume) as totalVol " +
                "insert into outStream ";
        QueryCallback callback = new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                //lastValue = (Long) inEvents[0].getData(2);
                //count++;
                //eventArrived = true;
            }
        };


        SiddhiConfiguration configuration = new SiddhiConfiguration();
        configuration.setQueryPlanIdentifier("Test");
        configuration.setAsyncProcessing(false);
        SiddhiManager siddhiManager = new SiddhiManager(configuration);
        siddhiManager.setPersistStore(persistenceStore);
        InputHandler inputHandler = siddhiManager.defineStream(streamDefinition);

        /////////////////////////////////////////////////////////////////////////////////////

        String queryReference1 = siddhiManager.addQuery(query);
        siddhiManager.addCallback(queryReference1, callback);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //persisting
        Thread.sleep(1500);
        System.out.println("-----------------------");
        revision = siddhiManager.persist();

        System.out.println("revision id:::::::::::::"+revision);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //restarting Siddhi
        Thread.sleep(500);
        System.out.println("-------------------------");
        siddhiManager.shutdown();

        ////////////////////////////////////////////////////////////////////////////////////
        configuration = new SiddhiConfiguration();
        configuration.setQueryPlanIdentifier("Test");
        siddhiManager = new SiddhiManager(configuration);
        siddhiManager.setPersistStore(persistenceStore);

        inputHandler = siddhiManager.defineStream(streamDefinition);
        String queryReference2 = siddhiManager.addQuery(query);
        siddhiManager.addCallback(queryReference2, callback);

        //loading
        siddhiManager.restoreLastRevision();

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        Thread.sleep(50000);
        siddhiManager.shutdown();

        //Because of the use of in memory persistence store
        Hazelcast.shutdownAll();
    }

    public void testSubPersistentStore() throws Exception{
        int count;
        long lastValue;
        long firstValue;
        final boolean eventArrived;

        PersistenceStore persistenceStore = new SiddhiDistributedPersistenceHandler();
        String revision;

        String streamDefinition = "define stream cseStream ( symbol string, price float, volume int )";
        String query = "from cseStream[price>10]#window.length(10) " +
                "select symbol, price, sum(volume) as totalVol " +
                "insert into outStream ";
        QueryCallback callback = new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                //Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                //lastValue = (Long) inEvents[0].getData(2);
                //count++;
                //eventArrived = true;
            }
        };


        SiddhiConfiguration configuration = new SiddhiConfiguration();
        configuration.setQueryPlanIdentifier("Test");
        configuration.setAsyncProcessing(false);
        SiddhiManager siddhiManager;// = new SiddhiManager(configuration);
        //siddhiManager.setPersistStore(persistenceStore);
        InputHandler inputHandler; //= siddhiManager.defineStream(streamDefinition);

        /*////////////////////////////////////////////////////////////////////////////////////

        String queryReference1 = siddhiManager.addQuery(query);
        siddhiManager.addCallback(queryReference1, callback);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //persisting
        Thread.sleep(1500);
        System.out.println("-----------------------");
        revision = siddhiManager.persist();

        System.out.println("revision id:::::::::::::"+revision);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //restarting Siddhi
        Thread.sleep(500);
        System.out.println("-------------------------");
        siddhiManager.shutdown();

        /*///////////////////////////////////////////////////////////////////////////////////
        configuration = new SiddhiConfiguration();
        configuration.setQueryPlanIdentifier("Test");
        siddhiManager = new SiddhiManager(configuration);
        siddhiManager.setPersistStore(persistenceStore);

        inputHandler = siddhiManager.defineStream(streamDefinition);
        String queryReference2 = siddhiManager.addQuery(query);
        siddhiManager.addCallback(queryReference2, callback);

        //loading
        siddhiManager.restoreLastRevision();

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        Thread.sleep(5000);
        siddhiManager.shutdown();

        //Because of the use of in memory persistence store
        Hazelcast.shutdownAll();
    }

}