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

import org.apache.commons.lang3.StringUtils;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomQueryGenerator {

    private int _lastAttrID = 1;
    private int _lastStreamID = 1;
    private int _lastQueryID = 1;

    protected Random randomizer;

    protected String newStreamName()
    {
        return "stream"+ (_lastStreamID++);
    }
    protected String newQueryID()
    {
        return "query"+ (_lastQueryID++);
    }
    protected String newAttributeName()
    {
        return "attr" + (_lastAttrID++);
    }

    protected String newCondition(List<StreamDefinition.Attribute> attrs)
    {
        int attrIdx =  randomizer.nextInt(attrs.size());
        String attrName = attrs.get(attrIdx).getName();
        String op = randomizer.nextBoolean() ? ">" : "<";
        float value = randomizer.nextFloat() * 100;
        String valStr = new DecimalFormat("#0.0#").format(value);
        String cond = String.format("%s %s %s",attrName,op,valStr);

        return cond;
    }

    protected String generateMultipleConditions(List<StreamDefinition.Attribute> attrs,
                                                int count)
    {
        String[] conds = new String[count];
        for(int i=0; i < count ; i++)
        {
            conds[i] = newCondition(attrs);
        }
        String complete = StringUtils.join(conds," and ");
        return complete;
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
            for(int attrId= 1 ; attrId<= attrCount ; attrId++)
            {
                def.addAttribute("attr" + attrId, StreamDefinition.Type.FLOAT);
            }

            defs.add(def);
        }

        return defs;
    }

    protected Query generateQuery(List<StreamDefinition> inputs, List<StreamDefinition> outputs)
    {

        int inputIndex= randomizer.nextInt(inputs.size());
        int outputIndex = randomizer.nextInt(outputs.size());

        StreamDefinition inputDef= inputs.get(inputIndex);
        StreamDefinition outputDef =outputs.get(outputIndex);

        int numTypes = 5;
        //Query types
        // 0: filter 1
        // 1: filter 2
        // 2: window
        // 3 4: window + filter 1

        int queryType = randomizer.nextInt(numTypes);


        String template = "from %s%s%s " +
                " select %s " +
                " insert into %s ";

        String varInput, varCondition,varWindow, varInAttr, varOutput;
        varInput=varCondition=varWindow= varInAttr= varOutput="";

        varInput = inputDef.getStreamId();
        varOutput = outputDef.getStreamId();

        List<StreamDefinition.Attribute> inAttrs = inputDef.getAttributeList();
        List<StreamDefinition.Attribute> outAttrs = outputDef.getAttributeList();

        switch (queryType)
        {
            case 0: //filter 1
            {
                String conds = generateMultipleConditions(inAttrs,1);
                varCondition = "[" + conds + "]";

                List<String> inps = new ArrayList<String>();
                for(int i=0; i < outAttrs.size(); i++)
                {
                    int attrPos =i;
                    String attr = inAttrs.get(attrPos).getName();
                    inps.add(attr);
                }
                varInAttr= StringUtils.join(inps,",");
                break;
            }
            case 1: //filter 2
            {
                String cond = generateMultipleConditions(inAttrs, 2);

                varCondition = "[" + cond + "]";

                List<String> inps = new ArrayList<String>();
                for (int i = 0; i < outAttrs.size(); i++) {
                    int attrPos = i;
                    String attr = inAttrs.get(attrPos).getName();
                    inps.add(attr);
                }
                varInAttr = StringUtils.join(inps, ",");
                break;
            }
            case 2: //window
            case 3:
            {
                String type = randomizer.nextBoolean() ? "lengthBatch" : "length";
                int batchCount = 5 + randomizer.nextInt(10*1000);
                varWindow="#window."+type+"("+batchCount+") ";

                List<String> inps = new ArrayList<String>();
                for (int i = 0; i < outAttrs.size(); i++) {
                    int attrPos = i;
                    String attr = " max(" + inAttrs.get(attrPos).getName() +
                            ") as " +inAttrs.get(attrPos).getName();
                    inps.add(attr);
                }
                varInAttr = StringUtils.join(inps, ",");
                break;
            }

            case 4: //window +  filter
            {
                String cond = generateMultipleConditions(inAttrs, 1);
                varCondition = "[" + cond + "]";
                String type = randomizer.nextBoolean() ? "lengthBatch" : "length";
                int batchCount = 5 + randomizer.nextInt(5000);
                varWindow="#window."+type+"("+batchCount+") ";

                List<String> inps = new ArrayList<String>();

                for (int i = 0; i < outAttrs.size(); i++) {
                    int attrPos = i;
                    String attr = " max(" + inAttrs.get(attrPos).getName() +
                            ") as " +inAttrs.get(attrPos).getName();
                    inps.add(attr);
                }
                varInAttr= StringUtils.join(inps,",");
                break;
            }

        }

        String queryString = String.format(template,varInput, varCondition,varWindow, varInAttr, varOutput);
        List<StreamDefinition> inputDefList = Arrays.asList(inputDef);

        Query query = new Query( queryString,inputDefList,outputDef,newQueryID(), Configuration.ENGINE_TYPE_SIDDHI);
        return query;
    }

    public List<Query> generateQueries(int queryCount,List<StreamDefinition> inputs, List<StreamDefinition> outputs)
    {
        System.out.print("Generating " + queryCount + " queries ...");
        List<Query> allQueries= new ArrayList<Query>(queryCount);

        for(int qIndex = 0; qIndex < queryCount ; qIndex++)
        {
            Query query = generateQuery(inputs,outputs);
            allQueries.add(query);
        }
        System.out.println(" Done!");
        return allQueries;
    }

}
