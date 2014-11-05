package org.cse.visiri.communication.eventserver.server;

import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
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

    public EventServer(EventServerConfig eventServerConfig, List<StreamDefinition> streamDefinitions, StreamCallback streamCallback) {
        this.eventServerConfig = eventServerConfig;
        this.streamDefinitionList = streamDefinitions;
        this.streamCallback = streamCallback;
        //this.streamRuntimeInfo = EventServerUtils.createStreamRuntimeInfo(streamDefinition);

        this.streamRuntimeInfoHashMap=new HashMap<String, StreamRuntimeInfo>();
        for(int i=0;i<streamDefinitions.size();i++){
            // this.streamRuntimeInfos[i]=EventServerUtils.createStreamRuntimeInfo(streamDefinitions[i]);
            streamRuntimeInfoHashMap.put(streamDefinitions.get(i).getStreamId(),EventServerUtils.createStreamRuntimeInfo(streamDefinitions.get(i)));
        }
        pool = Executors.newFixedThreadPool(eventServerConfig.getNumberOfThreads());

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
                                String streamId=new String(streamNameData, 0, streamNameData.length);//
                                //System.out.println("Stream ID :"+streamId);//

                                StreamRuntimeInfo streamRuntimeInfo=streamRuntimeInfoHashMap.get(streamId);//
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

                                Event eventStream=new Event();
                                eventStream.setStreamId(streamId);
                                eventStream.setData(event);

                                streamCallback.receive(eventStream);
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
