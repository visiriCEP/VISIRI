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

package org.cse.visiri.communication;

import org.cse.visiri.util.Query;

import java.io.Serializable;


public class MessageObject implements Serializable {
    private int eventType;
    private Query query;
    private String destination;
    private String from;

    public MessageObject(int eventType,Query query,String from,String destination){
        this.from=from;
        this.eventType=eventType;
        this.query=query;
        this.destination=destination;
    }

    public MessageObject(int eventType,String from){
        this.eventType=eventType;
        this.from=from;

    }

    public int getEventType() {
        return eventType;
    }
    public String getFrom(){
        return this.from;
    }

    public Query getPersistedEngine() {
        return query;
    }
    public String getDestination(){
        return destination;
    }

}
