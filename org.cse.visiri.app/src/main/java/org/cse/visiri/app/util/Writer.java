package org.cse.visiri.app.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Malinda Kumarasinghe on 12/6/2014.
 */
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

        if(count%period == 0){
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
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
        count++;
    }
}
