package org.cse.visiri.communication.eventserver.server;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.StreamDefinition;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by visiri on 10/28/14.
 */

public class EventServer {

    public static final int DEFAULT_PORT = 7211;
    private EventServerConfig eventServerConfig = new EventServerConfig(DEFAULT_PORT);
    private List<StreamDefinition> streamDefinitionList;
    private StreamCallback streamCallback;
    private ExecutorService pool;
    //private StreamRuntimeInfo streamRuntimeInfo;
    private HashMap<String,StreamRuntimeInfo> streamRuntimeInfoHashMap;
    private EventRateStore eventRateStore;

    private HashMap<String,Queue> eventBufferQueueMap;
    private HashMap<String,Boolean> eventBufferConditionMap;

    private String identifier;


    public EventServer(EventServerConfig eventServerConfig, List<StreamDefinition> streamDefinitions, StreamCallback streamCallback,String identifier) {
        this.identifier=identifier;
        eventRateStore=new EventRateStore();

        this.eventServerConfig = eventServerConfig;
        this.streamDefinitionList = streamDefinitions;
        this.streamCallback = streamCallback;
        //this.streamRuntimeInfo = EventServerUtils.createStreamRuntimeInfo(streamDefinition);

        this.streamRuntimeInfoHashMap=new HashMap<String, StreamRuntimeInfo>();
        this.eventBufferQueueMap =new HashMap<String, Queue>();
        this.eventBufferConditionMap=new HashMap<String, Boolean>();
        for(int i=0;i<streamDefinitions.size();i++){
            // this.streamRuntimeInfos[i]=EventServerUtils.createStreamRuntimeInfo(streamDefinitions[i]);
            streamRuntimeInfoHashMap.put(streamDefinitions.get(i).getStreamId(),EventServerUtils.createStreamRuntimeInfo(streamDefinitions.get(i)));
            eventBufferQueueMap.put(streamDefinitions.get(i).getStreamId(),new LinkedList());
            eventBufferConditionMap.put(streamDefinitions.get(i).getStreamId(),false);
        }
        pool = Executors.newFixedThreadPool(eventServerConfig.getNumberOfThreads());

        System.out.print("Event Server started for :");
        for(StreamDefinition sd : streamDefinitionList)
        {
            System.out.print(sd.getStreamId()+",");
        }
        System.out.println("\n");
    }


    public void start() throws Exception {
        System.out.println("Starting on " + eventServerConfig.getPort());
        ServerSocket welcomeSocket = new ServerSocket(eventServerConfig.getPort());
        while (true) {
            try {
                final Socket connectionSocket = welcomeSocket.accept();
                pool.submit(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            BufferedInputStream in = new BufferedInputStream(connectionSocket.getInputStream());

                            while (true) {
                                int streamNameSize = loadData(in) & 0xff;
                                byte[] streamNameData = loadData(in, new byte[streamNameSize]);
                                String streamId = new String(streamNameData, 0, streamNameData.length);//
                          //      System.out.println("Stream ID :"+streamId);//

                                StreamRuntimeInfo streamRuntimeInfo = streamRuntimeInfoHashMap.get(streamId);//
                                Object[] event = new Object[streamRuntimeInfo.getNoOfAttributes()];
                                byte[] fixedMessageData = loadData(in, new byte[streamRuntimeInfo.getFixedMessageSize()]);

                                ByteBuffer bbuf = ByteBuffer.wrap(fixedMessageData, 0, fixedMessageData.length);
                                StreamDefinition.Type[] attributeTypes = streamRuntimeInfo.getAttributeTypes();
                                for (int i = 0; i < attributeTypes.length; i++) {
                                    StreamDefinition.Type type = attributeTypes[i];
                                    switch (type) {
                                        case INTEGER:
                                            event[i] = bbuf.getInt();
                                            continue;
                                        case LONG:
                                            event[i] = bbuf.getLong();
                                            continue;
                                        case BOOLEAN:
                                            event[i] = bbuf.get() == 1;
                                            continue;
                                        case FLOAT:
                                            event[i] = bbuf.getFloat();
                                            continue;
                                        case DOUBLE:
                                            event[i] = bbuf.getLong();
                                            continue;
                                        case STRING:
                                            int size = bbuf.getShort() & 0xffff;
                                            byte[] stringData = loadData(in, new byte[size]);
                                            event[i] = new String(stringData, 0, stringData.length);
                                    }
                                }

                                Event eventStream = new Event();
                                eventStream.setStreamId(streamId);
                                eventStream.setData(event);

                                eventRateStore.increment(identifier);

                                if(!eventBufferConditionMap.get(streamId)) {
                                    streamCallback.receive(eventStream);
                               //     System.out.println("event received"+streamId);
                                }else{
                                    Queue<Object> tmpQ= eventBufferQueueMap.get(streamId);
                                    tmpQ.add(eventStream);
                                    eventBufferQueueMap.put(streamId, tmpQ);
                                }
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void addStreamDefinition(StreamDefinition streamDefinition){

        if(!streamDefinitionList.contains(streamDefinition)) {
            streamDefinitionList.add(streamDefinition);
            streamRuntimeInfoHashMap.put(streamDefinition.getStreamId(), EventServerUtils.createStreamRuntimeInfo(streamDefinition));
        }
    }

    public double getAverageEventRate(){
        return eventRateStore.getAverageRate();
    }
    public double getInstantEventRate(){
        return eventRateStore.getInstantRate();
    }

    public void bufferStateChanged(List<String> bufferingEventList){
        Iterator it = eventBufferQueueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if(!bufferingEventList.contains(pairs.getKey())){
                Queue<Event> tmpQ= eventBufferQueueMap.get(pairs.getKey());
                eventBufferConditionMap.put(pairs.getKey().toString(),false);
                System.out.println("releasing buffer:"+pairs.getKey());
                for(Event e:tmpQ){
                    streamCallback.receive(e);
                }
                eventBufferQueueMap.put(pairs.getKey().toString(),new LinkedList());
            }
            else{
                eventBufferConditionMap.put(pairs.getKey().toString(),true);
            }
        }
    }

    private int loadData(BufferedInputStream in) throws IOException {

        while (true) {
            int byteData = in.read();
            if (byteData != -1) {
                return byteData;
            }
        }
    }

    private byte[] loadData(BufferedInputStream in, byte[] dataArray) throws IOException {

        int start = 0;
        while (true) {
            int readCount = in.read(dataArray, start, dataArray.length - start);
            start += readCount;
            if (start == dataArray.length) {
                return dataArray;
            }
        }
    }
}
