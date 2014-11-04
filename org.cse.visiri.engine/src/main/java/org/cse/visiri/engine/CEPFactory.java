package org.cse.visiri.engine;

import org.cse.visiri.util.Query;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
public class CEPFactory {

    public CEPFactory(){}

    public static CEPEngine createEngine(int  type,Query q){

        if(type==CEPEngine.ENGINE_TYPE_SIDDHI){
            return new SiddhiCEPEngine(q);
        }
        throw new UnsupportedOperationException();
    }
    public CEPEngine restoreEngine(){
        throw new UnsupportedOperationException();
    }
}
