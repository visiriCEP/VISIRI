package org.cse.visiri.util.costmodelcalc;


import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.SiddhiQLGrammarLexer;
import org.wso2.siddhi.query.compiler.SiddhiQLGrammarParser;
import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;

import java.util.List;

/**
 * Created by lasitha on 11/13/14.
 */
public class SiddhiCostModelCalculator extends CostModelCalculator {
    private double maxCost=1000.0;

    //TODO normalize the cost values
    @Override
    public double calculateCost(Query q) {
        double cost=10.0;
        org.wso2.siddhi.query.api.query.Query siddhiQuery=SiddhiCompiler.parseQuery(q.getQuery());
        CommonTokenStream siddhiTokens= this.parseQueryTokens(q.getQuery());
        List<CommonToken> tokenList=siddhiTokens.getTokens();

        cost+=calcAttributeCost(q);
        cost+=calcFilteringCost(q.getQuery(), tokenList);
        cost+=calcStreamCountCost(siddhiQuery);
        cost+=calcTimeWindowCost(q.getQuery(), tokenList);
        return cost;
    }

    private static double calcAttributeCost(Query q){
        double cost=0.0;
        for(StreamDefinition d:q.getInputStreamDefinitionsList()){
            cost+=0.1*d.getAttributeList().size();
        }
        cost+=0.1*q.getOutputStreamDefinition().getAttributeList().size();
        return cost;
    }

    private static double calcFilteringCost(String query,List<CommonToken> tokenList){
        double cost=0.0;
        //TODO find a better way to get filtering queries
        for(CommonToken t:tokenList){
            if(t.getType()==88){//filter
                cost+=10.0;
                int windowIdx=tokenList.indexOf(t);
                while(tokenList.get(windowIdx).getType()!=89){
                    if(tokenList.get(windowIdx).getType()==21){
                        cost+=0.5;
                    }
                    windowIdx++;
                }
                break;
            }
        }
        return cost;
    }


    private static double calcTimeWindowCost(String query,List<CommonToken> tokenList){
        double cost=0.0;
        for(CommonToken t:tokenList){
            if(t.getType()==138){//time window
                cost+=100;
                int windowIdx=tokenList.indexOf(t);
                while(tokenList.get(windowIdx).getType()!=45){
                    windowIdx++;
                }
                System.out.println(query.substring(tokenList.get(windowIdx).getStartIndex(),tokenList.get(windowIdx).getStopIndex()+1));
                double windowVal=Integer.parseInt(query.substring(tokenList.get(windowIdx).getStartIndex(),tokenList.get(windowIdx).getStopIndex()+1));
                //windowVal/=10000;
                while(tokenList.get(windowIdx).getType()!=21){
                    if(tokenList.get(windowIdx).getType()==72){
                        windowIdx--;
                        break;
                    }
                    windowIdx++;
                }
                String timeUnit=query.substring(tokenList.get(windowIdx).getStartIndex(), tokenList.get(windowIdx).getStopIndex() + 1);
                double timeUnitVal=1;
                if (tokenList.get(windowIdx).getType()==21){
                    timeUnitVal=calcTimeUnitValue(timeUnit);
                }
                cost+=Math.pow(windowVal*timeUnitVal,2);
                //break;
            }
        }
        return cost;
    }

    private static double calcTimeUnitValue(String timeUnitString){
        double timeUnitVal=1.0;
        if("year".equals(timeUnitString)||"years".equals(timeUnitString)){
            timeUnitVal=365*24*60*60000;
        }
        else if("month".equals(timeUnitString)||"months".equals(timeUnitString)){
            timeUnitVal=30*24*60*60000;
        }else if("week".equals(timeUnitString)||"weeks".equals(timeUnitString)){
            timeUnitVal=7*24*60*60000;
        }else if("day".equals(timeUnitString)||"days".equals(timeUnitString)){
            timeUnitVal=24*60*60000;
        }else if("hour".equals(timeUnitString)||"hours".equals(timeUnitString)){
            timeUnitVal=60*60000;
        }else if("minute".equals(timeUnitString)||"minutes".equals(timeUnitString)||"min".equals(timeUnitString)){
            timeUnitVal=1*60000;
        }else if("second".equals(timeUnitString)||"seconds".equals(timeUnitString)||"sec".equals(timeUnitString)){
            timeUnitVal=1000;
        }else if("millisecond".equals(timeUnitString)||"milliseconds".equals(timeUnitString)){
            timeUnitVal=1;
        }
        return timeUnitVal;
    }
    private static double calcStreamCountCost(org.wso2.siddhi.query.api.query.Query siddhiQuery){
        double cost=0.0;
        cost+=siddhiQuery.getInputStream().getStreamIds().size();
        if(siddhiQuery.getOutputStream()!=null) {
            cost++;
        }
        return cost;
    }

    private static CommonTokenStream parseQueryTokens(String source) throws SiddhiParserException {
        try {
            SiddhiQLGrammarLexer lexer = new SiddhiQLGrammarLexer();
            lexer.setCharStream(new ANTLRStringStream(source));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SiddhiQLGrammarParser parser = new SiddhiQLGrammarParser(tokens);


            SiddhiQLGrammarParser.queryFinal_return r = parser.queryFinal();
            CommonTree t = (CommonTree) r.getTree();

            CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
            nodes.setTokenStream(tokens);

            //SiddhiQLGrammarWalker walker = new SiddhiQLGrammarWalker(nodes);
            //return walker.queryFinal();
            return tokens;
        } catch (Throwable e) {
            throw new SiddhiParserException(e.getMessage(), e);
        }
    }
}
