package org.cse.visiri.core;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import junit.framework.TestCase;
import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Malinda Kumarasinghe on 1/1/2015.
 */
public class AgentTest extends TestCase {

    Agent agent;
    private List<Query> queries;
    private EngineHandler engineHandler;
    private UtilizationUpdater utilizationUpdater;

    public void setUp() throws Exception {
        super.setUp();
        utilizationUpdater = new UtilizationUpdater();
        utilizationUpdater.start();
        engineHandler = new EngineHandler("Node");
        agent=new Agent(engineHandler,utilizationUpdater);

    }

    public void testRun() throws Exception {
        assertEquals(false,agent.isAlive());
        agent.start();
        assertEquals(true,agent.isAlive());

    }

}
