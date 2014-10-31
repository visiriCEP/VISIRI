package org.cse.visiri.communication;

import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    EnvironmentChangedCallback changedCallback;

    public Environment(EnvironmentChangedCallback callback)
    {
        changedCallback = callback;
        throw new UnsupportedOperationException();
    }

    public Map<Query,List<Query>> getOriginalToDeployedQueriesMapping()
    {
        throw new UnsupportedOperationException();
    }


    String getNodeId()
    {

        throw new UnsupportedOperationException();
    }
}
