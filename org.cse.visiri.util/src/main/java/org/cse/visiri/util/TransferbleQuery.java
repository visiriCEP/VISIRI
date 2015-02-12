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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


//TODO have to optimize the query selection
public class TransferbleQuery {

    private Query[] queryArray;

    public TransferbleQuery(List<Query> queryList){

//        String myNode= Environment.getInstance().getNodeId();
//        Map<String,List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
//        List<Query> queryList=nodeQueryMap.get(myNode);
        queryArray=new Query[queryList.size()];
        queryArray= (Query[]) queryList.toArray();
    }

    public TransferbleQuery(Query[] queriesArray){

        this.queryArray=queriesArray;
    }

    public TransferbleQuery(){

    }

    private void updateQueryArray(List<Query> qList){
//        String myNode= Environment.getInstance().getNodeId();
//        Map<String,List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
//        List<Query> queryList=nodeQueryMap.get(myNode);
          List<Query> queryList=qList;
        queryArray=new Query[queryList.size()];
        queryArray= (Query[]) queryList.toArray(new Query[0]);
    }

    public List<Query> detectTransferbleQuery(final double[] eventRates, final List<Query> qList){
        updateQueryArray(qList);
        List<Query> queryList=new ArrayList<Query>();
        if(eventRates.length!=queryArray.length){
            System.out.println("*****Error****");       //has to handle exception
            throw new UnknownError();
        }

        int size=queryArray.length;

        Arrays.sort(queryArray,new Comparator<Query>() {
            @Override
            public int compare(Query o1, Query o2) {
                double c1 = o1.getCost() *eventRates[qList.indexOf(o1)];
                double c2 = o2.getCost() * eventRates[qList.indexOf(o2)];
                double diff = (c1-c2) * 1000;
                return (int) diff;
            }
        });

        int minIndex=(int)(size*(9.0/20));
        int maxIndex=(int)(size*(11.0/20));                          //get the middle 10% of the queries

        for(int j=minIndex;j<maxIndex;j++){
            queryList.add(queryArray[j]);
        }

        return queryList;
    }

}
