package org.cse.visiri.app.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by lasitha on 12/29/14.
 */
public class Reader {


    public static Map<String,Integer>  readConfig(){
        BufferedReader br = null;
        Map<String,Integer> config=new HashMap<String,Integer>();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("VISIRI_CONFIG.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                String key,val;
                key=sCurrentLine.split("=")[0];
                val=sCurrentLine.split("=")[1];

                config.put(key,Integer.parseInt(val));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return config;
    }
}
