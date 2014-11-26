package org.cse.visiri.algo;

import junit.framework.TestCase;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RoundRobinDistributionAlgoTest extends TestCase {

    public void testGetQueryDistribution() throws Exception {
        List<Query> queryList=new ArrayList<Query>();
        StreamDefinition def1=new StreamDefinition();
        def1.setStreamId("ABC");
        def1.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        def1.addAttribute("Att2", StreamDefinition.Type.FLOAT);

        List<StreamDefinition> inputStreamDefinitionList2=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList2.add(def1);
        //String queryString2="from  ABC [ Att1 >= 50 ] select Att1, Att2 insert into StockQuote;";
        String queryString2="from  ABC select Att1, Att2 insert into StockQuote;";
        StreamDefinition outputDef=new StreamDefinition();
        outputDef.setStreamId("StockQuote");
        outputDef.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        outputDef.addAttribute("Att2", StreamDefinition.Type.FLOAT);

        Query query2=new Query(queryString2,inputStreamDefinitionList2,outputDef,"2", Configuration.ENGINE_TYPE_SIDDHI);

        queryList.add(query2);

        RoundRobinDistributionAlgo rrdAlgo=new RoundRobinDistributionAlgo();
        QueryDistribution qd=rrdAlgo.getQueryDistribution(queryList);
        Iterator i=qd.getQueryAllocation().entrySet().iterator();
        System.out.println("distributed");
        while(i.hasNext()){
            Map.Entry pairs = (Map.Entry)i.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
}