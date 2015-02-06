package org.cse.visiri.app.algoevaluation;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.cse.visiri.algo.*;
import org.cse.visiri.app.util.FilteredQueryGenerator;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.costmodelcalc.CostModelCalculator;

import java.util.*;

/**
 * Created by Geeth on 2015-01-06.
 */
public class DistributionEval {

    final String DISP_PREFIX = "20.1.";
    final String NODE_PREFIX = "10.1.";

    public void EvaluateDistribution(QueryDistribution dist)
    {
        Map<String,Integer> allInfo = new TreeMap<String, Integer>();
        Map<String,Integer> nodeInfo = new TreeMap<String, Integer>();
        Map<String,Double> nodeCosts = new TreeMap<String, Double>();
        for(Query q: dist.getQueryAllocation().keySet())
        {
            String node = dist.getQueryAllocation().get(q);
            if(!allInfo.containsKey(node))
            {
                allInfo.put(node, 0);
            }
            int val= allInfo.get(node);
            allInfo.put(node, val + 1);
            if(node.startsWith(NODE_PREFIX))
            {
                if(!nodeInfo.containsKey(node))
                {
                    nodeInfo.put(node, 0);
                    nodeCosts.put(node,0.0);
                }
                val= nodeInfo.get(node);
                nodeInfo.put(node, val + 1);
                nodeCosts.put(node, nodeCosts.get(node) + q.getCost());
            }
        }

        DescriptiveStatistics stat = new DescriptiveStatistics();
        DescriptiveStatistics costStat = new DescriptiveStatistics();
        System.out.println("Query counts : ");
        for(String node: nodeInfo.keySet())
        {
            System.out.println(node + " : " + allInfo.get(node));
            stat.addValue(nodeInfo.get(node));
            costStat.addValue(nodeCosts.get(node));
        }


        System.out.println();
        double mean= stat.getMean();
        double stdDev = Math.sqrt(stat.getPopulationVariance());
        double varCoef = stdDev/mean;
        System.out.println("mean : " + mean);
        System.out.println("stdDev : " + stdDev);
        System.out.println("Coefficient of var : " + varCoef);


        System.out.println("\nCosts :");
        mean= costStat.getMean();
        stdDev = Math.sqrt(costStat.getPopulationVariance());
        varCoef = stdDev/mean;
        System.out.println("mean : " + mean);
        System.out.println("stdDev : " + stdDev);
        System.out.println("Coefficient of var : " + varCoef);
    }

    public List<String> generateNodeList(int count,boolean dispatcher)
    {
        String prefix = dispatcher? DISP_PREFIX : NODE_PREFIX;
        List<String> nodes = new ArrayList<String>();

        for(int i=1; i <= count; i++)
        {
            String newNode = prefix + i/10+ "." + i% 10;
            nodes.add(newNode);
        }

        return nodes;
    }

    public void start()
    {
        int seed = 999;
        double complexity= 4.1;

        int inputDefCount = 500, outputDefCount = 50;
        FilteredQueryGenerator qg = new FilteredQueryGenerator(seed,complexity);

        List<Integer> queryCounts = Arrays.asList(1000,5000,10000);
        Map<String,QueryDistributionAlgo> algos = new HashMap<String, QueryDistributionAlgo>();
        algos.put("Random", new RandomDistributionAlgo());
        algos.put("SCTXPF",new SCTXPFDistributionAlgo());
        algos.put("SCTXPF+", new SCTXPFPlusDistributionAlgo());

        List<Integer> nodeCounts = Arrays.asList(4,8,12);


        List<StreamDefinition> inDef,outDef;
        inDef= qg.generateDefinitions(inputDefCount,3,6);
        outDef = qg.generateDefinitions(inputDefCount,1,3);
        List<Query> queries;

        for(int queryCount : queryCounts) {
            queries = qg.generateQueries(queryCount,inDef,outDef);

            CostModelCalculator costCal = new CostModelCalculator();
            System.out.print("Calculating costs ");
            int cnt =0;
            for(Query q: queries)
            {
                cnt++;
                double cost = costCal.calculateCost(q);
                q.setCost(cost);
                if(cnt%100 == 0)
                {
                    System.out.print(".");
                }
            }
            System.out.println(" - done!");

            System.out.println( "************ " + queryCount +" queries ***********");
            for (int nodeCount : nodeCounts) {
                System.out.println("-----------" + nodeCount + " nodes --------------");
                for (String algoName : algos.keySet()) {
                    QueryDistributionAlgo algo = algos.get(algoName);

                    QueryDistributionParam param = new QueryDistributionParam();

                    param.setNodeList(generateNodeList(nodeCount, false));
                    param.setDispatcherList(generateNodeList(1, true));
                    param.setQueries(queries);
                    param.setNodeQueryTable(new HashMap<String, List<Query>>());

                    QueryDistribution dist = algo.getQueryDistribution(param);

                    System.out.println("-- " + algoName + " --");

                    EvaluateDistribution(dist);
                    System.out.println();
                }
            }
        }

    }

    public static  void main(String[] arg)
    {
        DistributionEval ev = new DistributionEval();
        ev.start();
    }
}
