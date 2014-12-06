package org.cse.visiri.app;

import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visiri on 12/6/14.
 */
public class Evaluation {

    public static void main(String[] args){

        StreamDefinition inputStreamDef=new StreamDefinition();
        inputStreamDef.setStreamId("players");
        List<StreamDefinition.Attribute> attributeList=new ArrayList<StreamDefinition.Attribute>();
       // StreamDefinition.Attribute attribute=new StreamDefinition.Attribute();



        inputStreamDef.addAttribute("sid", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("x", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("y", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("z", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("v", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("a", StreamDefinition.Type.INTEGER);

        StreamDefinition outputStreamdef=inputStreamDef;
        List<StreamDefinition> streamDefinitionList=new ArrayList<StreamDefinition>();
        streamDefinitionList.add(inputStreamDef);
       // Query query1=new Query()


    }

    private ArrayList<Query> getStockQueries()
    {
        ArrayList<Query> queries = new ArrayList<Query>();

        StreamDefinition inputDef = new StreamDefinition();
        inputDef.setStreamId("stockStream");
        inputDef.addAttribute("Open", StreamDefinition.Type.DOUBLE);
        inputDef.addAttribute("High", StreamDefinition.Type.DOUBLE);
        inputDef.addAttribute("Low", StreamDefinition.Type.DOUBLE);
        inputDef.addAttribute("Close", StreamDefinition.Type.DOUBLE);
        inputDef.addAttribute("Volume", StreamDefinition.Type.DOUBLE);
        inputDef.addAttribute("Date", StreamDefinition.Type.STRING);

        List<StreamDefinition> inputDefs = new ArrayList<StreamDefinition>();
        inputDefs.add(inputDef);

        StreamDefinition outStocks = new StreamDefinition();
        outStocks.setStreamId("stocks");
        outStocks.addAttribute("Date", StreamDefinition.Type.STRING);
        outStocks.addAttribute("Volume", StreamDefinition.Type.DOUBLE);

        StreamDefinition outVolumes = new StreamDefinition(outStocks);
        outVolumes.setStreamId("volumes");

        StreamDefinition outStockValue = new StreamDefinition(outStocks);
        outStockValue.setStreamId("volumes");

        StreamDefinition outAverages = new StreamDefinition();
        outAverages.setStreamId("averages");
        outStocks.addAttribute("avgs", StreamDefinition.Type.DOUBLE);

        String queryStr = "from stockStream[Open < Close]\n" +
                "select Date,Volume\n" +
                "insert into stocks";
        Query query=new Query(queryStr,inputDefs,
                outStocks,"gt1", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);


        queryStr = "from stockStream[High != Open]\n" +
                "select Date,Volume\n" +
                "insert into stocks";

        query=new Query(queryStr,inputDefs,
                outStocks,"gt2", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Close < Open]\n" +
                "select Date,Volume\n" +
                "insert into stocks";
        query=new Query(queryStr,inputDefs,
                outStocks,"gt3", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[High != Close]\n" +
                "select Date,Volume\n" +
                "insert into stocks";
        query=new Query(queryStr,inputDefs,
                outStocks,"gt4", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        //--
        queryStr = "from stockStream[Volume < 500000]\n" +
                "select Date,Volume\n" +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt5", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Volume >= 500000 and Volume < 5000000]\n" +
                "select Date,Volume\n" +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt6", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Volume >= 5000000 and Volume < 10000000]\n" +
                "select Date,Volume\n" +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt7", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Volume >= 10000000]\n" +
                "select Date,Volume\n" +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt8", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        //----
        queryStr = "from stockStream[Close < 20]\n" +
                "select Date,Volume,Close\n" +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gt9", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Close >= 20 and Close < 50]\n" +
                "select Date,Volume,Close\n" +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gs1", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream[Close >= 50]\n" +
                "select Date,Volume,Close\n" +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gs2", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        //---------
        queryStr = "from stockStream#window.lengthBatch(20) \n" +
                "select avg(Close) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs3", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream#window.lengthBatch(10) \n" +
                "select avg(Close) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs4", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream#window.lengthBatch(50) \n" +
                "select avg(Close) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs5", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream#window.lengthBatch(20) \n" +
                "select avg(Open) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs6", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream#window.lengthBatch(10) \n" +
                "select avg(Open) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs7", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        queryStr = "from stockStream#window.lengthBatch(50) \n" +
                "select avg(Open) as avgs\n" +
                "insert into averages  for expired-events";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs8", Configuration.ENGINE_TYPE_SIDDHI);
        queries.add(query);

        return queries;
    }
}
