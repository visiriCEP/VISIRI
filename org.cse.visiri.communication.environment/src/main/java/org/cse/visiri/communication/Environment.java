package org.cse.visiri.communication;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    private static Environment environment;
    private EnvironmentChangedCallback changedCallback;
    private Map<String,Boolean> nodeUpdatedLatestChanges;
    private HazelcastInstance hzInstance;



    //private Environment(EnvironmentChangedCallback callback)
    private Environment()
    {                                                //To be modified
       // changedCallback = callback;
        Config cfg = new Config();
        hzInstance = Hazelcast.newHazelcastInstance(cfg);
    }

    public Map<Query,List<Query>> getOriginalToDeployedQueriesMapping()
    {
        throw new UnsupportedOperationException();
    }


    public String getNodeId()
    {
       return hzInstance.getCluster().getLocalMember().getInetSocketAddress().toString();
    }



    public static void main(String args[]){

      //  System.out.println("My "+Environment.getInstance().getNodeId());
    }

}
