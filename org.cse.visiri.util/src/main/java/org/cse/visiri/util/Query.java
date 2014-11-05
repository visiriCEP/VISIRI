package org.cse.visiri.util;


import java.io.Serializable;
import java.util.List;

/**
 * Created by visiri on 10/30/14.
 */
public class Query implements Serializable{
    private String query;
    private List<StreamDefinition> inputStreamDefinitionsList;
    private StreamDefinition outputStreamDefinition;
    private String queryId;
    private int engineId;

    public Query(String query,List<StreamDefinition> inputStreamDefinitionList,StreamDefinition outputStreamDefinition,String queryId,int engineId){
        this.setQuery(query);
        this.setInputStreamDefinitionsList(inputStreamDefinitionList);
        this.setOutputStreamDefinition(outputStreamDefinition);
        this.setQueryId(queryId);
        this.setEngineId(engineId);
    }


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<StreamDefinition> getInputStreamDefinitionsList() {
        return inputStreamDefinitionsList;
    }

    public void setInputStreamDefinitionsList(List<StreamDefinition> inputStreamDefinitionsList) {
        this.inputStreamDefinitionsList = inputStreamDefinitionsList;
    }

    public void addInputStreamDefinition(StreamDefinition streamDefinition){
        this.inputStreamDefinitionsList.add(streamDefinition);
    }

    public StreamDefinition getOutputStreamDefinition() {
        return outputStreamDefinition;
    }

    public void setOutputStreamDefinition(StreamDefinition outputStreamDefinition) {
        this.outputStreamDefinition = outputStreamDefinition;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public int getEngineId() {
        return engineId;
    }

    public void setEngineId(int engineId) {
        this.engineId = engineId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Query )
        {
            return ((Query)obj).getQueryId().equals(this.getQueryId());
        }
        else {
            return false;
        }
    }
}
