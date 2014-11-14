package org.cse.visiri.algo.util.costmodelcalc;

import org.cse.visiri.util.Query;

/**
 * Created by lasitha on 11/13/14.
 */
public class DirectEngCostModelCalculator extends CostModelCalculator {
    @Override
    public double calculateCost(Query q) {
        return 10.0;
    }
}
