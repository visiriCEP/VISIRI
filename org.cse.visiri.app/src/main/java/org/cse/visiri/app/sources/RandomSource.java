package org.cse.visiri.app.sources;

import org.cse.visiri.app.util.RandomQueryGenerator;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Geeth on 2014-11-08.
 */
public class RandomSource {

    EventClient cl;
    Random r = new Random();
    private List<StreamDefinition> getDefinitions()
    {
        RandomQueryGenerator qg = new RandomQueryGenerator(1);
        final int inDefCount = 100, outDefCount = 50;
        final int attrCntMin = 2, attrCntMax= 4;

        List<StreamDefinition> inDefs = qg.generateDefinitions(inDefCount,attrCntMin,attrCntMax);
        List<StreamDefinition> outDefs = qg.generateDefinitions(outDefCount,attrCntMin,attrCntMax);


        return inDefs;
    }

    public void start() throws  Exception
    {

        cl =// new EventClient("169.254.190.2:6666",getDefinitions());
        new EventClient("localhost:6666",getDefinitions());
        //7211
    }

    private Event generateEvent(StreamDefinition def)
    {
        Event ev = new Event();
        ev.setStreamId(def.getStreamId());
        Object[] dat = new Object[def.getAttributeList().size()];
        ev.setData(dat);
        int index = 0;

        for (StreamDefinition.Attribute att : def.getAttributeList()) {
            Object o ;
            switch (att.getType()) {
                case FLOAT:
                    o = r.nextFloat() * 100;
                    break;
                case DOUBLE:
                    o = r.nextDouble() * 100;
                    break;
                case INTEGER:
                    o = r.nextInt(1000);
                    break;
                case BOOLEAN:
                    o = r.nextBoolean();
                    break;
                case STRING:
                    o = "str_" + r.nextInt(1000);
                    break;
                default:
                    o=null;
            }
            dat[index++] = o;
        }

        return ev;
    }

    public void sendEvents() throws Exception
    {
        System.out.println("Started");
        List<StreamDefinition> defs = getDefinitions();
        int count = 3* 1000* 1000;
        int freq = 100*1000;
        int defCount = defs.size();
        for(int i=1;i <= count; i++)
        {
            int defIdx = r.nextInt(defCount);
            StreamDefinition def = defs.get(defIdx);
            Event ev = generateEvent(def);
            cl.sendEvent(ev);

            if(i % freq == 0)
            {
                //Thread.sleep(1000);
                System.out.println("Sent " + i);
            }
        }

        System.out.println("Finished");
    }

    public static void main(String[] arg) throws Exception {
        RandomSource source = new RandomSource();
        source.start();
        source.sendEvents();

        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
