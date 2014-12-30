package org.cse.visiri.app.sources;

import org.cse.visiri.app.util.CsvEventReader;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geeth on 2014-12-07.
 */
public class FootballSource {

    //events per second
    public final int frequency = 50000;
    EventRateStore eventRateStore;
    public static void main(String[] arg) throws Exception {
        FootballSource sink = new FootballSource();
        sink.start();
    }

    private List<StreamDefinition> getDefinitions()
    {
        eventRateStore=EventRateStore.getInstance();
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

        StreamDefinition inputStreamDef=new StreamDefinition();
        inputStreamDef.setStreamId("players");

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

        defs.add(inputStreamDef);
        return defs;
    }

    public void start()
    {

        try {
            System.out.println(System.getProperty("user.dir"));
            EventClient client = new EventClient("localhost:7211",getDefinitions());




            CsvEventReader reader = new CsvEventReader("../full-game"
                    ,getDefinitions().get(0));

            Event ev;

            int sent =0;
            while((ev = reader.getNextEvent()) != null)
            {
                client.sendEvent(ev);
                eventRateStore.increment("players");

                sent++;
                if(sent % 1000 == 0) {
                    Thread.sleep(1000);
                    System.out.println("Sent " + sent);
                }

                if(sent == 50*1000)
                {
                    break;
                }

            }
            System.out.println("Finished!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
