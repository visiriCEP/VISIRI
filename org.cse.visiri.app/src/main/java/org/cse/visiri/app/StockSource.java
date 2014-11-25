package org.cse.visiri.app;

import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geeth on 2014-11-25.
 */
public class StockSource {
    //events per second
    public final int frequency = 500;

    public static void main(String[] arg) throws Exception {
        StockSource sink = new StockSource();
        sink.start();
    }

    private List<StreamDefinition> getDefinitions()
    {
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

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

        return defs;
    }

    public void start()
    {

        try {
            System.out.println(System.getProperty("user.dir"));
            EventClient client = new EventClient("localhost:6666",getDefinitions());

            CsvEventReader reader = new CsvEventReader("eem_08jan_08mayy.csv"
                                ,getDefinitions().get(0));

            Event ev;

            long sleepTime = (1000)/frequency;
            while((ev = reader.getNextEvent()) != null)
            {
                client.sendEvent(ev);
                Thread.sleep(sleepTime);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
