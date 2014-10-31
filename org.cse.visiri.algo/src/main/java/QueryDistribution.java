import org.cse.visiri.util.Query;

import java.util.HashMap;
import java.util.List;

/**
 * Created by visiri on 10/31/14.
 */
public class QueryDistribution {

    private HashMap<Query,String> queryToNodeMap;
    private HashMap<Query,List<Query>> originalToGeneratedQuery;

    public QueryDistribution(){
        this.queryToNodeMap=new HashMap<Query, String>();
        this.originalToGeneratedQuery=new HashMap<Query, List<Query>>();
    }

    public void addQueryToNode(Query query,String nodeIp){
        this.getQueryToNodeMap().put(query, nodeIp);
    }

    public void addGeneratedQueryToOriginal(Query query,List<Query> queryList){
        this.getOriginalToGeneratedQuery().put(query, queryList);
    }


    public HashMap<Query, String> getQueryToNodeMap() {
        return queryToNodeMap;
    }

    public HashMap<Query, List<Query>> getOriginalToGeneratedQuery() {
        return originalToGeneratedQuery;
    }
}
