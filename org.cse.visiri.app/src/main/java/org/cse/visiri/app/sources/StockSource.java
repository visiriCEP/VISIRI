package org.cse.visiri.app.sources;

import org.cse.visiri.app.util.CsvEventReader;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Geeth on 2014-11-25.
 */
public class StockSource {
    //events per second
    public final int frequency = 10000;
    EventRateStore eventRateStore;
    public static void main(String[] arg) throws Exception {
        StockSource sink = new StockSource();
        sink.start();
    }

    private List<StreamDefinition> getDefinitions()
    {
        eventRateStore=new EventRateStore();
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

            int sent =0;
            for(int i=0;i<20;i++){
                CsvEventReader reader = new CsvEventReader("eem_08jan_08mayy.csv"
                        ,getDefinitions().get(0));

                Event ev;
                while((ev = reader.getNextEvent()) != null)
                {
                    client.sendEvent(ev);
                    eventRateStore.increment("StockSource");

                    if(sent % 1000 == 0) {
                        Thread.sleep(1000);
                        sent = 0;
                    }
                    sent++;
                }
            }

            System.out.println("Finished. Press enter");
            new Scanner(System.in).next();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
