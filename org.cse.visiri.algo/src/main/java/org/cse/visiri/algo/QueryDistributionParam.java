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

package org.cse.visiri.algo;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;


public class QueryDistributionParam {
    private List<Query> queries;
    private Map<String,List<Query>> nodeQueryTable;
    private List<String> nodeList;
    private List<String> dispatcherList;

    public  static QueryDistributionParam fromEnvironment()
    {
        Environment env = Environment.getInstance();
        QueryDistributionParam param = new QueryDistributionParam();
        param.setNodeQueryTable(env.getNodeQueryMap());
        param.setNodeList(env.getNodeIdList(Environment.NODE_TYPE_PROCESSINGNODE));
        param.setDispatcherList(env.getNodeIdList(Environment.NODE_TYPE_DISPATCHER));

        return param;
    }


    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public Map<String, List<Query>> getNodeQueryTable() {
        return nodeQueryTable;
    }

    public void setNodeQueryTable(Map<String, List<Query>> nodeQueryTable) {
        this.nodeQueryTable = nodeQueryTable;
    }

    public List<String> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<String> nodeList) {
        this.nodeList = nodeList;
    }

    public List<String> getDispatcherList() {
        return dispatcherList;
    }

    public void setDispatcherList(List<String> dispatcherList) {
        this.dispatcherList = dispatcherList;
    }
}
