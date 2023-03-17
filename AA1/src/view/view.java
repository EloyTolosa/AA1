/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.*;
import model.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 *
 * @author eloytolosa
 */
public class view extends JFrame implements comunicable {

    JPanel mainPanel, chartPanel;
    XChartPanel xChart;

    JButton n,nlogn, nsquared;

    JProgressBar progressBar;

    // GUI Static variables
    static int FRAME_WIDTH = 1000;
    static int FRAME_HEIGHT = 800;

    static int MAIN_PANEL_HEIGHT = FRAME_HEIGHT;
    static int MAIN_PANEL_WIDTH = FRAME_WIDTH;

    static int TOP_PANEL_HEIGHT = (int)((2/(double)15)*FRAME_HEIGHT);
    static int TOP_PANEL_WIDTH = FRAME_WIDTH;
    static int TOP_PANEL_X = 0;
    static int TOP_PANEL_Y = 0;

    static int CHART_WIDTH = FRAME_WIDTH;
    static int CHART_HEIGHT = (int)((10/(double)15)*FRAME_HEIGHT);
    static int CHART_X = 0;
    static int CHART_Y = TOP_PANEL_HEIGHT;

    static int BOTTOM_PANEL_HEIGHT = (int)((1/(double)15)*FRAME_HEIGHT);
    static int BOTTOM_PANEL_WIDTH = FRAME_WIDTH;
    static int BOTTOM_PANEL_X = 0;
    static int BOTTOM_PANEL_Y = TOP_PANEL_HEIGHT+CHART_HEIGHT;

    static int BUTTON_WIDTH = (int)((4/(double)15)*TOP_PANEL_WIDTH); // 3 BUTTONS, 3*4 = 12; 12+3 FROM THE SELECTOR = 15
    static int BUTTON_HEIGHT = TOP_PANEL_HEIGHT;
    static int BUTTON_INIT_X = 0;
    static int BUTTON_INIT_Y = 0;
    
    // Program variables
    controller controller;
    
    public CategoryChart getChart(double[][] inputData) {
        CategoryChart chart = new CategoryChartBuilder().width(CHART_WIDTH).height(CHART_HEIGHT).title("Chart").build();

        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);
        
        chart.addSeries("Data", inputData[0], inputData[1]);

        return chart;
    }
        
    public void showChart(double[][] data) {
        if (xChart != null) {
            mainPanel.remove(chartPanel);
        }

        // Restart chart panel
        chartPanel = new JPanel();
        chartPanel.setBounds(CHART_X, CHART_Y, CHART_WIDTH, CHART_HEIGHT);
        chartPanel.add(new XChartPanel(getChart(data)));
        mainPanel.add(chartPanel);
        
        // Revalidate and pack window
        revalidate();
    }
    
    public view(String title) {

        super(title);
               
        // Panel parent container
        mainPanel = new JPanel();  
        mainPanel.setLayout(null);  // Absolute layout
        mainPanel.setBounds(TOP_PANEL_X, TOP_PANEL_Y, FRAME_WIDTH, FRAME_HEIGHT);
        
        // Chart panel
        xChart = null;
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setBounds(BOTTOM_PANEL_X, BOTTOM_PANEL_Y, BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT);
        
        // Declare buttons and their listener functions
        n = new JButton();
        n.setBounds(BUTTON_INIT_X+0*BUTTON_WIDTH, BUTTON_INIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        n.setText("n");
        n.addActionListener((al)-> {
            controller.comunicate(action.CALCULATE, Function.N);
        });
        
        nlogn = new JButton();  
        nlogn.setBounds(BUTTON_INIT_X+1*BUTTON_WIDTH, BUTTON_INIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        nlogn.setText("n*log(n)");
        nlogn.addActionListener((al) -> {
            controller.comunicate(action.CALCULATE, Function.N_LOG_N);
        });

        nsquared = new JButton();
        nsquared.setBounds(BUTTON_INIT_X+2*BUTTON_WIDTH, BUTTON_INIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        nsquared.setText("n^2");
        nsquared.addActionListener((e)-> {
            controller.comunicate(action.CALCULATE, Function.N_SQUARED);
        });

        /* arrayNSelector = new JTextArea();
        arrayNSelector.setToolTipText("Select array N: (min 100 max 1000)");
        arrayNSelector.setBounds(N_SELECTOR_X, N_SELECTOR_Y, N_SELECTOR_WIDTH, N_SELECTOR_HEIGHT); */
        
        // Add buttons to panel
        mainPanel.add(n);
        mainPanel.add(nsquared);
        mainPanel.add(nlogn);
        
        // Add progress bar
        mainPanel.add(progressBar);
        
        // Add panel to frame
        add(mainPanel); 
        
        // Frame properties
        setSize(FRAME_WIDTH, FRAME_HEIGHT);  
        setLocationRelativeTo(null);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Show GUI
        setVisible(true);  
        
        // Initialize controller object 
        controller = new controller(this);
        
    }

    @Override
    public void comunicate(Object... data) {
        // code goes here
        action c = (action) data[0];
        switch (c) {
            case PAINT:
                double[][] inputData = (double[][])data[1];
                showChart(inputData);
                controller.comunicate(action.STOP);
                break;
            case PROGRESS:
                int value = (int) data[1];
                progressBar.setValue(value);
                break;
            default:
                System.out.println("[VIEW] Not implemented.");
        }
    }
    
}
