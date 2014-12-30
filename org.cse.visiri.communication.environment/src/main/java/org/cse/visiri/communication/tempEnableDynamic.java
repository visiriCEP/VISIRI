package org.cse.visiri.communication;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Created by Malinda Kumarasinghe on 12/30/2014.
 */
public class tempEnableDynamic {
    public static  void main(String[] args){
      // HazelcastInstance hz = Hazelcast.newHazelcastInstance();
Environment.getInstance().addDynamic();
        System.out.println("Done.");
    }
}
