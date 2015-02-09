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

package org.cse.visiri.util;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class TransferbleEngine {
    private Map<String,List<Query>> nodeTransferbleQueryMap;
    private Set<StreamDefinition> completlyRemovedEvents;


    public TransferbleEngine(Map<String,List<Query>> nodeTransferbleQueryMap,Set<StreamDefinition> completlyRemovedEvents){
        this.nodeTransferbleQueryMap=nodeTransferbleQueryMap;
        this.completlyRemovedEvents=completlyRemovedEvents;
    }


    public Map<String,List<Query>> getNodeTransferbleQueryMap(){
        return nodeTransferbleQueryMap;
    }

    public Set<StreamDefinition> getCompletlyRemovedEvents(){
        return completlyRemovedEvents;
    }

}
