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


    public List<Query> getDEBSQueries(){
        List<Query> queryList=new ArrayList<Query>();

        StreamDefinition inputStreamDef=new StreamDefinition();
        inputStreamDef.setStreamId("players");
        List<StreamDefinition.Attribute> attributeList=new ArrayList<StreamDefinition.Attribute>();

        inputStreamDef.addAttribute("sid", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("ts", StreamDefinition.Type.STRING);
        inputStreamDef.addAttribute("x", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("y", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("z", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("v", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("a", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("vx", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("vy", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("vz", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("ax", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("ay", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("az", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDef);

        StreamDefinition outputStreamdef1=new StreamDefinition();
        outputStreamdef1.setStreamId("stopplayer");
        outputStreamdef1.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q1="from players[v<=1] " +
                "select sid,x,y,z,v,a " +
                "insert into stopplayer";
        Query query1=new Query(q1,inputStreamDefinitionList,outputStreamdef1,"1", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query1);

        StreamDefinition outputStreamdef2=new StreamDefinition();
        outputStreamdef2.setStreamId("trotplayer");
        outputStreamdef2.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q2="from players[v>1 and v<=11] " +
                "select sid,x,y,z,v,a " +
                "insert into trotplayer";

        Query query2=new Query(q2,inputStreamDefinitionList,outputStreamdef2,"2", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query2);

        StreamDefinition outputStreamdef3=new StreamDefinition();
        outputStreamdef3.setStreamId("lowplayer");
        outputStreamdef3.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q3="from players[v>11 and v<=14] " +
                "select sid,x,y,z,v,a " +
                "insert into lowplayer";

        Query query3=new Query(q3,inputStreamDefinitionList,outputStreamdef3,"3", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query3);

        StreamDefinition outputStreamdef4=new StreamDefinition();
        outputStreamdef4.setStreamId("mediumplayer");
        outputStreamdef4.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q4="from players[v>14 and v<=17] " +
                "select sid,x,y,z,v,a " +
                "insert into mediumplayer";

        Query query4=new Query(q4,inputStreamDefinitionList,outputStreamdef4,"4", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query4);


        StreamDefinition outputStreamdef5=new StreamDefinition();
        outputStreamdef5.setStreamId("highplayer");
        outputStreamdef5.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q5="from players[v>17 and v<=24] " +
                "select sid,x,y,z,v,a " +
                "insert into highplayer";

        Query query5=new Query(q5,inputStreamDefinitionList,outputStreamdef5,"5", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query5);

        StreamDefinition outputStreamdef6=new StreamDefinition();
        outputStreamdef6.setStreamId("sprintplayer");
        outputStreamdef6.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q6="from players[v>24] " +
                "select sid,x,y,z,v,a " +
                "insert into sprintplayer";

        Query query6=new Query(q6,inputStreamDefinitionList,outputStreamdef6,"6", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query6);

        StreamDefinition outputStreamdefAvgSpeedPlayer=new StreamDefinition();
        outputStreamdefAvgSpeedPlayer.setStreamId("avgspeedplayers");
        outputStreamdefAvgSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefAvgSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.DOUBLE);

        String q15="from players#window.length(50) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query15=new Query(q15,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"15", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query15);

        String q7="from players#window.length(100) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query7=new Query(q7,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"7", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query7);


        String q8="from players#window.length(200) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query8=new Query(q8,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"8", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query8);

        StreamDefinition outputStreamdefHighSpeedPlayer=new StreamDefinition();
        outputStreamdefHighSpeedPlayer.setStreamId("highspeedplayers");
        outputStreamdefHighSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefHighSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.DOUBLE);

        String q9="from players#window.length(50) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query9=new Query(q9,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"9", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query9);


        String q10="from players#window.length(100) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query10=new Query(q10,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"10", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query10);


        String q11="from players#window.length(200) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query11=new Query(q11,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"11", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query11);

        //////////////////////////////////////////////////////////// 11 end

        String q12="from players#window.time(1sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query12=new Query(q12,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"12", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query12);

        String q13="from players#window.time(5sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query13=new Query(q13,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"13", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query13);

        String q14="from players#window.time(10sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query14=new Query(q14,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"14", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queryList.add(query14);

        return queryList;

    }

    public List<Query> getStockQueries()
    {
        ArrayList<Query> queries = new ArrayList<Query>();

        StreamDefinition inputDef = new StreamDefinition();


        inputDef.setStreamId("stock");
        inputDef.addAttribute("Index", StreamDefinition.Type.INTEGER);
        inputDef.addAttribute("Open", StreamDefinition.Type.FLOAT);
        inputDef.addAttribute("High", StreamDefinition.Type.FLOAT);
        inputDef.addAttribute("Low", StreamDefinition.Type.FLOAT);
        inputDef.addAttribute("Close", StreamDefinition.Type.FLOAT);
        inputDef.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        inputDef.addAttribute("Date", StreamDefinition.Type.STRING);

        List<StreamDefinition> inputDefs = new ArrayList<StreamDefinition>();
        inputDefs.add(inputDef);

        StreamDefinition outStocks = new StreamDefinition();
        outStocks.setStreamId("stocks");
        outStocks.addAttribute("Date", StreamDefinition.Type.STRING);
        outStocks.addAttribute("Volume", StreamDefinition.Type.INTEGER);

        StreamDefinition outVolumes = new StreamDefinition(outStocks);
        outVolumes.setStreamId("volumes");

        StreamDefinition outStockValue = new StreamDefinition(outStocks);
        outStockValue.setStreamId("stockValue");
        outStockValue.addAttribute("Close", StreamDefinition.Type.FLOAT);

        StreamDefinition outAverages = new StreamDefinition();
        outAverages.setStreamId("averages");
        outAverages.addAttribute("avgs", StreamDefinition.Type.FLOAT);

        String queryStr = "from stock[Open < Close] " +
                "select Date,Volume " +
                "insert into stocks";
        Query query=new Query(queryStr,inputDefs,
                outStocks,"gt1", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);


        queryStr = "from stock[High != Open] " +
                "select Date,Volume " +
                "insert into stocks";

        query=new Query(queryStr,inputDefs,
                outStocks,"gt2", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Close < Open] " +
                "select Date,Volume " +
                "insert into stocks";
        query=new Query(queryStr,inputDefs,
                outStocks,"gt3", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[High != Close] " +
                "select Date,Volume " +
                "insert into stocks";
        query=new Query(queryStr,inputDefs,
                outStocks,"gt4", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        //--
        queryStr = "from stock[Volume < 500000] " +
                "select Date,Volume " +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt5", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Volume >= 500000 and Volume < 5000000] " +
                "select Date,Volume " +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt6", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Volume >= 5000000 and Volume < 10000000] " +
                "select Date,Volume " +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt7", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Volume >= 10000000] " +
                "select Date,Volume " +
                "insert into volumes";
        query=new Query(queryStr,inputDefs,
                outVolumes,"gt8", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        //----
        queryStr = "from stock[Close < 20] " +
                "select Date,Volume,Close " +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gt9", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Close >= 20 and Close < 50] " +
                "select Date,Volume,Close " +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gs1", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock[Close >= 50] " +
                "select Date,Volume,Close " +
                "insert into stockValue";
        query=new Query(queryStr,inputDefs,
                outStockValue,"gs2", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        //---------
        queryStr = "from stock#window.lengthBatch(20) " +
                "select max(Close) as avgs " +
                "insert into averages;";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs3", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock#window.lengthBatch(10) " +
                "select min(Close) as avgs " +
                "insert into averages ";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs4", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock#window.lengthBatch(50) " +
                "select max(Close) as avgs " +
                "insert into averages   ";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs5", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock#window.lengthBatch(20) " +
                "select max(Open) as avgs " +
                "insert into averages ";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs6", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock#window.lengthBatch(10)  " +
                "select max(Open) as avgs " +
                "insert into averages  ";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs7", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        queryStr = "from stock#window.lengthBatch(50) " +
                "select min(Open) as avgs " +
                "insert into averages  ";
        query=new Query(queryStr,inputDefs,
                outAverages,"gs8", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(query);

        /////////////////////////////////////

        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

        StreamDefinition inputStreamDefinitionStock=new StreamDefinition();
        inputStreamDefinitionStock.setStreamId("stock");
        inputStreamDefinitionStock.addAttribute("Index", StreamDefinition.Type.INTEGER);
        inputStreamDefinitionStock.addAttribute("Open", StreamDefinition.Type.FLOAT);
        inputStreamDefinitionStock.addAttribute("High", StreamDefinition.Type.FLOAT);
        inputStreamDefinitionStock.addAttribute("Low", StreamDefinition.Type.FLOAT);
        inputStreamDefinitionStock.addAttribute("Close", StreamDefinition.Type.FLOAT);
        inputStreamDefinitionStock.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        inputStreamDefinitionStock.addAttribute("Date", StreamDefinition.Type.STRING);
        defs.add(inputStreamDefinitionStock);

        StreamDefinition outputStreamDefinitionOutStock=new StreamDefinition();
        outputStreamDefinitionOutStock.setStreamId("outStock");
        outputStreamDefinitionOutStock.addAttribute("Index", StreamDefinition.Type.INTEGER);
        outputStreamDefinitionOutStock.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinitionOutStock.addAttribute("High", StreamDefinition.Type.FLOAT);
        outputStreamDefinitionOutStock.addAttribute("Low", StreamDefinition.Type.FLOAT);
        outputStreamDefinitionOutStock.addAttribute("Close", StreamDefinition.Type.FLOAT);
        outputStreamDefinitionOutStock.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        outputStreamDefinitionOutStock.addAttribute("Date", StreamDefinition.Type.STRING);

        StreamDefinition outputStreamDefinitionOutStock2=new StreamDefinition();
        outputStreamDefinitionOutStock2.setStreamId("outStock2");
        outputStreamDefinitionOutStock2.addAttribute("Open", StreamDefinition.Type.FLOAT);
        outputStreamDefinitionOutStock2.addAttribute("High", StreamDefinition.Type.FLOAT);



        String qa1="from stock " +
                "select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";

        Query querya1=new Query(qa1,defs,outputStreamDefinitionOutStock,"a1", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya1);

        String qa2="from stock " +
                "[ Open <= 50 ] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query querya2=new Query(qa2,defs,outputStreamDefinitionOutStock,"a2", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya2);

        String qa3="from stock " +
                "[ Open <= 50 and Open>25] select Index,Open,High,Low,Close,Volume,Date " +
                "insert into outStock;";
        Query querya3=new Query(qa3,defs,outputStreamDefinitionOutStock,"a3", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya3);

        String qa4="from stock[Open > 25]#window.timeBatch( 10 seconds ) "+
                "select max(Open) as Open, max(Close) as High "+
                "insert into outStock2;";
        Query querya4=new Query(qa4,defs,outputStreamDefinitionOutStock2,"a4", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya4);

        String qa5="from stock[Open > 25]#window.timeBatch( 1 hour ) "+
                "select max(Open) as Open, max(Open) as High "+
                " insert into outStock2;";
        Query querya5=new Query(qa5,defs,outputStreamDefinitionOutStock2,"a5", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya5);

        String qa6="from stock[Open > 250]#window.length(1000) "+
                "select max(Open) as Open, max(High) as High "+
                "insert into outStock2;";
        Query querya6=new Query(qa6,defs,outputStreamDefinitionOutStock2,"a6", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya6);

        String qa7=" from a1 = stock[Open >25] -> a2 = stock[High<50] " +
                "select a1.Open as Open, a2.High as High " +
                "insert into outStock2";
        Query querya7=new Query(qa7,defs,outputStreamDefinitionOutStock2,"a7", Configuration.ENGINE_TYPE_SIDDHI,1.0);
        queries.add(querya7);
        ///////////////////////////////////////////

        return queries;
    }
}
