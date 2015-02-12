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

package org.cse.visiri.util.costmodelcalc;

import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FastSiddhiCostModelCalculator extends SiddhiCostModelCalculator {

    Pattern filterPattern, windowPattern;

    public FastSiddhiCostModelCalculator()
    {
        filterPattern = Pattern.compile("[<>]");
        windowPattern = Pattern.compile("#window\\.[a-zA-Z]+\\(\\s*([0-9]+).*\\)");
    }

    @Override
    public double calculateCost(Query q) {
        double cost=10.0;

        cost += calculateAttributeCost(q);
        cost+=calculateFilteringCost(q);
        cost+=calcStreamCountCost(q);
        cost+=calcWindowCost(q);

        cost=Math.log(cost);
        return cost;
    }

    protected double calculateAttributeCost(Query q)
    {
        Set<StreamDefinition> definitions = new HashSet<StreamDefinition>(q.getInputStreamDefinitionsList());
        definitions.add(q.getOutputStreamDefinition());
        double cost = 0.0;
        for(StreamDefinition sd: definitions)
        {
            cost+=  0.1 *sd.getAttributeList().size();
        }

        return cost;
    }

    protected double calculateFilteringCost(Query q)
    {
        String query = q.getQuery();

        int filterCount =0;
        Matcher matcher = filterPattern.matcher(query);
        while(matcher.find()) filterCount++;

        double cost =0;
        if(filterCount > 0)
        {
            cost = 10.0 + (filterCount * 0.5);
        }

        return cost;
    }

    protected double calcWindowCost(Query q)
    {
        String query = q.getQuery();
        Matcher matcher = windowPattern.matcher(query);

        if(matcher.find())
        {
            String num = matcher.group(1).trim();
            int windowSize = Integer.parseInt(num);

            double cost = 100 + Math.pow(windowSize,1.5);
            return cost;
        }
        else
        {
            return 0;
        }
    }

    protected double calcStreamCountCost(Query q)
    {
        int streams = q.getInputStreamDefinitionsList().size() + 1;
        return  (double) streams;
    }
}
