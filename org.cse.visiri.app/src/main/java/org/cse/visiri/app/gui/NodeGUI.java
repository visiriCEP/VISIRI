package org.cse.visiri.app.gui;

//import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import org.cse.visiri.algo.util.UtilizationUpdater;
import org.cse.visiri.app.RandomEvaluation;
import org.cse.visiri.app.gui.chartpanel.ChartFrame;
import org.cse.visiri.communication.Environment;
import org.cse.visiri.communication.eventserver.server.EventServer;
import org.cse.visiri.core.Dispatcher;
import org.cse.visiri.core.GUICallback;
import org.cse.visiri.core.Node;
import org.cse.visiri.util.EventRateStore;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;
import org.cse.visiri.util.Utilization;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


/**
 * Created by lasitha on 1/15/15.
 */
public class NodeGUI implements GUICallback {
    private byte runningMode;   //0-no mode
                                //1-dispatcher
                                //2-empty node
                                //3-random evaluation node

    private JComboBox selectionComboBox;
    private JButton startButton;
    private JButton stopButton;
    private JPanel mainPanel;
    private JPanel selectedPanel;
    private JPanel dispatcherPanel;
    private JPanel nodePanel;
    private JList queryList;
    private JLabel ipAddressLabel;
    private JPanel throughputChartLoadPanel;
    private JScrollPane dispatcherTableScrollPane;
    private JScrollPane processorTableScrollPane;

    private JTabbedPane tabbedGraphPanel;
    private JProgressBar cpuProgressBar;
    private JProgressBar memoryProgressBar;
    private JProgressBar networkProgressBar;
    private JLabel cpuPresentageLabel;
    private JLabel memoryPresentageLabel;
    private JLabel networkPresentageLabel;
    private JPanel memoryChartPanel;
    private JPanel dispatherTabelPanel;
    private JPanel processorTabelPanel;
    private JComboBox nodeTypeComboBox;
    private JButton startProcessingNodeButton;
    private JLabel statusLabel;
    private JComboBox bufferingModeComboBox;
    private JButton dispatherStartButton;


    private ChartFrame throughputChartFrame;
    private ChartFrame memoryChartFrame;

    private Node node;
    private Dispatcher dispatcher;

    private boolean guiCallbackSet;

    public NodeGUI() {

        dispatcherPanel.setVisible(false);
        nodePanel.setVisible(false);

        tabbedGraphPanel.setVisible(false);
        throughputChartFrame =new ChartFrame("Throughput");
        memoryChartFrame=new ChartFrame("Memory",100);

        throughputChartLoadPanel.setLayout(new GridLayout());
        throughputChartLoadPanel.add(throughputChartFrame.getContentPanel());
        throughputChartLoadPanel.repaint();

        memoryChartPanel.setLayout(new GridLayout());
        memoryChartPanel.add(memoryChartFrame.getContentPanel());
        memoryChartPanel.repaint();
//
        Thread t=new Thread(new Runnable() {
            public void run()
            {
                while (true) {
                    double memory=0;
                    double throughput=0;
                    double cpu=0;
                    double network=0;
                    UtilizationUpdater uu=null;
                    EventRateStore rs=null;
                    if(runningMode==1){
                        if(dispatcher!=null) {
                            uu = dispatcher.getUtilizationUpdater();
                            if(dispatcher.getEngineHandler()!=null){
                                rs=dispatcher.getEngineHandler().getEventRateStore();
                            }
                        }

                    }else if(runningMode==2 || runningMode==3){
                        if(node!=null){
                            uu=node.getUtilizationUpdater();
                            if(node.getEngineHandler()!=null){
                                rs=node.getEngineHandler().getEventRateStore();
                            }
                        }
                    }

                    try {
                        Utilization u=uu.update();
                        memory = 100-u.getFreeMemoryPercentage();
                        cpu=u.getJVMCpuUtilization();
                        throughput=rs.getAverageRate();
                    }catch(NullPointerException e){

                    }
                    throughputChartFrame.refreshChart(throughput);
                    memoryChartFrame.refreshChart(memory);
                    setUtilizationValues((int)cpu,(int)memory,(int)network);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatusLabel("Starting...");
                if (selectionComboBox.getSelectedIndex() == 0) {
                    dispatcherPanel.setVisible(false);
                    nodePanel.setVisible(true);
                    runningMode=2;

                    //ipAddressLabel.setText(Environment.getInstance().getNodeId());
                } else {
                    dispatcherPanel.setVisible(true);
                    nodePanel.setVisible(false);
                    //drawDispatcherTable(dispatcher.getQueries());
                    runningMode=1;

                }
                tabbedGraphPanel.setVisible(true);
                selectionComboBox.setEnabled(false);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        startProcessingNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(nodeTypeComboBox.getSelectedIndex()==0){
                    node=new Node();
                    setGUICallback();
                    node.initialize();
                    runningMode=2;
                    System.out.println("Node Started");
                    setStatusLabel("Node started");
                }else{
                    node=new Node();
                    setGUICallback();
                    node.initialize();
                    RandomEvaluation ev = new RandomEvaluation();
                    java.util.List<Query> queryList= ev.getQueries();
                    node.addQueries(queryList);
                    HashMap<String,StreamDefinition> subscribeMap=new HashMap<String, StreamDefinition>();

                    for(Query query:queryList){
                        subscribeMap.put(query.getOutputStreamDefinition().getStreamId(),query.getOutputStreamDefinition());
                    }

                    Set<String> outputSet=subscribeMap.keySet();

                    for(String outputStream:outputSet){
                        //node.subscribeToStream(outputStream,sinkIp+":6666");
                        node.subscribeToStream(outputStream, Environment.getInstance().getNodeId()+":6666");
                    }
//        System.out.println("Starting in 30 seconds");
//        Thread.sleep(40*1000);

                    //
                    try {
                        runningMode=3;
                        node.start();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Random Evaluation Node started");
                    setStatusLabel("Random evaluation node started");
                }
                nodeTypeComboBox.setEnabled(false);
                startProcessingNodeButton.setEnabled(false);


            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(node!=null) {
                    node.stop();
                }
                if(dispatcher!=null) {
                    dispatcher.stop();
                }
                node=null;
                dispatcher=null;
                dispatcherPanel.setVisible(false);
                nodePanel.setVisible(false);
                selectionComboBox.setEnabled(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                nodeTypeComboBox.setEnabled(true);
                startProcessingNodeButton.setEnabled(true);
                System.out.println("Stopped");
                setStatusLabel("Stopped");
            }
        });
        dispatherStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(bufferingModeComboBox.getSelectedIndex()==0){
                    EventServer.setBufferingEnabled(true);
                }else if(bufferingModeComboBox.getSelectedIndex()==1){
                    EventServer.setBufferingEnabled(false);
                }
                dispatcher=new Dispatcher();
                setGUICallback();
                dispatcher.initialize();
                System.out.println("Dispatcher started");
                setStatusLabel("Dispatcher started");
                dispatherStartButton.setEnabled(false);
            }
        });
    }

    public void setGUICallback(){
        if(!guiCallbackSet) {
            if (runningMode == 1) {
                if (dispatcher != null) {
                    dispatcher.setGuiCallback(this);
                }
            } else if (runningMode == 2 || runningMode == 3) {
                if (node != null) {
                    node.setGuiCallback(this);
                }
            }
        }
    }

    public void setUtilizationValues(int cpu,int memory,int network){
        cpuProgressBar.setValue(cpu);
        cpuPresentageLabel.setText(cpu+"%");
        if(cpu>75)
            cpuProgressBar.setForeground(Color.red);
        else
            cpuProgressBar.setForeground(Color.lightGray);

        memoryProgressBar.setValue(memory);
        memoryPresentageLabel.setText(memory+"%");
        if(memory>75){
            memoryProgressBar.setForeground(Color.red);
        }else{
            memoryProgressBar.setForeground(Color.lightGray);
        }
        networkProgressBar.setValue(network);
        networkPresentageLabel.setText(network+"%");
        if(network>75){
            networkProgressBar.setForeground(Color.red);
        }else {
            networkProgressBar.setForeground(Color.lightGray);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("NodeGUI");

        try {
            //UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
            //UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
            try{
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            }catch(UnsupportedLookAndFeelException e2){
                e2.printStackTrace();
            }
        }
        frame.setContentPane(new NodeGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800,400);
        frame.setVisible(true);
    }

    public void drawDispatcherTable(List<Query> queryList1){
        if(dispatcherTableScrollPane==null) {
            Object rowData[][] = new Object[queryList1.size()][2];

            int i=0;
            List<StreamDefinition> ouputStreamDefinitionList=new ArrayList<StreamDefinition>();
            ///Map<String,List<StreamDefinition>> nodeStreamDefinitionListMap=new HashMap<String, List<StreamDefinition>>();

            for(Query q : queryList1){
                ouputStreamDefinitionList.add(q.getOutputStreamDefinition());
            }

            Map<String,List<String>> destinationNodeMap;

            //Have to check about whether dispatcher or processing node
            if(Environment.getInstance().getNodeType()==Environment.NODE_TYPE_DISPATCHER){
                destinationNodeMap=Environment.getInstance().getEventNodeMapping();
            }else {
                destinationNodeMap=Environment.getInstance().getSubscriberMapping();
            }


            for(StreamDefinition streamDefinition : ouputStreamDefinitionList){
                String streamId = streamDefinition.getStreamId();
                List<String> nodeIpList=destinationNodeMap.get(streamId);
                if(nodeIpList != null) {
                    rowData[i][0]=streamId;
                    for (String nodeIp : nodeIpList) {
                        if(rowData[i][1]!=null) {
                            rowData[i][1] = rowData[i][1] + ", " + nodeIp;
                        }else{
                            rowData[i][1] = nodeIp;
                        }
                    }
                    i++;
                }
            }
            Object columnNames[] = {"Stream", "Node"};
            JTable table = new JTable(rowData, columnNames);

            dispatcherTableScrollPane = new JScrollPane(table);
            //dispatcherTableScrollPane.setViewportView(table);
            dispatcherTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            dispatherTabelPanel.setLayout(new GridLayout());
            dispatherTabelPanel.add(dispatcherTableScrollPane);
        }
    }

    public void drawProcessorTable(List<Query> queryList1) {

        Object rowData[][] = new Object[queryList1.size()][2];

//            rowData[0][0]="query0";
//            rowData[0][1]="from cseEventStream[price==foo.price and foo.try>5 in foo] " +
//                    "select symbol, avg(price) as avgPrice";
//
//            rowData[1][0]="query1";
//            rowData[1][1]="from car [Id>=10]#window.time(1000) select brand,Id insert into filterCar;";
//
//            rowData[2][0]="query2";
//            rowData[2][1]="from ABC " +
//                "[ Att1 >= 50 ] select Att1 " +
//                "insert into DER;";
//
        int i=0;
        for(Query q:queryList1){
            rowData[i][0]=q.getQueryId();
            rowData[i][1]=q.getQuery();
            i++;
        }

        Object columnNames[] = {"QueryId", "Query"};
        JTable table = new JTable(rowData, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        if (processorTableScrollPane == null) {
            processorTableScrollPane = new JScrollPane(table);

            processorTabelPanel.setLayout(new GridLayout());
            processorTabelPanel.add(processorTableScrollPane);
        }else{
            processorTableScrollPane = new JScrollPane(table);
            processorTableScrollPane.repaint();
        }
    }

    @Override
    public void queriesChanged() {
        if(runningMode==1){
            if(dispatcher!=null) {
                drawDispatcherTable(dispatcher.getQueries());
            }
        }else if(runningMode==2||runningMode==3){
            if(node!=null) {
                drawProcessorTable(node.getQueries());
            }
        }
        setStatusLabel("New queries added");
    }

    @Override
    public void newEnginesRecieved(String from) {
        setStatusLabel("New Engines received from "+from);
    }

    @Override
    public void bufferingStart() {
        setStatusLabel("Buffering started");
    }

    public void setStatusLabel(final String statusText){
        statusLabel.setText("Status: "+statusText);
        Thread t=new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(10000);
                }catch (InterruptedException e){

                }
                statusLabel.setText("");
            }
        };
        t.start();
    }
}
