package org.cse.visiri.app;

import org.cse.visiri.app.util.FilteredQueryGenerator;
import org.cse.visiri.app.util.RandomQueryGenerator;
import org.cse.visiri.app.util.Reader;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.HashSet;
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
        final double filteringLevel = 4.0;

        int inDefCount = Reader.readConfig().get("input_def_count"), outDefCount = Reader.readConfig().get("output_def_count"),queryCount=Reader.readConfig().get("query_count");

        RandomQueryGenerator qg ;
        qg = new FilteredQueryGenerator(seed,filteringLevel);
        //final int inDefCount = 500, outDefCount = 50,queryCount=4000;

        final int inAttrCntMin = 3, inAttrCntMax= 10;
        final int outAttrCntMin = 1, outAttrCntMax= 3;

        in= qg.generateDefinitions(inDefCount,inAttrCntMin,inAttrCntMax);
        out = qg.generateDefinitions(outDefCount,outAttrCntMin,outAttrCntMax);
        queries = qg.generateQueries(queryCount,in,out);
    }

    public List<StreamDefinition> getInputDefinitions()
    {
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();
        HashSet<String> ids = new HashSet<String>();
        for(Query q : queries)
        {
            for(StreamDefinition def : q.getInputStreamDefinitionsList())
            {
                String id = def.getStreamId();
                if(!ids.contains(id))
                {
                    ids.add(id);
                    defs.add(def);
                }
            }
        }
        return defs;
    }

    public List<StreamDefinition> getOutputDefinitions()
    {
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();
        HashSet<String> ids = new HashSet<String>();
        for(Query q : queries)
        {
            StreamDefinition def = q.getOutputStreamDefinition();

            String id = def.getStreamId();
            if(!ids.contains(id))
            {
                ids.add(id);
                defs.add(def);
            }

        }
        return defs;
    }

    public List<Query> getQueries(){

        return queries;
    }
}
