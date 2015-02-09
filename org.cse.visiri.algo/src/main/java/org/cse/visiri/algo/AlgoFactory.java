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
package org.cse.visiri.algo;


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
