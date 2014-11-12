package org.cse.visiri.core;

import junit.framework.TestCase;

import java.util.Scanner;

/**
 * Created by Geeth on 2014-11-11.
 */

public class DispatcherTest extends TestCase {

    Dispatcher disp;
    public void setUp() throws Exception {
        super.setUp();

    disp = new Dispatcher();

    }

    public void testStart() throws Exception {
        disp.initialize();
        System.out.println("disp inited");
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
