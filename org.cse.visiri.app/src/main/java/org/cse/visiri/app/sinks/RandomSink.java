package org.cse.visiri.app.sinks;

import org.cse.visiri.app.RandomEvaluation;
import org.cse.visiri.app.util.Writer;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;

/**
 * Created by Geeth on 2014-11-08.
 */
public class RandomSink {

    private static int port = 6666;
    EventServer server;
    EventRateStore eventRateStore;
    Writer writer;

    private long count;

    public RandomSink(){
        writer=new Writer(100*1000);
    }

    private  List<StreamDefinition> getDefinitions()
    {
        eventRateStore=EventRateStore.getInstance();

        RandomEvaluation ev=  new RandomEvaluation();
        List<StreamDefinition> inDefs = ev.getInputDefinitions();
        List<StreamDefinition> outDefs = ev.getOutputDefinitions();

        return outDefs;
    }

    public void start() throws Exception {
        EventServerConfig conf = new EventServerConfig(port);
        conf.setEventRateStoreFrequency(100*1000);
        server = new EventServer(conf,getDefinitions(), new StreamCallback() {
            @Override
            public void receive(Event event) {
//                count++;
                writer.write();
//                if(count%20000==0) {
//                    System.out.print("Received :");
//                    System.out.println(count);
//                 //   System.out.printf(" :Event received : %s ", event.getStreamId());
//
//
//                }

               // eventRateStore.increment();


            }
        },"EventSink");

        System.out.println("Event sink started at port " + port);
        server.start();

    }

    public static void main(String[] arg) throws Exception {
        RandomSink sink = new RandomSink();
        sink.start();
    }



}