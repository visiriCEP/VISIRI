package org.cse.visiri.util.costmodelcalc;

import junit.framework.TestCase;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Geeth on 2015-01-01.
 */
public class CostModelCalculatorTest extends TestCase {
    public void testCalculateCost() throws Exception {

        List<Query> queries = getQueries();

        FastSiddhiCostModelCalculator fastCal = new FastSiddhiCostModelCalculator();
        CostModelCalculator normalCal = new CostModelCalculator();

        for(Query q: queries)
        {
            double costF,costN;
            costF = fastCal.calculateCost(q);
            costN = normalCal.calculateCost(q);

            System.out.println(q.getQuery());
            System.out.println("Fast : " + costF);
            System.out.println("Normal : "+costN);
            System.out.println();

            assertEquals(costF,costN,0.01);
        }

    }


    private List<Query> getQueries()
    {
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("stock");
        inputStreamDefinition1.addAttribute("Index", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Open", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("High", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Low", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Close", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Date", StreamDefinition.Type.STRING);
        //defs.add(inputStreamDefinition1);

        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("outStock");
        outputStreamDefinition.addAttribute("Index", StreamDefinition.Type.INTEGER);
        outputStreamDefinition.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("High", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Low", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Close", StreamDefinition.Type.FLOAT);
        outputStreamDefinition.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        outputStreamDefinition.addAttribute("Date", StreamDefinition.Type.STRING);

        StreamDefinition outputStreamDefinition2=new StreamDefinition();
        outputStreamDefinition2.setStreamId("outStock2");
        outputStreamDefinition2.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinition2.addAttribute("High", StreamDefinition.Type.FLOAT);

        String queryString="from stock " +
                "select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";

        List<StreamDefinition> defs = Arrays.asList(inputStreamDefinition1);

        Query query1=new Query(queryString,defs,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);


        String queryString2="from stock " +
                "[ Open <= 50 ] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query query2=new Query(queryString2,defs,outputStreamDefinition,"2", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString3="from stock " +
                "[ Open <= 50 and Open>25] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query query3=new Query(queryString3,defs,outputStreamDefinition,"3", Configuration.ENGINE_TYPE_SIDDHI);

        String queryString4="from stock[Open > 25]#window.lengthBatch(1600 ) "+
                " select max(Open) as Open, avg(Open) as High "+
                "insert into outStock2;";
        Query query4=new Query(queryString4,defs,outputStreamDefinition2,"4", Configuration.ENGINE_TYPE_SIDDHI);


        return Arrays.asList(query1,query2,query3,query4);
    }
}
