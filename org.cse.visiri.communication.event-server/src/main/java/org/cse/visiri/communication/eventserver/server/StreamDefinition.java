package org.cse.visiri.communication.eventserver.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visiri on 10/28/14.
 */
public class StreamDefinition {

    public enum Type {
        INTEGER, LONG, BOOLEAN, FLOAT, DOUBLE, STRING
    }

    private String streamId;
    private List<Attribute> attributeList = new ArrayList<Attribute>();

    public class Attribute {
        private String name;
        private Type type;

        private Attribute(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }
    }

    public String getStreamId() {
        return streamId;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public void addAttribute(String name, Type type) {
        this.attributeList.add(new Attribute(name, type));
    }


}
