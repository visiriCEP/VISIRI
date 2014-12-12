package org.cse.visiri.app;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Node;

import java.util.Scanner;

/**
 * Created by Geeth on 2014-12-07.
 */
public class EmptyNode {


    public static void main(String[] args) throws Exception {
        Environment environment;
        Evaluation evaluation=new Evaluation();
        Node node=new Node();

        node.initialize();

        Scanner sc = new Scanner(System.in);


        System.out.println("Started");
        sc.next();
    }
}
