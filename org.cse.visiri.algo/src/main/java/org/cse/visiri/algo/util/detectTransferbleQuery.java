package org.cse.visiri.algo.util;

import org.cse.visiri.algo.util.costmodelcalc.CostModelCalculator;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by visiri on 11/23/14.
 */
public class detectTransferbleQuery {

    private Query[] queryArray;
    private CostModelCalculator costModelCalculator;

    public detectTransferbleQuery(){
        this.costModelCalculator=new CostModelCalculator();
        String myNode= Environment.getInstance().getNodeId();
        Map<String,List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
        List<Query> queryList=nodeQueryMap.get(myNode);

        queryArray=new Query[queryList.size()];
        queryArray= (Query[]) queryList.toArray();

    }

    public detectTransferbleQuery(Query[] queriesArray){
        this.costModelCalculator=new CostModelCalculator();
        this.queryArray=queriesArray;
    }

    public void updateQueryArray(){
        String myNode= Environment.getInstance().getNodeId();
        Map<String,List<Query>> nodeQueryMap=Environment.getInstance().getNodeQueryMap();
        List<Query> queryList=nodeQueryMap.get(myNode);

        queryArray=new Query[queryList.size()];
        queryArray= (Query[]) queryList.toArray();
    }

    public Query detectQuery(double[] eventRates){

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
            costArray[i]=costModelCalculator.calculateCost(query);
            costRateValueArray[i]=eventRates[i]*costArray[i];  // cost * eventRate
            costRateQueryMap.put(costRateValueArray[i],query);
            i++;
        }

        Arrays.sort(costRateValueArray);

        int middleIndex=costRateValueArray.length/2;

        return costRateQueryMap.get(costRateValueArray[middleIndex]);
    }


}
