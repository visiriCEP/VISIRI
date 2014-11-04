package org.cse.visiri.communication;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    private final String UTILIZATION_MAP="UTILIZATION_MAP";
    private final String NODE_QUERY_MAP="NODE_QUERY_MAP";
    private final String ORIGINAL_TO_DEPLOYED_MAP="ORIGINAL_TO_DEPLOYED_MAP";
    private final String SUBSCRIBER_MAP="SUBSCRIBER_MAP";

    private List<String> bufferingEventList;
    private EnvironmentChangedCallback changedCallback;

   // private Map<String,Boolean> nodeUpdatedLatestChanges;
   // private Map<Query,List<Query>> originalQueryToDeployedQueryMap;
   // private Map<String,List<Query>> nodeToQueriesMap;
    //private Map<String, Double> utilizationMap;
    //private Map<String,List<String>> subscriberMapping;


    private HazelcastInstance hzInstance = null;
    private static Environment instance = null;


    private Environment() {
        Config cfg = new Config();
        hzInstance = Hazelcast.newHazelcastInstance(cfg);
        bufferingEventList=new ArrayList<String>();

//        utilizationMap=hzInstance.getMap(UTILIZATION_MAP);
//        nodeToQueriesMap=hzInstance.getMap(NODE_QUERY_MAP);
//        originalQueryToDeployedQueryMap=hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP);
//        subscriberMapping=hzInstance.getMap(SUBSCRIBER_MAP);
    }
    
 

    public void setChangedCallback(EnvironmentChangedCallback callback)
    {
        this.changedCallback = callback;
    }

    public EnvironmentChangedCallback getChangedCallback(){
        return changedCallback;
    }


    public void setNodeUtilization(String nodeIp,Double value){
        hzInstance.getMap(UTILIZATION_MAP).put(nodeIp, value);
    }

    public void addQueryDistribution(QueryDistribution queryDistribution){

       //Adding to originalQueryToDeployedQueryMap

        Map<Query,List<Query>> generatedQueries=queryDistribution.getGeneratedQueries();

        for(Query query : generatedQueries.keySet()){
            hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP).put(query,generatedQueries.get(query));
        }

        //Adding to nodeToQueriesMap
        Map<Query,String> queryAllocation=queryDistribution.getQueryAllocation();

        for(Query query : queryAllocation.keySet()){
            String ip=queryAllocation.get(query);
            List<Query> queryList= (List<Query>)(hzInstance.getMap(NODE_QUERY_MAP).get(ip));//.get(ip);

            if(queryList==null){
                queryList=new ArrayList<Query>();
            }
            queryList.add(query);
            hzInstance.getMap(NODE_QUERY_MAP).put(ip, queryList);
        }
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
        return hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP);
    }


    public String getNodeId()
    {
        return hzInstance.getCluster().getLocalMember().getInetSocketAddress().toString();
    }

    public Map<String,List<Query>> getNodeQueryMap()
    {
        return hzInstance.getMap(NODE_QUERY_MAP);
    }


    public Map<String,Double> getNodeUtilizations()
    {
        return hzInstance.getMap(UTILIZATION_MAP);
    }

    public List<String> getBufferingEventList()
    {
        return bufferingEventList;
    }

    public Map<String,List<String>> getSubscriberMapping()
    {
        return hzInstance.getMap(SUBSCRIBER_MAP);
    }


    public static void main(String args[]) {
        //Environment.getInstance().setNodeUtilization("1231",12.5);
        //System.out.println("My "+Environment.getInstance().getNodeUtilizations().get("1231"));
    }

}
