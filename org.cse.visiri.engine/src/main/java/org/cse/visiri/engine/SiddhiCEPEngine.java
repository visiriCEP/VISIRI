package org.cse.visiri.engine;

import com.hazelcast.core.Hazelcast;
import org.apache.log4j.BasicConfigurator;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.*;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.io.IOException;
import java.util.List;


/**
 * Created by visiri on 11/3/14.
 */
public class SiddhiCEPEngine extends CEPEngine {

    private SiddhiManager siddhiManager;
    private Query query;
    private OutputEventReceiver outputEventReceiver;
    public EventRateStore eventRateStore;


    public SiddhiCEPEngine(Query query,OutputEventReceiver outputEventReceiver){
        eventRateStore=new EventRateStore();
        this.query=query;
        this.outputEventReceiver=outputEventReceiver;
        this.start();

    }

    public double getAvgEventRate(){
        return eventRateStore.getAverageRate();
    }

    public double getInstantEventRate(){
        return eventRateStore.getInstantRate();
    }

    @Override
    public void start() {

        this.siddhiManager=new SiddhiManager();
        List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
        String queryString=query.getQuery();
        String outputStreamId=query.getOutputStreamDefinition().getStreamId();

        for(int i=0;i<inputStreamDefinitionList.size();i++){
            org.wso2.siddhi.query.api.definition.StreamDefinition streamDefinition;
            streamDefinition = new org.wso2.siddhi.query.api.definition.StreamDefinition();
            streamDefinition = streamDefinition.name(inputStreamDefinitionList.get(i).getStreamId());

            List<StreamDefinition.Attribute> attributeList=inputStreamDefinitionList.get(i).getAttributeList();

            for(int j=0;j<attributeList.size();j++){
                StreamDefinition.Type type=attributeList.get(j).getType();
                Attribute.Type type1;
                if(type.equals(StreamDefinition.Type.STRING)){
                    type1= Attribute.Type.STRING;
                }else if (type.equals(StreamDefinition.Type.INTEGER)){
                    type1=Attribute.Type.INT;
                }else if(type.equals(StreamDefinition.Type.DOUBLE)){
                    type1=Attribute.Type.DOUBLE;
                }else if(type.equals(StreamDefinition.Type.FLOAT)){
                    type1=Attribute.Type.FLOAT;
                }else if (type.equals(StreamDefinition.Type.LONG)){
                    type1=Attribute.Type.LONG;
                }else if (type.equals(StreamDefinition.Type.BOOLEAN)){
                    type1=Attribute.Type.BOOL;
                }else{
                    type1=Attribute.Type.TYPE;
                }

                streamDefinition.attribute(attributeList.get(j).getName(),type1 );

            }

           siddhiManager.defineStream(streamDefinition);

        }

        siddhiManager.addQuery(queryString);
        siddhiManager.addCallback(outputStreamId,new StreamCallback() {
            @Override
            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                //to do with output stream

                for(int i=0;i<events.length;i++){
                    Event event=new Event();
                    event.setStreamId(events[i].getStreamId());
                    event.setData(events[i].getData());
                    try {
                        outputEventReceiver.sendEvents(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void stop() {
        siddhiManager.shutdown();
    }

    @Override
    public Object saveState() {

        return siddhiManager.persist();

    }

    @Override
    public void sendEvent(Event event) {
        eventRateStore.increment();
        InputHandler inputHandler=siddhiManager.getInputHandler(event.getStreamId());
        try {
            inputHandler.send(event.getData());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
