package org.cse.visiri.util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by visiri on 10/30/14.
 */
public class Query implements Serializable{
    private String query;
    private List<StreamDefinition> inputStreamDefinitionsList;
    private StreamDefinition outputStreamDefinition;
    private String queryId;
    private int engineId;
    private double cost;

    public Query(String query,List<StreamDefinition> inputStreamDefinitionList,StreamDefinition outputStreamDefinition,String queryId,int engineId){
        this.setQuery(query);
        if(inputStreamDefinitionList == null)
        {
            inputStreamDefinitionList = new ArrayList<StreamDefinition>();
        }
        this.setInputStreamDefinitionsList(inputStreamDefinitionList);
        this.setOutputStreamDefinition(outputStreamDefinition);
        this.setQueryId(queryId);
        this.setEngineId(engineId);
    }

    /** Create a copy of a query **/
    public Query(Query q, boolean newID)
    {
        this.setQuery(q.query);
        this.setInputStreamDefinitionsList(new ArrayList<StreamDefinition>());
        for(StreamDefinition sd : q.getInputStreamDefinitionsList())
        {
            this.addInputStreamDefinition(new StreamDefinition(sd));
        }
        if(q.getOutputStreamDefinition() != null) {
            this.setOutputStreamDefinition(new StreamDefinition(q.getOutputStreamDefinition()));
        }
        this.setQueryId(queryId);
        this.setEngineId(engineId);

        if(newID)
        {
            setQueryId(getNewQueryId());
        }
    }

    private static String getNewQueryId()
    {
        return UUID.randomUUID().toString();
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

    public double getCost() {
         return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
