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

package org.cse.visiri.app.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
