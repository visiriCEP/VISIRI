package org.cse.visiri.algo;

import org.cse.visiri.algo.util.CostModelCalculator;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Geeth on 2014-11-04.
 */

/**
A modified version of SCTXPF algorithm which takes query cost into account
and considers event types instead of used event attributes
**/
public class SCTXPFDistributionAlgo extends QueryDistributionAlgo {

    public int maximumThreshold = 30;


    @Override
    QueryDistribution getQueryDistribution(List<Query> queries) {

        QueryDistribution dist = new QueryDistribution();
        Environment env = Environment.getInstance();

        CostModelCalculator costCal = new CostModelCalculator();


        for(Query q : queries)
        {

        }


        return null;
    }
}
