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


public class Configuration {

    private static int nodeType;
    public final static int INSTANT_EVENT_COUNT=100;
    public final static int AVERAGE_EVENT_COUNT=2000;
    public final static int MAX_EVENT_RATE=5000;
    public final static int MAX_EVENT_RATE_GAP=500;
    public final static int AGENT_UPDATE_PERIOD=8000;
    public final static Double UTILIZATION_THRESHOULD=90.0;
    public final static int ENGINE_TYPE_DIRECT=0;
    public final static int ENGINE_TYPE_SIDDHI=1;

    public final static int EVENT_RATE_FREQ=500000;


    public static int getNodeType() {
        return nodeType;
    }

    public static void setNodeType(int nodeType) {
        Configuration.nodeType = nodeType;
    }
}
