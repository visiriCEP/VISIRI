package org.cse.visiri.app;

import junit.framework.TestCase;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Dispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DispatcherNodeTest extends TestCase {

    public void testDispatcherBuffering() throws InterruptedException {
        final Dispatcher disp=new Dispatcher();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                // code goes here.
                try {
                    disp.initialize();
                    System.out.println("disp inited");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        t1.start();
        Thread.sleep(1000);

        List<String> tmpQ=new LinkedList<String>();
        tmpQ.add("car");
//        server.bufferingStart(tmpQ);
        Environment.getInstance().getBufferingEventList().add("stream1");
        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_BUFFERING_START);

        System.out.println("timer started");
        Thread.sleep(20000);
        System.out.println("timeout");
        //tmpQ.remove("fire");
//        server.bufferingStop();
        Environment.getInstance().sendEvent(Environment.EVENT_TYPE_BUFFERING_STOP);


        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}