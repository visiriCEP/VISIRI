package org.cse.visiri.communication;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 11/5/2014.
 */
public class EnvironmentTest extends junit.framework.TestCase {

    Environment environment;
    public void setUp() throws Exception {
        super.setUp();
        environment=Environment.getInstance();

    }


    public void testSetNodeUtilization() throws Exception {
        environment.setNodeUtilization("1.1.1.1",12.5);
        assertEquals(12.5, environment.getNodeUtilizations().get("1.1.1.1"));
    }
    public void testGetNodeList() throws Exception {
     //   List<String> nodes=environment.getNodeIdList();

       // environment.setNodeUtilization("1.1.1.1",12.5);
        //assertEquals(12.5, environment.getNodeUtilizations().get("1.1.1.1"));
    }

    public void testAddQueryDistribution() throws Exception {
        Query q1=new Query("qq1",new ArrayList<StreamDefinition>(),new StreamDefinition(),"id0",1);
        Query q2=new Query("qq2",new ArrayList<StreamDefinition>(),new StreamDefinition(),"id0",2);
        Query q3=new Query("qq3",new ArrayList<StreamDefinition>(),new StreamDefinition(),"id0",3);
        Query nq1=new Query("nq2",new ArrayList<StreamDefinition>(),new StreamDefinition(),"id0",2);

        List<Query> qList=new ArrayList<Query>();
        qList.add(nq1);
        qList.add(q1);

        QueryDistribution queryDistribution=new QueryDistribution();
        queryDistribution.getGeneratedQueries().put(q1,qList);
        queryDistribution.getGeneratedQueries().put(q2,qList);
        queryDistribution.getGeneratedQueries().put(q3,qList);

        queryDistribution.getQueryAllocation().put(q1,"1.1.1.1");
        queryDistribution.getQueryAllocation().put(q2,"2.2.2.2");

        environment.addQueryDistribution(queryDistribution);

        assertEquals(3,environment.getOriginalToDeployedQueriesMapping().size());
        assertEquals(nq1.getQuery(),environment.getOriginalToDeployedQueriesMapping().get(q1).get(0).getQuery());
        assertEquals(qList.size(),environment.getOriginalToDeployedQueriesMapping().get(q1).size());

        Map<String,List<Query>> temp=environment.getNodeQueryMap();
        assertEquals(q1.getQueryId(),temp.get("1.1.1.1").get(0).getQueryId());
        assertEquals(q2.getQueryId(),temp.get("2.2.2.2").get(0).getQueryId());

    }


}
