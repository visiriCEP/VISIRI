package org.cse.visiri.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Malinda Kumarasinghe on 12/6/2014.
 */
public class Writer {

    int count=0,period;

    public static void main(String[] args){
       Writer writeToFile=new Writer(0);
       writeToFile.write();
    }

    public Writer(int period){
        this.period=period;
    }

    public void write(){

        if(period!=count){
            count++;
        }
        else {

            count=0;
            try {
                String time = Long.toString(System.currentTimeMillis());
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("VISIRI_results.txt", true)));
                out.println(time);
                out.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }
}
