package org.cse.visiri.algo.util.costmodelcalc;

import junit.framework.TestCase;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.costmodelcalc.CostModelCalculator;

import java.util.ArrayList;
import java.util.List;

public class CostModelCalculatorTest extends TestCase {

    public void testCalculateWindowCost1() throws Exception {
        // query 1
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDefinition1);
        String queryString=//"from cseEventStream[price==foo.price and foo.try>5 in foo] " +
                //"select symbol, avg(price) as avgPrice ";

                "from car [Id>=10]#window.length(10000) select brand,Id insert into filterCar;";

//        "from ABC " +
//                "[ Att1 >= 50 ] select Att1 " +
//                "insert into DER;");


//        "from  cseEventStream[ price >= 60 and symbol=='IBM']#window.time(1000) " +
//                "insert all-events into StockQuote symbol, avg(price) as avgPrice ;"



        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);

        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);

        double b=new CostModelCalculator().calculateCost(query1);
        System.out.println(b);
    }

    public void testCalculateWindowCost2() throws Exception {
        // query 1
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDefinition1);
        String queryString=//"from cseEventStream[price==foo.price and foo.try>5 in foo] " +
                //"select symbol, avg(price) as avgPrice ";

                //"from car [Id>=10]#window.time(1000) select brand,Id insert into filterCar;";
                "from StockExchangeStream[symbol == 'WSO2']#window.time( 1 year ) \n" +
                        "select max(price) as maxPrice, avg(price) as avgPrice, min(price) as minPrice\n" +
                        "insert into WSO2StockQuote for all-events ";
//        "from ABC " +
//                "[ Att1 >= 50 ] select Att1 " +
//                "insert into DER;");


//        "from  cseEventStream[ price >= 60 and symbol=='IBM']#window.time(1000) " +
//                "insert all-events into StockQuote symbol, avg(price) as avgPrice ;"



        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);

        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);

        double b=new CostModelCalculator().calculateCost(query1);
        System.out.println(b);
    }

    public void testCalculateFilteringCost1() throws Exception {
        // query 1
        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDefinition1);
        String queryString="from cseEventStream[price==foo.price and foo.try>5 in foo] " +
                "select symbol, avg(price) as avgPrice ";

                //"from car [Id>=10]#window.time(1000) select brand,Id insert into filterCar;";
//                "from StockExchangeStream[symbol == 'WSO2']#window.time( 1 year ) \n" +
//                        "select max(price) as maxPrice, avg(price) as avgPrice, min(price) as minPrice\n" +
//                        "insert into WSO2StockQuote for all-events ";
//        "from ABC " +
//                "[ Att1 >= 50 ] select Att1 " +
//                "insert into DER;");


//        "from  cseEventStream[ price >= 60 and symbol=='IBM']#window.time(1000) " +
//                "insert all-events into StockQuote symbol, avg(price) as avgPrice ;"



        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);

        Query query1=new Query(queryString,inputStreamDefinitionList,outputStreamDefinition,"1", Configuration.ENGINE_TYPE_SIDDHI);

        double b=new CostModelCalculator().calculateCost(query1);
        System.out.println(b);
    }
}