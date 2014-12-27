package org.cse.visiri.app.util;

import org.apache.commons.lang3.StringUtils;
import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Geeth on 2014-12-27.
 */
public class FilteredQueryGenerator extends RandomQueryGenerator{

    private int complexity = 2;
    public  FilteredQueryGenerator(int seed)
    {
        super(seed);
    }
    public  FilteredQueryGenerator(int seed,int complexity)
    {
        super(seed);
        this.complexity = complexity;
    }
    @Override
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
                String conds = generateMultipleConditions(inAttrs,2*complexity);
                varCondition = "[" + conds + "]";

                List<String> inps = new ArrayList<String>();
                for(int i=0; i < outAttrs.size(); i++)
                {
                    int attrPos =i;
                    String attr = inAttrs.get(attrPos).getName();
                    inps.add(attr);
                }
                varInAttr= StringUtils.join(inps, ",");
                break;
            }
            case 1: //filter 2
            {
                String cond = generateMultipleConditions(inAttrs,4*complexity);

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
            {
                String type = randomizer.nextFloat() < 0.80 ? "lengthBatch" : "length";
                int batchCount = 5 + randomizer.nextInt(200*complexity);
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


            case 3:
            case 4: //window +  filter
            {
                String cond = generateMultipleConditions(inAttrs,4);
                varCondition = "[" + cond + "]";
                String type = randomizer.nextFloat() < 0.75 ? "lengthBatch" : "length";
                int batchCount = 5 + randomizer.nextInt(200*complexity);
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
}
