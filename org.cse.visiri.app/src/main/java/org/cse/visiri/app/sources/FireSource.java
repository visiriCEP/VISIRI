package org.cse.visiri.app.sources;

import org.cse.visiri.app.Evaluation;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Geeth on 2014-11-08.
 */
public class FireSource {

    EventClient cl;

    private List<StreamDefinition> getDefinitions()
    {
        Evaluation ev = new Evaluation();
        return ev.getFireInputDefinitionList();
    }

    public void start() throws  Exception
    {

        cl =// new EventClient("169.254.190.2:6666",getDefinitions());
        new EventClient("localhost:7211",getDefinitions());
        //7211
    }

    public void sendEvents() throws Exception
    {
        char[] alphabet = new char[52];
        for(int i=0; i < 26 ; i++)
        {
            alphabet[i] =(char)('A' +i);
            alphabet[i+26] = (char)('a' +i);
        }

        List<StreamDefinition> defs = getDefinitions();

        for(StreamDefinition def:defs) {
            //StreamDefinition def = defs.get(2);
            System.out.println("\nSending " + def.getStreamId() + " :");
            for (int i = 0; i < 10; i++) {
                Event ev = new Event();
                ev.setStreamId(def.getStreamId());
                Object[] dat = new Object[def.getAttributeList().size()];
                ev.setData(dat);
                int index = 0;
                Random r = new Random();
                for (StreamDefinition.Attribute att : def.getAttributeList()) {
                    Object o = "<NULL>";
                    switch (att.getType()) {
                        case DOUBLE:
                            o = r.nextDouble() * 100;
                            break;
                        case INTEGER:
                            o = r.nextInt(100);
                            break;
                        case BOOLEAN:
                            o = r.nextBoolean();
                            break;
                        case STRING:
                            char[] str = new char[3];
                            for(int idx =0;idx<3;idx++)
                            {
                                str[idx] = alphabet[r.nextInt(alphabet.length)];
                            }
                            o = new String(str);
                            break;
                        case FLOAT:
                            o = r.nextFloat() * 100;
                            break;
                    }
                    dat[index++] = o;
                }
                cl.sendEvent(ev);
            }
        }
    }

    public static void main(String[] arg) throws Exception {
        FireSource source = new FireSource();
        source.start();
        source.sendEvents();

        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
