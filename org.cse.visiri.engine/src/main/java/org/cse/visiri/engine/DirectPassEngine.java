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

import org.cse.visiri.util.Event;
import org.cse.visiri.util.Query;

import java.io.IOException;


public class DirectPassEngine extends CEPEngine {

    private Query query;
    private OutputEventReceiver outputEventReceiver;

    public DirectPassEngine(Query query,OutputEventReceiver outputEventReceiver){
        this.query=query;
        this.outputEventReceiver=outputEventReceiver;
        this.start();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Object saveState() {
        return null;
    }

    @Override
    public void sendEvent(Event event) {
        try {
            try {
                outputEventReceiver.sendEvents(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restoreEngine() {

    }
}
