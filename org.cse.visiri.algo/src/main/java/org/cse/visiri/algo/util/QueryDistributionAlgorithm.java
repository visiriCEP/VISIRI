package org.cse.visiri.algo.util;

import org.cse.visiri.util.QueryDistribution;
import org.cse.visiri.util.Query;

import java.util.List;

/**
 * Created by visiri on 10/31/14.
 */
public class QueryDistributionAlgorithm {

    private List<Query> queryList;
    private QueryDistribution queryDistribution;

    public QueryDistributionAlgorithm(List<Query> queryList){
        this.queryList=queryList;
    }

    public void addQuery(Query query){
        this.queryList.add(query);
    }

    public void distributeQueries(){

    }

    public QueryDistribution getQueryDistribution(){
        return queryDistribution;
    }

    public List<Query> getQueryList(){
        return queryList;
    }


}
