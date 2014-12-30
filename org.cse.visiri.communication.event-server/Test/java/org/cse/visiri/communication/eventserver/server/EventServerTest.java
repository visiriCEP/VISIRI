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
        tmpQ.add("car");
        server.bufferingStart(tmpQ);

        System.out.println("timer started");
        Thread.sleep(20000);
        System.out.println("timeout");
        //tmpQ.remove("fire");
        server.bufferingStop();
    }


    private static int port = 7211;
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
        defs.add(sd);

        StreamDefinition inputStreamDefinition1=new StreamDefinition();
        inputStreamDefinition1.setStreamId("car");
        inputStreamDefinition1.addAttribute("brand", StreamDefinition.Type.STRING);
        inputStreamDefinition1.addAttribute("Id", StreamDefinition.Type.INTEGER);
        inputStreamDefinition1.addAttribute("value", StreamDefinition.Type.INTEGER);
        defs.add(inputStreamDefinition1);

        StreamDefinition def1=new StreamDefinition();
        def1.setStreamId("ABC");
        def1.addAttribute("Att1", StreamDefinition.Type.INTEGER);
        def1.addAttribute("Att2", StreamDefinition.Type.FLOAT);
        defs.add(def1);

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