package org.cse.visiri.communication;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.Utilization;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment implements MessageListener {

    public static final int NODE_TYPE_PROCESSINGNODE = 1;
    public static final int NODE_TYPE_DISPATCHER = 2;

    public static final int EVENT_TYPE_QUERIES_CHANGED = 1;
    public static final int EVENT_TYPE_NODES_CHANGED = 2;
    public static final int EVENT_TYPE_BUFFERING_START= 3;
    public static final int EVENT_TYPE_EVENTSUBSCIBER_CHANGED = 4;
    public static final int EVENT_TYPE_NODE_START = 5;
    public static final int EVENT_TYPE_NODE_STOP = 6;
    public static final int EVENT_TYPE_ENGINE_PASS = 7;
    public static final int EVENT_TYPE_BUFFERING_STOP = 8;
    public static final int EVENT_TYPE_DISPATCHER_NOTIFICATION = 9;

    private final String REMOVING_EVENT_LIST = "REMOVING_EVENT_LIST";
    private final String BUFFERING_EVENT_LIST = "BUFFERING_EVENT_LIST";
    private final String ENABLE_DYNAMIC = "ENABLE_DYNAMIC";
    private final String ENABLE_DYNAMIC2 = "ENABLE_DYNAMIC2";
    private final String NEW_DISTRIBUTION = "NEW_DISTRIBUTION";
    private final String NEW_DISTRIBUTION_TO_DISPATCHER = "NEW_DISTRIBUTION_TO_DISPATCHER";
    private final String NODE_READY_MAP = "NODE_READY_MAP";
    private final String UTILIZATION_MAP = "UTILIZATION_MAP";
    private final String EVENT_RATE_MAP = "EVENT_RATE_MAP";
    private final String NODE_QUERY_MAP = "NODE_QUERY_MAP";
    private final String ORIGINAL_TO_DEPLOYED_MAP = "ORIGINAL_TO_DEPLOYED_MAP";
    private final String SUBSCRIBER_MAP = "SUBSCRIBER_MAP";
    private final String NODE_LIST = "NODE_LIST";
    private final String PERSISTENCE_MAP = "persistenceMap";
    private final String REVISION_MAP = "revisionMap";


    private List<String> bufferingEventList = null;
    private EnvironmentChangedCallback changedCallback = null;
    private static HazelcastInstance hzInstance = null;
    private static Environment instance = null;

    private String nodeId;


    // private static TransactionOptions options=null;
  //  private static TransactionContext transaction =null;



    private ITopic<Object> topic;


    private void deleteCheckFile(){
        try {
            File f = new File("VISIRI_check.txt");
            f.delete();
            System.out.println("Existing VISIRI_check file deleted !");
        }catch (Exception e){
            System.out.println("Check file not found !");
        }
    }
    private Environment() {

        Config cfg = new Config();
        hzInstance = Hazelcast.newHazelcastInstance(cfg);

        bufferingEventList = new ArrayList<String>();

        topic = hzInstance.getTopic ("VISIRI");
        topic.addMessageListener(this);


        hzInstance.getMap(NODE_READY_MAP).put(getNodeId(),false);

        deleteCheckFile();
        enableDynamic2();
    }

    public void setReady(){
        System.out.println("Sent ready message to environment");
        hzInstance.getMap(NODE_READY_MAP).put(getNodeId(),true);
    }

    public boolean checkReadyAllNodes(){

        List<String> nodes=instance.getNodeIdList(Environment.NODE_TYPE_PROCESSINGNODE);
        Map readyMap=hzInstance.getMap(NODE_READY_MAP);

        for(String ip :  nodes){
            if(readyMap.get(ip)==null){
                return false;
            }

            else if(!(Boolean)readyMap.get(ip)){
                return false;
            }
        }

        System.out.println("All nodes are ready");
        return true;
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
        IMap map = hzInstance.getMap(UTILIZATION_MAP);
        if(map.tryLock(nodeIp,100,TimeUnit.MILLISECONDS)) {
            try {
                hzInstance.getMap(UTILIZATION_MAP).put(nodeIp, value);
            } finally {
                try {
                    map.unlock(nodeIp);
                }catch (OperationTimeoutException e){}
            }
        }
    }


    public void setNodeEventRate( Double value) {
//        try {
//            Lock lock = hzInstance.getLock(EVENT_RATE_MAP);
//            lock.tryLock(1, TimeUnit.SECONDS);
//            try {
//                hzInstance.getMap(EVENT_RATE_MAP).put(getNodeId(), value);
//            } finally {
//                lock.unlock();
//            }
//        }catch(InterruptedException e){
//        }

        IMap map=hzInstance.getMap(EVENT_RATE_MAP);
        if(map.tryLock(getNodeId(), 100, TimeUnit.MILLISECONDS)) {
            try {
                map.put(getNodeId(), value);
            } finally {
                try {
                    map.unlock(getNodeId());
                }catch (OperationTimeoutException e){}
            }
        }

    }

    public void addQueryDistribution(QueryDistribution queryDistribution) {
       // options = new TransactionOptions().setTransactionType( TransactionOptions.TransactionType.LOCAL );
        //transaction= hzInstance.newTransactionContext(options);
        //transaction.beginTransaction();
        //Adding to originalQueryToDeployedQueryM

        Map<Query, List<Query>> generatedQueries = queryDistribution.getGeneratedQueries();
        int xx = 0;
        System.out.print("adding query map....");
        for (Query query : generatedQueries.keySet()) {
            hzInstance.getMap(ORIGINAL_TO_DEPLOYED_MAP).put(query, generatedQueries.get(query));
            if(++xx % 100 == 0)
            {
                System.out.print(xx+ " ");
            }
        }
        System.out.println(" done.");
        //Adding to nodeT7oQueriesMap
        Map<Query, String> queryAllocation = queryDistribution.getQueryAllocation();
        System.out.print("put query allocation map....");
        xx =0;

        Map<String,List<Query>> nodeQueryMap = new HashMap<String, List<Query>>(hzInstance.<String, List<Query>>getMap(NODE_QUERY_MAP));

        for (Query query : queryAllocation.keySet()) {
            String ip = queryAllocation.get(query);
            List<Query> queryList = (List<Query>) nodeQueryMap.get(ip);//.get(ip);

            if (queryList == null) {
                queryList = new ArrayList<Query>();
            }

            queryList.add(query);
            //hzInstance.getMap(NODE_QUERY_MAP).put(ip, queryList);
            nodeQueryMap.put(ip, queryList);
            if(++xx % 100 == 0)
            {
                System.out.print(xx+ " ");
            }
        }
        getNodeQueryMap().putAll(nodeQueryMap);

        System.out.println(" done.");
       // transaction.commitTransaction();
        System.out.println("-- commited---");
       // transaction = null;
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

    public Map<String, Map<String, byte[]>> getPersistenceMapping() {
        return hzInstance.getMap(PERSISTENCE_MAP);
    }

    public Map<String, List<String>> getRevisionMapping() {
        return hzInstance.getMap(REVISION_MAP);
    }

    public void stop(){
        hzInstance.shutdown();
        instance=null;
    }


    public String getNodeId() {
        if(nodeId==null) {
            nodeId=hzInstance.getCluster().getLocalMember().getInetSocketAddress().getHostString();
        }
        return nodeId;
    }

    public Map<String, List<Query>> getNodeQueryMap() {
        return hzInstance.getMap(NODE_QUERY_MAP);
    }


    public Map<String, Utilization> getNodeUtilizations() {
        return hzInstance.getMap(UTILIZATION_MAP);
    }

    public Map<String, Double> getNodeEventRates() {
        return hzInstance.getMap(EVENT_RATE_MAP);
    }

    public Double getNodeEventRatesAverage() {

        Map<String, Double> map= hzInstance.getMap(EVENT_RATE_MAP);
        double sum=0;

        List<Double> eventRateArray= (List<Double>) map.values();

        for(Double value:eventRateArray){
            sum+=value;
        }

        double averageValue=sum/eventRateArray.size();

        return averageValue;
    }

    public void setNodeUtilizations(Utilization utilization) {
        hzInstance.getMap(UTILIZATION_MAP).put(getNodeId(),utilization);
    }

    public List<String> getBufferingEventList() {
        return hzInstance.getList(BUFFERING_EVENT_LIST);
    }

    public Map<String, List<String>> getSubscriberMapping() {
        return hzInstance.getMap(SUBSCRIBER_MAP);
    }

    public Map<String, List<String>> getEventNodeMapping() {

       // transaction.beginTransaction();

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

      // transaction.commitTransaction();
        return eventNodeMap2;
    }

    @Override
    public void onMessage(Message event) {
       // System.out.println("Message Recieved "+event.getMessageObject());

        MessageObject messageObject=(MessageObject)event.getMessageObject();
        int eventType=messageObject.getEventType();

        switch (eventType){
            case Environment.EVENT_TYPE_BUFFERING_START:
                changedCallback.bufferingStart();
                break;
//            case Environment.EVENT_TYPE_BUFFERING_STOP:
//                changedCallback.bufferingStop();
//                break;
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
            case Environment.EVENT_TYPE_ENGINE_PASS:
                changedCallback.newEnginesRecieved(messageObject.getFrom());
                break;
            case Environment.EVENT_TYPE_DISPATCHER_NOTIFICATION:
                changedCallback.dynamicCompleted();
        }

    }

    public void sendEvent(int eventType){
       topic.publish(new MessageObject(eventType,getNodeId()));
    }

//    public void sendEngine(Query query,String destination){
//        topic.publish(new MessageObject(Environment.EVENT_TYPE_ENGINE_PASS,query,destination));
//    }

    public void createNewTransferable(Map<String,List<Query>> transferableEngines){

        for(String ip : transferableEngines.keySet()) {
            hzInstance.getMap(NEW_DISTRIBUTION).put(ip, transferableEngines.get(ip));
            hzInstance.getMap(NEW_DISTRIBUTION_TO_DISPATCHER).put(ip, transferableEngines.get(ip));
        }

    }

    public Map<String,List<Query>> getChangedQueries(){
        return hzInstance.getMap(NEW_DISTRIBUTION_TO_DISPATCHER);
    }

    public void clearChangedQueries(){
        hzInstance.getMap(NEW_DISTRIBUTION_TO_DISPATCHER).clear();
        hzInstance.getSet(REMOVING_EVENT_LIST).clear();
    }

    public Boolean checkDynamic(){
       String temp= (String) hzInstance.getMap(ENABLE_DYNAMIC).get(ENABLE_DYNAMIC);

        if(temp!=null && temp.equals(getNodeId()))
            return true;
        else
            return false;
    }

    public Boolean checkDynamic2(){
        Boolean temp= (Boolean) hzInstance.getMap(ENABLE_DYNAMIC2).get(ENABLE_DYNAMIC2);

        if(temp!=null && temp.equals(true))
            return true;
        else
            return false;
    }
    public void enableDynamic2(){
        hzInstance.getMap(ENABLE_DYNAMIC2).put(ENABLE_DYNAMIC2,true);
    }
    public void disableDynamic2(){
        hzInstance.getMap(ENABLE_DYNAMIC2).put(ENABLE_DYNAMIC2,false);
    }


    public void enableDynamic(){
        hzInstance.getMap(ENABLE_DYNAMIC).put(ENABLE_DYNAMIC,getNodeId());
    }

    public void shutdownHazelcast(){
        hzInstance.shutdown();
    }
    public void disableDynamic(){
        hzInstance.getMap(ENABLE_DYNAMIC).put(ENABLE_DYNAMIC,"FALSE");
    }



    public Boolean checkTransferInprogress(){
        boolean val=true;
        IMap map = hzInstance.getMap(NEW_DISTRIBUTION);
        try {
            map.lockMap(500,TimeUnit.MILLISECONDS);
            if(map.size()==0){
                val=false;
            }else{
                val= true;
            }
        } finally {
            map.unlockMap();
        }
        return val;
    }


    public void clearNewAddedQueries(){
        hzInstance.getMap(NEW_DISTRIBUTION).remove(getNodeId());
    }

    public List<Query> getAdditionalQueries(){
        return (List<Query>) hzInstance.getMap(NEW_DISTRIBUTION).get(getNodeId());
    }

    public Set<StreamDefinition> getRemovablesToDispatcher(){
        return hzInstance.getSet(REMOVING_EVENT_LIST);
    }

    public void addRemovablesToDispatcher(Set<StreamDefinition> set){
        for(StreamDefinition s : set) {
            hzInstance.getSet(REMOVING_EVENT_LIST).add(s);
        }
    }





//    public static void main(String args[]) {
//
//    }
}
