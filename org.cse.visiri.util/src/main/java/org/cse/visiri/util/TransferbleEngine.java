package org.cse.visiri.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by visiri on 1/13/15.
 */
public class TransferbleEngine {
    private Map<String,List<Query>> nodeTransferbleQueryMap;
    private Set<StreamDefinition> completlyRemovedEvents;


    public TransferbleEngine(Map<String,List<Query>> nodeTransferbleQueryMap,Set<StreamDefinition> completlyRemovedEvents){
        this.nodeTransferbleQueryMap=nodeTransferbleQueryMap;
        this.completlyRemovedEvents=completlyRemovedEvents;
    }


    public Map<String,List<Query>> getNodeTransferbleQueryMap(){
        return nodeTransferbleQueryMap;
    }

    public Set<StreamDefinition> getCompletlyRemovedEvents(){
        return completlyRemovedEvents;
    }

}
