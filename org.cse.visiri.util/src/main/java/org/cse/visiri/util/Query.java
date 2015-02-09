/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Query implements Serializable{
    private String query;
    private List<StreamDefinition> inputStreamDefinitionsList;
    private StreamDefinition outputStreamDefinition;
    private String queryId;
    private int engineId;
    private double cost = 1.0;

    public Query(String query,List<StreamDefinition> inputStreamDefinitionList,StreamDefinition outputStreamDefinition,String queryId,int engineId,double cost){
        this.setQuery(query);
        if(inputStreamDefinitionList == null)
        {
            inputStreamDefinitionList = new ArrayList<StreamDefinition>();
        }
        this.setInputStreamDefinitionsList(inputStreamDefinitionList);
        this.setOutputStreamDefinition(outputStreamDefinition);
        this.setQueryId(queryId);
        this.setEngineId(engineId);
        this.setCost(cost);
    }

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
        this.setQuery(q.getQuery());
        this.setInputStreamDefinitionsList(new ArrayList<StreamDefinition>());
        for(StreamDefinition sd : q.getInputStreamDefinitionsList())
        {
            this.addInputStreamDefinition(new StreamDefinition(sd));
        }
        if(q.getOutputStreamDefinition() != null) {
            this.setOutputStreamDefinition(new StreamDefinition(q.getOutputStreamDefinition()));
        }
        this.setQueryId(q.getQueryId());
        this.setEngineId(q.getEngineId());
        this.setCost(q.getCost());

        if(newID)
        {
            setQueryId(getNewQueryId());
        }
    }

    private static int __last = 1;
    private static String getNewQueryId()
    {
        return "gen" + __last++;
       // return UUID.randomUUID().toString();
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
