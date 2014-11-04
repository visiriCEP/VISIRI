package org.cse.visiri.communication;

import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    EnvironmentChangedCallback changedCallback;

    public Map<Query,List<Query>> getOriginalToDeployedQueriesMapping()
    {
        throw new UnsupportedOperationException();
    }

    public void setChangedCallback(EnvironmentChangedCallback callback)
    {
        changedCallback = callback;
    }

    private static Environment instance = null;

    private Environment()
    {

    }

    /** Singleton Accessor **/
    public static Environment getInstance()
    {
        if(instance == null)
        {
            instance = new Environment();
        }
        return instance;
    }

    String getNodeId()
    {
        throw new UnsupportedOperationException();
    }

    public List<String> getNodeIdList()
    {
        throw new UnsupportedOperationException();
    }

    public List<String> getDispatcherIdList()
    {
        throw new UnsupportedOperationException();
    }

    public Map<Query,List<Query>> getGeneratedQueryMapping()
    {
        throw new UnsupportedOperationException();
    }

    public Map<String,List<Query>> getNodeQueryTable()
    {
        throw new UnsupportedOperationException();
    }

    public Map<String,Double> getNodeUtilizations()
    {
        throw new UnsupportedOperationException();
    }

    public List<String> getBufferingEventList()
    {
        throw new UnsupportedOperationException();
    }

    public Map<String,List<String>> getSubscriberMapping()
    {
        throw new UnsupportedOperationException();
    }

}
