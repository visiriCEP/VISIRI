package org.cse.visiri.util.costmodelcalc;


import org.cse.visiri.util.Query;


/**
 * Created by visiri on 10/31/14.
 */
public class CostModelCalculator {

    public double calculateCost(final Query q){
        // consider every query has same cost for simplicity
        System.out.println(q.getEngineId()+":"+q.getQuery());
        if(q.getEngineId()==0){//CEPEngine.ENGINE_TYPE_DIRECT
            return new DirectEngCostModelCalculator().calculateCost(q);
        }
        else if(q.getEngineId()== 1){ //CEPEngine.ENGINE_TYPE_SIDDHI
            return new SiddhiCostModelCalculator().calculateCost(q);
        }


        return 10.0;
    }

}
