package org.cse.visiri.algo;

/**
 * Created by visiri on 11/26/14.
 */
public class AlgoFactory {

    public AlgoFactory(){};

    public static QueryDistributionAlgo createAlgorithm(int type){

            if(type==QueryDistributionAlgo.SCTXPF_ALGO){
                return new SCTXPFDistributionAlgo();
            }
            if(type==QueryDistributionAlgo.SCTXPF_PLUS_ALGO){
                return new SCTXPFPlusDistributionAlgo();
            }
            else if(type==QueryDistributionAlgo.ROUNDROBIN_ALGO){
                return new RoundRobinDistributionAlgo();
            }else if(type==QueryDistributionAlgo.RANDOM_DISTRIBUTOR_ALGO){
                return new RandomDistributionAlgo();
            }

        throw new UnsupportedOperationException();
    }
}
