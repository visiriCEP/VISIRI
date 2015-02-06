package org.cse.visiri.app;

import junit.framework.TestCase;
import org.cse.visiri.app.util.RandomMeasure;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.costmodelcalc.CostModelCalculator;

import java.util.*;

/**
 * Created by Geeth on 2015-01-01.
 */
public class RandomEvaluationTest extends TestCase {

    RandomMeasure ev = new RandomMeasure();

//    public void testGetInputDefinitions() throws Exception {
//
//    }
//
//    public void testGetOutputDefinitions() throws Exception {
//
//    }

    public void testGetQueries() throws Exception
    {

        CostModelCalculator costCal = new CostModelCalculator();
        final Map<Query,Double> costs = new HashMap<Query,Double>();

        List<Query> queries = ev.getQueries();

        int index = 0;
        for(Query q: queries)
        {
            if(++index % 10 == 0)
            {
                System.out.print(index + " ");
            }
            double c = costCal.calculateCost(q);
            costs.put(q,c);
        }

        List<Query> sorted = new ArrayList<Query>(queries);
        Collections.sort(sorted,new Comparator<Query>() {
            @Override
            public int compare(Query o1, Query o2) {
                return  (int)((costs.get(o1) - costs.get(o2)) * 1000);
            }
        });

        int count = sorted.size();
        System.out.println("--min--");
        System.out.println(sorted.get(0).getQuery());
        System.out.println(costs.get(sorted.get(0)));
        System.out.println();

        System.out.println("--max--");
        System.out.println(sorted.get(count-1).getQuery());
        System.out.println(costs.get(sorted.get(count-1)));
        System.out.println();

    }
}
