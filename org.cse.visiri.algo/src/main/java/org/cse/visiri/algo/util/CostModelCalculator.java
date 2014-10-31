package org.cse.visiri.algo.util;

import org.cse.visiri.util.Query;

/**
 * Created by visiri on 10/31/14.
 */
public class CostModelCalculator {

    private Query query;
    private double cost;

    public CostModelCalculator(Query query){
        this.query=query;
    }

    public void calculateCost(){

    }

    public double getCost(){
        return cost;
    }
}
