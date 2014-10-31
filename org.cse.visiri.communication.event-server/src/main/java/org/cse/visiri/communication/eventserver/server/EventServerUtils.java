package org.cse.visiri.communication.eventserver.server;

import org.cse.visiri.util.StreamDefinition;

/**
 * Created by visiri on 10/28/14.
 */
public class EventServerUtils {

    public static StreamRuntimeInfo createStreamRuntimeInfo(StreamDefinition streamDefinition) {
        StreamRuntimeInfo streamRuntimeInfo= new StreamRuntimeInfo(streamDefinition.getStreamId());

        int messageSize=0;
        int stringAttributes=0;
        StreamDefinition.Type[] attributeTypes=new StreamDefinition.Type[streamDefinition.getAttributeList().size()];

        java.util.List<StreamDefinition.Attribute> attributeList = streamDefinition.getAttributeList();
        for (int i = 0; i < attributeList.size(); i++) {
            StreamDefinition.Attribute attribute = attributeList.get(i);
            switch (attribute.getType()) {

                case INTEGER:
                    messageSize += 4;
                    attributeTypes[i]= StreamDefinition.Type.INTEGER;
                    break;
                case LONG:
                    messageSize += 8;
                    attributeTypes[i]= StreamDefinition.Type.LONG;
                    break;
                case BOOLEAN:
                    messageSize += 1;
                    attributeTypes[i]= StreamDefinition.Type.BOOLEAN;
                    break;
                case FLOAT:
                    messageSize += 4;
                    attributeTypes[i]= StreamDefinition.Type.FLOAT;
                    break;
                case DOUBLE:
                    messageSize += 8;
                    attributeTypes[i]= StreamDefinition.Type.DOUBLE;
                    break;
                case STRING:
                    messageSize += 2;
                    stringAttributes++;
                    attributeTypes[i]= StreamDefinition.Type.STRING;
                    break;
            }
        }
        streamRuntimeInfo.setFixedMessageSize(messageSize);
        streamRuntimeInfo.setNoOfStringAttributes(stringAttributes);
        streamRuntimeInfo.setNoOfAttributes(streamDefinition.getAttributeList().size());
        streamRuntimeInfo.setAttributeTypes(attributeTypes);

        return streamRuntimeInfo;
    }
}
