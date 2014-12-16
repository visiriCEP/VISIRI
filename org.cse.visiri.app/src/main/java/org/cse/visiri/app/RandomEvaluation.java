package org.cse.visiri.app;

import org.cse.visiri.app.util.RandomQueryGenerator;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.List;

/**
 * Created by visiri on 12/6/14.
 */
public class RandomEvaluation {

    private List<StreamDefinition> in,out;
    private  List<Query> queries;
    public RandomEvaluation()
    {
        final int seed = 12;

        RandomQueryGenerator qg = new RandomQueryGenerator(seed);
        final int inDefCount = 30, outDefCount = 50,queryCount=2000;
       // final int inDefCount = 1, outDefCount = 1,queryCount=1;
        final int inAttrCntMin = 3, inAttrCntMax= 5;
        final int outAttrCntMin = 1, outAttrCntMax= 3;

        in= qg.generateDefinitions(inDefCount,inAttrCntMin,inAttrCntMax);
        out = qg.generateDefinitions(outDefCount,outAttrCntMin,outAttrCntMax);
        queries = qg.generateQueries(queryCount,in,out);
    }

    public List<StreamDefinition> getInputDefinitions()
    {
        return in;
    }

    public List<StreamDefinition> getOutputDefinitions()
    {
        return out;
    }

    public List<Query> getQueries(){

        return queries;
    }
}
