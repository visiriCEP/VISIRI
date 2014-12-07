package org.cse.visiri.app;

import org.cse.visiri.core.Dispatcher;

import java.util.Scanner;

/**
 * Created by visiri on 12/7/14.
 */
public class DispatcherNode {

    public static void main(String[] args){
        Dispatcher disp=new Dispatcher();

        disp.initialize();
        System.out.println("disp inited");
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
