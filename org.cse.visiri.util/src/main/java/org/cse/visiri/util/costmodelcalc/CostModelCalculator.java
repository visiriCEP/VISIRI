package org.cse.visiri.util.costmodelcalc;


import org.cse.visiri.util.Query;


/**
 * Created by visiri on 10/31/14.
 */
public class CostModelCalculator {

    DirectEngCostModelCalculator directCal;
    SiddhiCostModelCalculator siddhiCal ;

    

    public double calculateCost(final Query q){
        // consider every query has same cost for simplicity
    //    System.out.println(q.getEngineId()+":"+q.getQuery());


        if(q.getEngineId()==0){//CEPEngine.ENGINE_TYPE_DIRECT
            if(directCal == null)
            {
                directCal = new DirectEngCostModelCalculator();
            }
            return directCal.calculateCost(q);
        }
        else if(q.getEngineId()== 1){ //CEPEngine.ENGINE_TYPE_SIDDHI
            if(siddhiCal == null)
            {
                siddhiCal = new SiddhiCostModelCalculator();
            }
            return siddhiCal.calculateCost(q);
        }


        return 10.0;
    }

}
