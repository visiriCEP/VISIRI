package org.cse.visiri.app.sinks;

import org.cse.visiri.app.Evaluation;
import org.cse.visiri.app.util.Writer;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Geeth on 2014-11-08.
 */
public class EventSink {

    private static int port = 6666;
    EventServer server;
    EventRateStore eventRateStore;
    Writer writer;

    private long count;

    public EventSink(){
        writer=new Writer(100);
    }

    private  List<StreamDefinition> getDefinitions()
    {
        eventRateStore=new EventRateStore();

        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

//        StreamDefinition sd =new StreamDefinition("fire",null);
//        sd.addAttribute("location", StreamDefinition.Type.STRING);
//        sd.addAttribute("temperature", StreamDefinition.Type.DOUBLE);
//        sd.addAttribute("casualties", StreamDefinition.Type.BOOLEAN);
//        defs.add(sd);
//
//        sd =new StreamDefinition("fight",null);
//        sd.addAttribute("location", StreamDefinition.Type.STRING);
//        sd.addAttribute("fighters", StreamDefinition.Type.INTEGER);
//        sd.addAttribute("deaths", StreamDefinition.Type.INTEGER);
//        sd.addAttribute("duration", StreamDefinition.Type.DOUBLE);
//
//        StreamDefinition outputStreamDefinition=new StreamDefinition();
//        outputStreamDefinition.setStreamId("filterCar");
//        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
//        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);
//        defs.add(outputStreamDefinition);
//
//        StreamDefinition outputDef=new StreamDefinition();
//        outputDef.setStreamId("StockQuote");
//        outputDef.addAttribute("Att1", StreamDefinition.Type.INTEGER);
//        outputDef.addAttribute("Att2", StreamDefinition.Type.FLOAT);
//        defs.add(outputDef);

//        StreamDefinition inputStreamDefinition1;//=new StreamDefinition();
//        inputStreamDefinition1.setStreamId("car");
//        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
//        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
//        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);
//        defs.add(inputStreamDefinition1);

//        inputStreamDefinition1=new StreamDefinition();
//        inputStreamDefinition1.setStreamId("outStock");
//        inputStreamDefinition1.addAttribute("Index", StreamDefinition.Type.INTEGER);
//        inputStreamDefinition1.addAttribute("Open", StreamDefinition.Type.FLOAT);
//        inputStreamDefinition1.addAttribute("High", StreamDefinition.Type.FLOAT);
//        inputStreamDefinition1.addAttribute("Low", StreamDefinition.Type.FLOAT);
//        inputStreamDefinition1.addAttribute("Close", StreamDefinition.Type.FLOAT);
//        inputStreamDefinition1.addAttribute("Volume", StreamDefinition.Type.INTEGER);
//        inputStreamDefinition1.addAttribute("Date", StreamDefinition.Type.STRING);
//        defs.add(inputStreamDefinition1);
//
//
//        StreamDefinition inputStreamDefinition2=new StreamDefinition();
//        inputStreamDefinition2.setStreamId("outStock2");
//        inputStreamDefinition2.addAttribute("Open", StreamDefinition.Type.FLOAT);
//        inputStreamDefinition2.addAttribute("High", StreamDefinition.Type.FLOAT);
//        defs.add(inputStreamDefinition2);

        //-- new

        Evaluation evaluation=new Evaluation();
        List<Query> debsQueryList=evaluation.getDEBSQueries();
        List<Query> stockQueryList=evaluation.getStockQueries();

        List<Query> queryList= new ArrayList<Query>();
        queryList.addAll(debsQueryList);
        queryList.addAll(stockQueryList);

        HashMap<String,StreamDefinition> outDefs = new HashMap<String, StreamDefinition>();
        for(Query q: queryList)
        {
            StreamDefinition outDef = q.getOutputStreamDefinition();
            outDefs.put(outDef.getStreamId(),outDef);
            System.out.print(outDef.getStreamId()+":::");
            for(StreamDefinition.Attribute a:outDef.getAttributeList()){
                System.out.print(a.getName()+",");
            }
            System.out.println();
        }

        defs.addAll(outDefs.values());


        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("stock");
        inputStreamDefinition1.addAttribute("Index", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Open", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("High", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Low", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Close", StreamDefinition.Type.FLOAT);
        inputStreamDefinition1.addAttribute("Volume", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("Date", StreamDefinition.Type.STRING);
        defs.add(inputStreamDefinition1);

//        StreamDefinition def1=new StreamDefinition();
//        def1.setStreamId("ABC");
//        def1.addAttribute("Att1", StreamDefinition.Type.INTEGER);
//        def1.addAttribute("Att2", StreamDefinition.Type.FLOAT);

        return defs;
    }

    public void start() throws Exception {
        EventServerConfig conf = new EventServerConfig(port);
        server = new EventServer(conf,getDefinitions(), new StreamCallback() {
            @Override
            public void receive(Event event) {

                writer.write();
                //if(count%1000==0) {
                    System.out.print(count);
                    System.out.printf(":Event received : %s ", event.getStreamId());

                    for (Object o : event.getData()) {
                        System.out.print(o.toString());
                        System.out.print(",");
                    }
                    System.out.println("}");
            //    }
                count++;
               // eventRateStore.increment();


            }
        },"EventSink");

        System.out.println("Event sink started at port " + port);
        server.start();

    }

    public static void main(String[] arg) throws Exception {
        EventSink sink = new EventSink();
        sink.start();
    }



}