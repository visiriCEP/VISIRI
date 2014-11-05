package org.cse.visiri.core;

import org.cse.visiri.engine.EngineHandler;
import org.cse.visiri.util.Query;

import java.util.List;

/**
 * Created by Geeth on 2014-11-05.
 */
public abstract class Node {

    public static final int NODE_TYPE_PROCESSINGNODE = 1;
    public static final int NODE_TYPE_DISPATCHER = 2;


    private List<Query> queries;
    private EngineHandler engineHandler;

}
