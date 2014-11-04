package org.cse.visiri.communication;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import org.cse.visiri.util.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    private static Environment environment;
    private EnvironmentChangedCallback changedCallback;
    private Map<String,Boolean> nodeUpdatedLatestChanges;
    private HazelcastInstance hzInstance;



    //private Environment(EnvironmentChangedCallback callback)
    private Environment() {                                                //To be modified
        // changedCallback = callback;
        Config cfg = new Config();
        hzInstance = Hazelcast.newHazelcastInstance(cfg);
    }
    
 

    public void setChangedCallback(EnvironmentChangedCallback callback)
    {
        changedCallback = callback;
    }

    private static Environment instance = null;


    /** Singleton Accessor **/
    public static Environment getInstance()
    {
        if(instance == null)
        {
            instance = new Environment();
        }
        return instance;
    }

    public List<String> getNodeIdList() {
        Set<Member> t = hzInstance.getCluster().getMembers();
        List<String> nodeIdList = new ArrayList<String>();

        for (Member member : t) {
            nodeIdList.add(member.getSocketAddress().getHostString());
        }
        return nodeIdList;
    }

    public Map<Query,List<Query>> getOriginalToDeployedQueriesMapping()
    {
        throw new UnsupportedOperationException();
    }


    public String getNodeId()
    {
        return hzInstance.getCluster().getLocalMember().getInetSocketAddress().toString();
    }
    public Map<Query,List<Query>> getGeneratedQueryMapping()
    {
    
        throw new UnsupportedOperationException();
    }



    public static void main(String args[]) {

        //  System.out.println("My "+Environment.getInstance().getNodeId());
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
