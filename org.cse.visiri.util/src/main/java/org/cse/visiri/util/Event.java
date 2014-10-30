package org.cse.visiri.util;

/**
 * Created by Geeth on 2014-10-30.
 */
public class Event {
    private String streamId;
    private Object[] data;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }
}
