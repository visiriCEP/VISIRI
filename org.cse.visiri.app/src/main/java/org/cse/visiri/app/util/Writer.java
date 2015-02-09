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

import java.io.*;
import java.util.Date;


public class Writer {

    int count=0,period;
    long startTime = 0;

    public static void main(String[] args){
       Writer writeToFile=new Writer(0);
       writeToFile.write();
    }

    public Writer(int period){
        this.period=period;
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("VISIRI_results.txt", true)));
            Date d = new Date();
            out.println(d.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(){

        if(count % period == 0){
            if(count == 0)
            {
                startTime = System.currentTimeMillis();
            }
            try {
                String time = Long.toString(System.currentTimeMillis()-startTime);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("VISIRI_results.txt", true)));
                out.print(time);
                out.println(" : " + count);
                out.close();
                System.out.println("Received :"+count);
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
        count++;
    }
}
