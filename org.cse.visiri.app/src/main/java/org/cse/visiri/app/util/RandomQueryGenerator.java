package org.cse.visiri.app.util;

import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Geeth on 2014-12-11.
 */
public class RandomQueryGenerator {

    private int _lastAttrID = 1;
    private int _lastStreamID = 1;

    private Random randomizer;

    private String newStreamName()
    {
        return "stream"+ (_lastStreamID++);
    }
    private String newAttributeName()
    {
        return "attr" + (_lastAttrID++);
    }

    public RandomQueryGenerator(int seed)
    {
        randomizer = new Random(seed);
    }

    public List<StreamDefinition> generateDefinitions(int defCount,int queryAttrMin, int queryAttrMax)
    {
        List<StreamDefinition> defs = new ArrayList<StreamDefinition>();

        for(int defIdx =0 ; defIdx < defCount ; defIdx++ )
        {
            StreamDefinition def = new StreamDefinition();
            def.setStreamId(newStreamName());
            int attrCount = queryAttrMin+randomizer.nextInt(queryAttrMax-queryAttrMin + 1);
            for(int attrId= 0 ; attrId< attrCount ; attrId++)
            {
                def.addAttribute(newAttributeName(), StreamDefinition.Type.FLOAT);
            }

            defs.add(def);
        }

        return defs;
    }

//    public List<Query> generateQueries(int queryCount)
//    {
//        List<Query> allQueries= new ArrayList<Query>(queryCount);
//
//        for(int qIndex = 0; qIndex < queryCount ; qIndex++)
//        {
//
//        }
//
//        return null;
//    }

}
