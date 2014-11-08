package org.cse.visiri.app;

import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geeth on 2014-11-08.
 */
public class EventSink {

    private static int port = 6666;
    EventServer server;

    private  List<StreamDefinition> getDefinitions()
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
        });

        System.out.println("Event sink started at port " + port);
        server.start();

    }

    public static void main(String[] arg) throws Exception {
        EventSink sink = new EventSink();
        sink.start();
    }
}