package org.cse.visiri.algo.util;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.util.Utilization;

/**
 * Created by visiri on 10/31/14.
 */
public class UtilizationUpdater {


    public UtilizationUpdater(){

    }

    public void start(){
        Utilization util = new Utilization(10);
        Environment.getInstance().setNodeUtilizations(util);
    }

    public void stop(){

    }

}
