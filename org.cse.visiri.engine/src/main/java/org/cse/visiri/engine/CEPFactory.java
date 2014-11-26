package org.cse.visiri.engine;

import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
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
