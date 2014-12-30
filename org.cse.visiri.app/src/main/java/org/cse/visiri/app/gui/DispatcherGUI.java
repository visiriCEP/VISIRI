package org.cse.visiri.app.gui;

import org.cse.visiri.core.Dispatcher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lasitha on 12/30/14.
 */
public class DispatcherGUI {
    private JLabel dispatcherNodeLabel;
    private JButton stopButton;
    private JPanel jPanel;
    private JButton startButton;
    private JLabel avgRateLbl;


    Dispatcher disp=new Dispatcher();


    public DispatcherGUI() {
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatcherNodeLabel.setText("Stoped");
                disp.stop();
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disp.initialize();
                System.out.println("disp inited");
                dispatcherNodeLabel.setText("Started");
            }
        });

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                // code goes here.
                int count=0;
                while(true){
                    avgRateLbl.setText(""+count++);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DispatcherGUI");
        frame.setContentPane(new DispatcherGUI().jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
