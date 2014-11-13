package org.cse.visiri.communication;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

import java.net.InetAddress;
import java.util.*;

/**
 * Created by Malinda Kumarasinghe on 11/5/2014.
 */

public class EnvironmentTest extends junit.framework.TestCase {

    Environment environment;
    Query q1,q2,nq1;
    List<Query> qList;
    QueryDistribution queryDistribution;

    public void setUp() throws Exception {
        super.setUp();
        environment=Environment.getInstance();


        StreamDefinition strdef1=new StreamDefinition();
        strdef1.setStreamId("AA");
        StreamDefinition strdef2=new StreamDefinition();
        strdef2.setStreamId("BB");
        StreamDefinition strdef3=new StreamDefinition();
        strdef3.setStreamId("CC");
        StreamDefinition strdef4=new StreamDefinition();
        strdef4.setStreamId("DD");

        List<StreamDefinition> streamDefinitionList1=new ArrayList<StreamDefinition>();
        streamDefinitionList1.add(strdef1);
        streamDefinitionList1.add(strdef2);

        List<StreamDefinition> streamDefinitionList2=new ArrayList<StreamDefinition>();
        streamDefinitionList2.add(strdef3);
        streamDefinitionList2.add(strdef4);

        List<StreamDefinition> streamDefinitionList3=new ArrayList<StreamDefinition>();
        streamDefinitionList3.add(strdef3);
        streamDefinitionList3.add(strdef4);

        q1=new Query("qq1",streamDefinitionList1,new StreamDefinition(),"id0",1);//AA,BB
        q2=new Query("qq2",streamDefinitionList1,new StreamDefinition(),"id0",2);//AA,BB
        Query q3=new Query("qq3",streamDefinitionList2,new StreamDefinition(),"id0",3);//CC,DD
        Query q4=new Query("qq4",streamDefinitionList2,new StreamDefinition(),"id0",4);//CC,DD
        Query q5=new Query("qq4",streamDefinitionList3,new StreamDefinition(),"id0",4);//CC,DD
        nq1=new Query("nq2",streamDefinitionList2,new StreamDefinition(),"id0",2);//CC,DD

        qList=new ArrayList<Query>();
        qList.add(nq1);
        qList.add(q1);

        queryDistribution=new QueryDistribution();
        queryDistribution.getGeneratedQueries().put(q1,qList);
        queryDistribution.getGeneratedQueries().put(q2,qList);
        queryDistribution.getGeneratedQueries().put(q3,qList);

        //"1.1.1.1" - AA,BB
        //"2.2.2.2" - AA,BB
        //"3.3.3.3" - CC,DD
        //"4.4.4.4" - CC,DD
        //AA - 1 2
        //BB -  1 2
        //CC -  3 4 5
        //DD - 3 4 5
        queryDistribution.getQueryAllocation().put(q1,"1.1.1.1");
        queryDistribution.getQueryAllocation().put(q2,"2.2.2.2");
        queryDistribution.getQueryAllocation().put(q3,"3.3.3.3");
        queryDistribution.getQueryAllocation().put(q4,"4.4.4.4");
        queryDistribution.getQueryAllocation().put(q5,"5.5.5.5");

        environment.addQueryDistribution(queryDistribution);

    }


    public void testSetNodeUtilization() throws Exception {
        environment.setNodeUtilization("1.1.1.1",12.5);
        assertEquals(12.5, environment.getNodeUtilizations().get("1.1.1.1"));
    }
    public void testGetEventClientMapping() throws Exception {

        environment.addQueryDistribution(queryDistribution);

        Map<String, List<String>> map=environment.getEventNodeMapping();

        assertEquals("1.1.1.1", map.get("AA").toArray()[0]);
        assertEquals("2.2.2.2",map.get("AA").toArray()[1]);

        assertEquals("1.1.1.1",map.get("BB").toArray()[0]);
        assertEquals("2.2.2.2",map.get("BB").toArray()[1]);

        assertEquals("5.5.5.5",map.get("CC").toArray()[0]);
        assertEquals("3.3.3.3",map.get("CC").toArray()[1]);
        assertEquals("4.4.4.4",map.get("CC").toArray()[2]);


        assertEquals("5.5.5.5",map.get("DD").toArray()[0]);
        assertEquals("3.3.3.3",map.get("DD").toArray()[1]);
        assertEquals("4.4.4.4",map.get("DD").toArray()[2]);

//        System.out.println("AA");
//        for(String s : map.get("AA")) {
//            System.out.println("  -"+s);
//        }

//        System.out.println("BB");

//        for(String s : map.get("BB")) {
//            System.out.println("  -"+s);
//        }
//        System.out.println("CC");
//        for(String s : map.get("CC")) {
//            System.out.println("  -"+s);
//        }
//        System.out.println("DD");
//        for(String s : map.get("DD")) {
//            System.out.println("  -"+s);
//        }
    }

    public void testAddQueryDistribution() throws Exception {
        environment.addQueryDistribution(queryDistribution);
        assertEquals(3,environment.getOriginalToDeployedQueriesMapping().size());
        assertEquals(nq1.getQuery(),environment.getOriginalToDeployedQueriesMapping().get(q1).get(0).getQuery());
        assertEquals(qList.size(),environment.getOriginalToDeployedQueriesMapping().get(q1).size());

        Map<String,List<Query>> temp=environment.getNodeQueryMap();
        assertEquals(q1.getQueryId(),temp.get("1.1.1.1").get(0).getQueryId());
        assertEquals(q2.getQueryId(),temp.get("2.2.2.2").get(0).getQueryId());
    }
    public void testGetNodeType() throws Exception {
        environment.setNodeType(Environment.NODE_TYPE_DISPATCHER);
        assertEquals(Environment.NODE_TYPE_DISPATCHER,environment.getNodeType());

        environment.setNodeType(Environment.NODE_TYPE_PROCESSINGNODE);
        assertEquals(Environment.NODE_TYPE_PROCESSINGNODE,environment.getNodeType());
    }

    public void testGetNodeId() throws Exception {
        String IP= InetAddress.getLocalHost().toString().split("/")[1];
        assertEquals(IP,environment.getNodeId());
    }

    public void testOnMessage() throws Exception {
        Environment.getInstance();

            Environment.getInstance().sendEvent(Environment.EVENT_TYPE_QUERIES_CHANGED);
       Scanner sc=new Scanner(System.in);
        System.out.println("Waiting...");
        sc.next();
     //   Node node=new Node();
       // node.start();
       // String IP= InetAddress.getLocalHost().toString().split("/")[1];
        //assertEquals(IP,environment.getNodeId());
    }


    public void tearDown() throws Exception {

      //  environment.stop();
    }


}
