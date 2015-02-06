package org.cse.visiri.util;

import org.cse.visiri.util.costmodelcalc.CostModelCalculator;

import org.cse.visiri.util.Query;

import java.util.*;

/**
 * Created by visiri on 11/23/14.
 */
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

    public List<Query> detectTransferbleQuery(double[] eventRates,List<Query> qList){
        updateQueryArray(qList);
        List<Query> queryList=new ArrayList<Query>();
        if(eventRates.length!=queryArray.length){
            System.out.println("*****Error****");       //has to handle exception
            throw new UnknownError();
        }

        int size=queryArray.length;

        double[] costArray=new double[size];
        double[] costRateValueArray=new double[size];
        Map<Double,Query> costRateQueryMap=new HashMap<Double, Query>();

        int i=0;
        for(Query query:queryArray){

            costArray[i]=query.getCost();
            costRateValueArray[i]=eventRates[i]*costArray[i];  // cost * eventRate
           // costRateValueArray[i]=eventRates[i]; //just the event rate for dynamic test only
            costRateQueryMap.put(costRateValueArray[i],query);
            i++;
        }

        Arrays.sort(costRateValueArray);

        int minIndex=(int)(size*(9.0/20));
        int maxIndex=(int)(size*(11.0/20));                          //get the middle 10% of the queries

        for(int j=minIndex;j<maxIndex;j++){
            queryList.add(costRateQueryMap.get(costRateValueArray[j]));
        }

        return queryList;
    }

}
