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

import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;


public class CEPFactory {

    public CEPFactory(){}

    public static CEPEngine createEngine(int  type,Query q,OutputEventReceiver outputEventReceiver){

        if(type== Configuration.ENGINE_TYPE_SIDDHI){
            return new SiddhiCEPEngine(q,outputEventReceiver);
        }
        else if(type == Configuration.ENGINE_TYPE_DIRECT)
        {
            return new DirectPassEngine(q,outputEventReceiver);
        }
        throw new UnsupportedOperationException();
    }
    public CEPEngine restoreEngine(){
        throw new UnsupportedOperationException();
    }
}
