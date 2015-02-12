package org.cse.visiri.app.sinks;

import org.cse.visiri.app.Evaluation;
import org.cse.visiri.app.util.Writer;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.StreamDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Geeth on 2014-11-08.
 */
public class FireSink {

    private static int port = 6666;
    EventServer server;
    EventRateStore eventRateStore;
    Writer writer;

    private long count;

    public FireSink(){
        writer=new Writer(100);
    }

    private  List<StreamDefinition> getDefinitions()
    {
        eventRateStore=new EventRateStore();

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
        List<StreamDefinition> defs= Arrays.asList( evaluation.getFireOutputDefinition());
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
        FireSink sink = new FireSink();
        sink.start();
    }



}