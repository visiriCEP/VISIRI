package org.cse.visiri.util;


import java.util.List;

/**
 * Created by visiri on 10/30/14.
 */
public class Query {
    private String query;
    private List<StreamDefinition> inputStreamDefinitionsList;
    private StreamDefinition outputStreamDefinition;
    private int queryId;
    private int engingId;

    public Query(String query,List<StreamDefinition> inputStreamDefinitionList,StreamDefinition outputStreamDefinition,int queryId,int engingId){
        this.setQuery(query);
        this.setInputStreamDefinitionsList(inputStreamDefinitionList);
        this.setOutputStreamDefinition(outputStreamDefinition);
        this.setQueryId(queryId);
        this.setEngingId(engingId);
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

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getEngingId() {
        return engingId;
    }

    public void setEngingId(int engingId) {
        this.engingId = engingId;
    }
}
