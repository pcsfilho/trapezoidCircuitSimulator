/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.plot;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author paulo
 */
public class PlotFile extends JFrame
{
    private ChartPanel graphicsArea;
    public PlotFile(String title, JFrame parent)
    {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        graphicsArea = new ChartPanel(null);
        graphicsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Grafico"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        add(graphicsArea, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    public void open_chart(String path_file)
    {
        File file = new File(path_file);
        graphicsArea.setChart(creatChart(file));
    }
    
    private JFreeChart creatChart(File file) {
        String title = null;
        String xAxisLabel = null;
        String yAxisLabel = null;
        BufferedReader in = null;
        String line = null;
        XYSeries dataset;
        try
        {
            in = new BufferedReader(new FileReader(file));
            line = in.readLine();
            //dataset = new XYSeries(line.split(" ")[1]);
            dataset = new XYSeries("Elemento");
            while ((line = in.readLine()) != null)
            {
                String[] data_line;
                data_line = line.split(" ");
                dataset.add(Double.valueOf(data_line[0]).doubleValue(), Double.valueOf(data_line[1]).doubleValue());
            }
            return ChartFactory.createXYLineChart(title,xAxisLabel, yAxisLabel, new XYSeriesCollection(dataset),PlotOrientation.VERTICAL,true,true,false);
        }
        catch (IOException ex)
        {
            ex.printStackTrace(System.err);
        }
        return null;
    }
}
