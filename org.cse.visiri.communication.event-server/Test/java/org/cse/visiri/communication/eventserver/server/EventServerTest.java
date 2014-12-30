package org.cse.visiri.communication.eventserver.server;

import junit.framework.TestCase;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventServerTest extends TestCase {

    public void testBufferStateChanged() throws Exception {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                // code goes here.
                try {
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        t1.start();
        Thread.sleep(1000);

        List<String> tmpQ=new LinkedList<String>();
        tmpQ.add("fire");
        server.bufferingStart(tmpQ);

        System.out.println("timer started");
        Thread.sleep(10000);
        System.out.println("timeout");
        //tmpQ.remove("fire");
        server.bufferingStop();
    }


    private static int port = 6666;
    EventServer server;

    private List<StreamDefinition> getDefinitions()
    {
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

        StreamDefinition sd =new StreamDefinition("fire",null);
        sd.addAttribute("location", StreamDefinition.Type.STRING);
        sd.addAttribute("temperature", StreamDefinition.Type.DOUBLE);
        sd.addAttribute("casualties", StreamDefinition.Type.BOOLEAN);
        defs.add(sd);

        sd =new StreamDefinition("fight",null);
        sd.addAttribute("location", StreamDefinition.Type.STRING);
        sd.addAttribute("fighters", StreamDefinition.Type.INTEGER);
        sd.addAttribute("deaths", StreamDefinition.Type.INTEGER);
        sd.addAttribute("duration", StreamDefinition.Type.DOUBLE);

        StreamDefinition outputStreamDefinition=new StreamDefinition();
        outputStreamDefinition.setStreamId("filterCar");
        outputStreamDefinition.addAttribute("brand", StreamDefinition.Type.STRING);
        outputStreamDefinition.addAttribute("Id", StreamDefinition.Type.INTEGER);
        defs.add(outputStreamDefinition);

        StreamDefinition outputDef=new StreamDefinition();
        outputDef.setStreamId("StockQuote");
        outputDef.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        outputDef.addAttribute("Att2", StreamDefinition.Type.FLOAT);
        defs.add(outputDef);

        return defs;
    }

    public void start() throws Exception {
        EventServerConfig conf = new EventServerConfig(port);
        server = new EventServer(conf,getDefinitions(), new StreamCallback() {
            @Override
            public void receive(Event event) {
                System.out.printf("Event received : %s {", event.getStreamId());
                for(Object o : event.getData())
                {
                    System.out.print(o.toString());
                    System.out.print(",");
                }
                System.out.println("}");

            }
        },"EventServer");
        System.out.println("Event sink started at port " + port);
        server.start();

    }

}