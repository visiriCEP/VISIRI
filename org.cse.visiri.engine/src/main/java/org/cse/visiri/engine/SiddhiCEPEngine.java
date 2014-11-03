package org.cse.visiri.engine;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;

import java.util.List;

/**
 * Created by visiri on 11/3/14.
 */
public class SiddhiCEPEngine extends CEPEngine {

    private SiddhiManager siddhiManager;
    private Query query;


    public SiddhiCEPEngine(Query query){
        this.query=query;

    }

    @Override
    public void start() {

        this.siddhiManager=new SiddhiManager();
        List<StreamDefinition> inputStreamDefinitionList=query.getInputStreamDefinitionsList();
        String queryString=query.getQuery();
        String outputStreamId=query.getOutputStreamDefinition().getStreamId();

       //org.wso2.siddhi.query.api.definition.StreamDefinition streamDefinition;


        for(int i=0;i<inputStreamDefinitionList.size();i++){
           // siddhiManager.defineStream(inputStreamDefinitionList.get(i)); //StreamDefinition problem
        }

        siddhiManager.addQuery(queryString);
        siddhiManager.addCallback(outputStreamId,new StreamCallback() {
            @Override
            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                //to do with output stream

            }
        });

    }

    @Override
    public void stop() {
        siddhiManager.shutdown();

    }

    @Override
    public Object saveState() {

        return null;
    }

    @Override
    public void sendEvent(Event event) {
        InputHandler inputHandler=siddhiManager.getInputHandler(event.getStreamId());
        try {
            inputHandler.send(event.getData());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
