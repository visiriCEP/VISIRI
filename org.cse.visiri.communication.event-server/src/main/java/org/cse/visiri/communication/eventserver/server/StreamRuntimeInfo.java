package org.cse.visiri.communication.eventserver.server;

import org.cse.visiri.util.StreamDefinition;

/**
 * Created by visiri on 10/28/14.
 */
public class StreamRuntimeInfo {

    private String streamId;
    private int fixedMessageSize;
    private int noOfStringAttributes;
    private int noOfAttributes;
    private StreamDefinition.Type[] attributes;

    public StreamRuntimeInfo(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamId() {
        return streamId;
    }

    public int getFixedMessageSize() {
        return fixedMessageSize;
    }

    public void setFixedMessageSize(int fixedMessageSize) {
        this.fixedMessageSize = fixedMessageSize;
    }

    public int getNoOfStringAttributes() {
        return noOfStringAttributes;
    }

    public void setNoOfStringAttributes(int noOfStringAttributes) {
        this.noOfStringAttributes = noOfStringAttributes;
    }

    public void setNoOfAttributes(int noOfAttributes) {
        this.noOfAttributes = noOfAttributes;
    }

    public int getNoOfAttributes() {
        return noOfAttributes;
    }

    public void setAttributeTypes(StreamDefinition.Type[] attributes) {
        this.attributes = attributes;
    }

    public StreamDefinition.Type[] getAttributeTypes() {
        return attributes;
    }
}
