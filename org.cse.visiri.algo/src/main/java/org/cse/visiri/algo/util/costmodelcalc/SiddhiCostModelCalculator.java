package org.cse.visiri.algo.util.costmodelcalc;

import antlr.TokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.cse.visiri.util.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.SiddhiQLGrammarLexer;
import org.wso2.siddhi.query.compiler.SiddhiQLGrammarParser;
import org.wso2.siddhi.query.compiler.SiddhiQLGrammarWalker;
import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;

import java.util.List;

/**
 * Created by lasitha on 11/13/14.
 */
public class SiddhiCostModelCalculator extends CostModelCalculator {

    @Override
    public double calculateCost(Query q) {
        double cost=10.0;
        org.wso2.siddhi.query.api.query.Query siddhiQuery=SiddhiCompiler.parseQuery(q.getQuery());
        CommonTokenStream siddhiTokens= this.parseQueryTokens(q.getQuery());
        List<CommonToken> tokenList=siddhiTokens.getTokens();
        cost+=getFilteringCost(q.getQuery(),tokenList);
        cost+=getStreamCountCost(siddhiQuery);
        cost+=getTimeWindowCost(q.getQuery(),tokenList);
        return cost;
    }

    private static double getFilteringCost(String query,List<CommonToken> tokenList){
        double cost=0;

        return cost;
    }


    private static double getTimeWindowCost(String query,List<CommonToken> tokenList){
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
                cost+=Math.pow(2,windowVal/100);
                break;
            }
        }
        return cost;
    }

    private static double getStreamCountCost(org.wso2.siddhi.query.api.query.Query siddhiQuery){
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
