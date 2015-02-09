/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.app;

import org.cse.visiri.app.util.RandomMeasure;
import org.cse.visiri.communication.eventserver.client.EventClient;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.communication.eventserver.server.EventServerConfig;
import org.cse.visiri.communication.eventserver.server.StreamCallback;
import org.cse.visiri.engine.OutputEventReceiver;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class RandomSingleNode implements StreamCallback {
    private EventServer server;
    private OutputEventReceiver receiver;
    private SiddhiManager siddhiManager;
    private EventClient cl;

    List<StreamDefinition> inDef ;
    List<StreamDefinition> outDef ;
    List<Query> queries ;


    public RandomSingleNode() throws Exception {

        RandomMeasure ev = new RandomMeasure();
        inDef = ev.getInputDefinitions();
        outDef = ev.getOutputDefinitions();
        queries = ev.getQueries();
        start();
    }
    public RandomSingleNode(List<StreamDefinition> inDef,  List<StreamDefinition> outDef,
            List<Query> queries) throws Exception {

        this.inDef = inDef;
        this.outDef = outDef;
        this.queries = queries;

        start();
    }

    private void start() throws Exception
    {


        receiver = new OutputEventReceiver();
        cl =new EventClient("localhost:6666",outDef);
        receiver.addDestinationIp("localhost:6666",cl);

        initSiddhi();


        EventServerConfig conf = new EventServerConfig(EventServer.DEFAULT_PORT);
        server = new EventServer(conf,inDef,this,"Node");
        server.start();
    }

    private void initSiddhi()
    {
        siddhiManager = new SiddhiManager();


        for(int i=0;i<inDef.size();i++) {
            org.wso2.siddhi.query.api.definition.StreamDefinition streamDefinition;
            streamDefinition = new org.wso2.siddhi.query.api.definition.StreamDefinition();
            streamDefinition = streamDefinition.name(inDef.get(i).getStreamId());

            List<StreamDefinition.Attribute> attributeList = inDef.get(i).getAttributeList();

            for (int j = 0; j < attributeList.size(); j++) {
                StreamDefinition.Type type = attributeList.get(j).getType();
                Attribute.Type type1;
                if (type.equals(StreamDefinition.Type.STRING)) {
                    type1 = Attribute.Type.STRING;
                } else if (type.equals(StreamDefinition.Type.INTEGER)) {
                    type1 = Attribute.Type.INT;
                } else if (type.equals(StreamDefinition.Type.DOUBLE)) {
                    type1 = Attribute.Type.DOUBLE;
                } else if (type.equals(StreamDefinition.Type.FLOAT)) {
                    type1 = Attribute.Type.FLOAT;
                } else if (type.equals(StreamDefinition.Type.LONG)) {
                    type1 = Attribute.Type.LONG;
                } else if (type.equals(StreamDefinition.Type.BOOLEAN)) {
                    type1 = Attribute.Type.BOOL;
                } else {
                    type1 = Attribute.Type.TYPE;
                }
                streamDefinition.attribute(attributeList.get(j).getName(), type1);
            }
            siddhiManager.defineStream(streamDefinition);
        }

        int added =0;
        System.out.println("Adding queries");
        for(Query q: queries)
        {
            siddhiManager.addQuery(q.getQuery());
            if(++added % 100 ==0)
            {
                System.out.print(added + " ");
            }
        }
        System.out.println("Done!");
        org.wso2.siddhi.core.stream.output.StreamCallback callback = new org.wso2.siddhi.core.stream.output.StreamCallback() {
            @Override
            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                //to do with output stream

                for (int i = 0; i < events.length; i++) {
                    Event event = new Event();
                    event.setStreamId(events[i].getStreamId());
                    event.setData(events[i].getData());


                    try {
                        try {
                            receiver.sendEvents(event);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };



        for(StreamDefinition sd: outDef) {
            siddhiManager.addCallback(sd.getStreamId(),callback);
        }



    }

    @Override
    public void receive(Event event) {


        InputHandler inputHandler=siddhiManager.getInputHandler(event.getStreamId());
        //System.out.println(event.getStreamId());
        try {
            inputHandler.send(event.getData());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] arg) throws Exception {

        RandomSingleNode rs = new RandomSingleNode();

        Scanner sc = new Scanner(System.in);
        System.out.println("Started");
        sc.next();
    }
}
