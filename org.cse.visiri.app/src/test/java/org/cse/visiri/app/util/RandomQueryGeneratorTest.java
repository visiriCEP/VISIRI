package org.cse.visiri.app.util;

import junit.framework.TestCase;
import org.cse.visiri.algo.AlgoFactory;
import org.cse.visiri.algo.QueryDistributionAlgo;
import org.cse.visiri.algo.QueryDistributionParam;
import org.cse.visiri.app.RandomEvaluation;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Geeth on 2014-12-11.
 */
public class RandomQueryGeneratorTest extends TestCase {
    RandomQueryGenerator qg;
    public void setUp() throws Exception {
        super.setUp();
        qg = new RandomQueryGenerator(3);
    }

    public void testGenerateDefinitions() throws Exception {
        List<StreamDefinition> defs = qg.generateDefinitions(15,2,6);
        assertEquals(15,defs.size());
        for(StreamDefinition d: defs)
        {
            assertTrue(d.getAttributeList().size() <= 6);
            assertTrue(d.getAttributeList().size() >= 2);

            System.out.print(d.getStreamId() + ": ");
            for(StreamDefinition.Attribute  a : d.getAttributeList())
            {
                System.out.print(a.getName() + ",");
            }
            System.out.println();
        }
    }

    public void testGenereateQueries() throws Exception
    {
        List<StreamDefinition> indefs = qg.generateDefinitions(150,4,6);
        List<StreamDefinition> outdefs = qg.generateDefinitions(100,2,4);

        List<Query> queries = qg.generateQueries(200,indefs,outdefs);

        for(Query q: queries)
        {
            System.out.print(q.getQueryId() +":\n" );
            for(StreamDefinition  d :q.getInputStreamDefinitionsList())
            {
                System.out.print(d.getStreamId() + " ");
            }

            System.out.println(" -> " + q.getOutputStreamDefinition().getStreamId());
            System.out.println(q.getQuery());
            System.out.println();
        }
        assertEquals(200,queries.size());
    }



    public  void testDistribution()
    {
        RandomEvaluation re = new RandomEvaluation();

        QueryDistributionParam param = new QueryDistributionParam();
        param.setDispatcherList(Arrays.asList("1.1.1.1"));
        param.setNodeList(Arrays.asList("2.1.1.1", "2.1.1.2", "2.1.1.3","2.1.1.4"));
        param.setQueries(re.getQueries());
        param.setNodeQueryTable(new HashMap<String, List<Query>>());

        QueryDistributionAlgo algo = AlgoFactory.createAlgorithm(QueryDistributionAlgo.SCTXPF_PLUS_ALGO);
        QueryDistribution dist = algo.getQueryDistribution(param);

        HashMap<String,Integer> counts = new HashMap<String, Integer>();
        counts.put("2.1.1.1",0);
        counts.put("2.1.1.2",0);
        counts.put("2.1.1.3",0);
        counts.put("2.1.1.4",0);
        counts.put("1.1.1.1",0);
        for(String s :dist.getQueryAllocation().values())
        {
            counts.put(s,counts.get(s) +1);
        }

        for(String s: counts.keySet())
        {
            System.out.println(s +" : " +counts.get(s));
        }

    }
}
