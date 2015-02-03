package org.cse.visiri.app.gui.chartpanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lasitha on 1/20/15.
 */

/**
 * A demonstration application showing a time series chart where you can dynamically add
 * (random) data by clicking on a button.
 *
 */
public class ChartFrame implements ActionListener {

    /** The time series data. */
    private TimeSeries series;

    /** The most recent value added. */
    private double lastValue = 100.0;

    private JPanel contentPanel;

    /**
     * Constructs a new demonstration application.
     *
     */
    public ChartFrame(final String chartName,final double maxVal) {


        this.series = new TimeSeries(chartName, Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset,chartName,maxVal);

        final ChartPanel chartPanel = new ChartPanel(chart);
        final JButton button = new JButton("Refresh");
        button.setActionCommand("Refresh_DATA");
        button.addActionListener(this);

        contentPanel=new JPanel(new BorderLayout());
        contentPanel.add(chartPanel);
        contentPanel.add(button,BorderLayout.NORTH);

        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

    }

    /**
     * Constructs a new demonstration application.
     *
     */
    public ChartFrame(final String chartName) {


        this.series = new TimeSeries(chartName, Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset,chartName);

        final ChartPanel chartPanel = new ChartPanel(chart);
        final JButton button = new JButton("Refresh");
        button.setActionCommand("Refresh_DATA");
        button.addActionListener(this);

        contentPanel=new JPanel(new BorderLayout());
        contentPanel.add(chartPanel);
        contentPanel.add(button,BorderLayout.NORTH);

        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

    }



    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset,final String chartName) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                chartName,
                "Time",
                chartName,
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        return result;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset,final String chartName,final double maxVal) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                chartName,
                "Time",
                chartName,
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, maxVal);
        return result;
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    *
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

    /**
     * Handles a click on the button by adding new (random) data.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("Refresh_DATA")) {
//            final double factor = 0.90 + 0.2 * Math.random();
//            this.lastValue = this.lastValue * factor;
//            final Millisecond now = new Millisecond();
//            //System.out.println("Now = " + now.toString());
            //this.series.add(now, this.lastValue);
            this.series.clear();
        }
    }

    public void refreshChart(final double val){
        this.series.add(new Millisecond(),val);
    }
}
