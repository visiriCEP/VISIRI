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

package org.cse.visiri.util.costmodelcalc;


import org.cse.visiri.util.Query;



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
                siddhiCal = new FastSiddhiCostModelCalculator();
            }
            return siddhiCal.calculateCost(q);
        }


        return 10.0;
    }

}
