package org.cse.visiri.communication;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.Utilization;

import java.util.*;
import java.util.Queue;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment implements MessageListener {

    public static final int NODE_TYPE_PROCESSINGNODE = 1;
    public static final int NODE_TYPE_DISPATCHER = 2;

    public static final int EVENT_TYPE_QUERIES_CHANGED = 1;
    public static final int EVENT_TYPE_NODES_CHANGED = 2;
    public static final int EVENT_TYPE_BUFFERINGSTATE_CHANGED = 3;
    public static final int EVENT_TYPE_EVENTSUBSCIBER_CHANGED = 4;
    public static final int EVENT_TYPE_NODE_START = 5;
    public static final int EVENT_TYPE_NODE_STOP = 6;

    private final String UTILIZATION_MAP = "UTILIZATION_MAP";
    private final String NODE_QUERY_MAP = "NODE_QUERY_MAP";
    private final String ORIGINAL_TO_DEPLOYED_MAP = "ORIGINAL_TO_DEPLOYED_MAP";
    private final String SUBSCRIBER_MAP = "SUBSCRIBER_MAP";
    private final String NODE_LIST = "NODE_LIST";

    private List<String> bufferingEventList = null;
    private EnvironmentChangedCallback changedCallback = null;
    private static HazelcastInstance hzInstance = null;
    private static Environment instance = null;

    private ITopic<Object> topic;

    private Environment() {
        Config cfg = new Config();
        hzInstance = Hazelcast.newHazelcastInstance(cfg);
        bufferingEventList = new ArrayList<String>();

        topic = hzInstance.getTopic ("VISIRI");
        topic.addMessageListener(this);

    }
    public void setNodeType(int nodeType){
        hzInstance.getMap(NODE_LIST).put(getNodeId(),nodeType);
    }

    public int getNodeType(){
        return (Integer.parseInt(hzInstance.getMap(NODE_LIST).get(getNodeId()).toString()));
    }


    public void setChangedCallback(EnvironmentChangedCallback callback) {
        this.changedCallback = callback;
    }

    public EnvironmentChangedCallback getChangedCallback() {
        return changedCallback;
    }

    public void setNodeUtilization(String nodeIp, Double value) {
        hzInstance.getMap(UTILIZATION_MAP).put(nodeIp, value);
    }

    public void addQueryDistribution(QueryDistribution queryDistribution) {

        //Adding to originalQueryToDeployedQueryMap

        Map<Query, List<Query>> generatedQueries = queryDistribution.getGeneratedQueries();

        for (Query query : generatedQueries.keySet()) {
            hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP).put(query, generatedQueries.get(query));
        }

        //Adding to nodeToQueriesMap
        Map<Query, String> queryAllocation = queryDistribution.getQueryAllocation();

        for (Query query : queryAllocation.keySet()) {
            String ip = queryAllocation.get(query);
            List<Query> queryList = (List<Query>) (hzInstance.getMap(NODE_QUERY_MAP).get(ip));//.get(ip);

            if (queryList == null) {
                queryList = new ArrayList<Query>();
            }
            queryList.add(query);
            hzInstance.getMap(NODE_QUERY_MAP).put(ip, queryList);
        }
    }

    /**
     * Singleton Accessor *
     */
    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    public List<String> getNodeIdList(int nodeType) {
        Set<Member> t = hzInstance.getCluster().getMembers();
        List<String> nodeIdList = new ArrayList<String>();

        for (Member member : t) {
            String ip=member.getInetSocketAddress().getHostString();

            if(nodeType == hzInstance.getMap(NODE_LIST).get(ip))
                nodeIdList.add(ip);
        }
        return nodeIdList;
    }

    public Map<Query, List<Query>> getOriginalToDeployedQueriesMapping() {
        return hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP);
    }

    public void stop(){
        hzInstance.shutdown();
        instance=null;
    }

    public String getNodeId() {
        return hzInstance.getCluster().getLocalMember().getInetSocketAddress().getHostString();
    }

    public Map<String, List<Query>> getNodeQueryMap() {
        return hzInstance.getMap(NODE_QUERY_MAP);
    }


    public Map<String, Utilization> getNodeUtilizations() {
        return hzInstance.getMap(UTILIZATION_MAP);
    }

    public void setNodeUtilizations(Utilization utilization) {
        hzInstance.getMap(UTILIZATION_MAP).put(getNodeId(),utilization);
    }

    public List<String> getBufferingEventList() {
        return bufferingEventList;
    }

    public Map<String, List<String>> getSubscriberMapping() {
        return hzInstance.getMap(SUBSCRIBER_MAP);
    }

    public Map<String, List<String>> getEventNodeMapping() {

        Map<String,List<Query>> nodeQueryMap=hzInstance.getMap(NODE_QUERY_MAP);
        Map<String,Set<String>> eventNodeMap=new HashMap<String, Set<String>>();
        Map<String,List<String>> eventNodeMap2=new HashMap<String, List<String>>();

        //For all IPs in the node query map
        for(Object ob: getNodeIdList(NODE_TYPE_PROCESSINGNODE)){
            String ip=(String)ob;

            //For all queries of a specific ip
            for(Query query : nodeQueryMap.get(ip) ){

                //For all StreamDefinitions of a query
                for(StreamDefinition streamDefinition : query.getInputStreamDefinitionsList()) {

                    Set<String> ipList=eventNodeMap.get(streamDefinition.getStreamId());
                    if(ipList==null){
                        ipList=new HashSet<String>();
                    }
                    ipList.add(ip);
                    eventNodeMap.put(streamDefinition.getStreamId(), ipList);
                }
            }
        }

        //Converting Set to List
        for(String stream : eventNodeMap.keySet()){
            Set<String> ipSet=eventNodeMap.get(stream);
            List<String> iplist = new ArrayList<String>(ipSet);
            eventNodeMap2.put(stream,iplist);
        }

        return eventNodeMap2;
    }

    @Override
    public void onMessage(Message event) {
        System.out.println("Message Recieved "+event.getMessageObject());

        int eventType=(Integer)event.getMessageObject();

        switch (eventType){
            case Environment.EVENT_TYPE_BUFFERINGSTATE_CHANGED:
                changedCallback.bufferingStateChanged();
                break;
            case Environment.EVENT_TYPE_QUERIES_CHANGED:
                changedCallback.queriesChanged();
                break;
            case Environment.EVENT_TYPE_NODES_CHANGED:
                changedCallback.nodesChanged();
                break;
            case Environment.EVENT_TYPE_EVENTSUBSCIBER_CHANGED:
                changedCallback.eventSubscriberChanged();
                break;
            case Environment.EVENT_TYPE_NODE_START:
                changedCallback.startNode();
                break;
            case Environment.EVENT_TYPE_NODE_STOP:
                changedCallback.stopNode();
                break;
        }

    }

    public void sendEvent(int eventType){
       topic.publish(eventType);
    }



    public static void main(String args[]) {

    }
}
