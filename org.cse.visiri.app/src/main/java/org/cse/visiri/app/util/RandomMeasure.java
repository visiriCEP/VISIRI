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

package org.cse.visiri.app.util;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class RandomMeasure {

    private List<StreamDefinition> in,out;
    private  List<Query> queries;

    final int inAttrCntMin = 3, inAttrCntMax= 5;
    final int outAttrCntMin = 1, outAttrCntMax= 3;
    //final int inDefCount = 500, outDefCount = 50,queryCount=4000;
    public RandomMeasure()
    {
        final int seed = 111;
        final double filteringLevel = 3;
        final int queryType =2;
        int inDefCount = 100, outDefCount =20,queryCount=500;

        RandomQueryGenerator qg ;
        qg = new FilteredQueryGenerator(seed,filteringLevel,queryType);

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
