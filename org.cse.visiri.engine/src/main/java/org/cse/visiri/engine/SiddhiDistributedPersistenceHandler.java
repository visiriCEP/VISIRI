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

package org.cse.visiri.engine;

import org.cse.visiri.communication.Environment;
import org.wso2.siddhi.core.persistence.PersistenceStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SiddhiDistributedPersistenceHandler implements PersistenceStore {

    //HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(new Config().setInstanceName(UUID.randomUUID().toString()));
    Map<String, Map<String, byte[]>> persistenceMap = Environment.getInstance().getPersistenceMapping();//new HashMap<String, Map<String, byte[]>>();//hazelcastInstance.getMap("persistenceMap");
    Map<String, List<String>> revisionMap = Environment.getInstance().getRevisionMapping();//new HashMap<String, List<String>>();//hazelcastInstance.getMap("revisionMap");


    @Override
    public void save(String queryPlanIdentifier, String revision, byte[] data) {
        Map<String, byte[]> executionPersistenceMap = persistenceMap.get(queryPlanIdentifier);
        if (executionPersistenceMap == null) {
            executionPersistenceMap = new HashMap<String, byte[]>();
        }

        executionPersistenceMap.put(revision, data);


        List<String> revisionList = revisionMap.get(queryPlanIdentifier);
        if (revisionList == null) {
            revisionList = new ArrayList<String>();
            revisionMap.put(queryPlanIdentifier, revisionList);
        }
        if (revisionList.size() == 0 || (revisionList.size() > 0 && !revision.equals(revisionList.get(revisionList.size() - 1)))) {
            revisionList.add(revision);
            revisionMap.put(queryPlanIdentifier, revisionList);
        }
        persistenceMap.put(queryPlanIdentifier, executionPersistenceMap);


    }

    @Override
    public byte[] load(String queryPlanIdentifier, String revision) {


        Map<String, byte[]> executionPersistenceMap = persistenceMap.get(queryPlanIdentifier);
        if (executionPersistenceMap == null) {
            System.out.println("Data not found for the execution plan " + queryPlanIdentifier);
            return null;
        }
        return executionPersistenceMap.get(revision);
    }

    @Override
    public String getLastRevision(String executionPlanIdentifier) {
        List<String> revisionList = revisionMap.get(executionPlanIdentifier);
        if (revisionList == null) {
            return null;
        }
        if (revisionList.size() > 0) {
            return revisionList.get(revisionList.size() - 1);
        }
        return null;
    }

    public void shutdown() {
        //hazelcastInstance.getLifecycleService().shutdown();
    }
}
