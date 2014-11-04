package org.cse.visiri.engineTest;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by visiri on 11/3/14.
 */
public class EventHandlerTest {

    public static void main(String[] args){

        HashMap<String,Integer> hashMap=new HashMap<String, Integer>();

        hashMap.put("one",1);
        hashMap.put("two",2);
        hashMap.put("three",3);

        Set set=hashMap.keySet();
        String s= (String) set.iterator().next();
        System.out.println(s);
    }
}
