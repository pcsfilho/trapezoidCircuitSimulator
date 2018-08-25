package org.gui.plot;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author paulo
 */
public class WindowPlot extends JFrame
{
    private ChartPanel graphicsArea;
    private XYSeries dataset;
    private String title;
    private String yAxisLabel;
    
    public WindowPlot(String title, String yLabel)
    {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graphicsArea = new ChartPanel(null);
        graphicsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Grafico"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        dataset= new XYSeries(title);
        this.title=title;
        this.yAxisLabel=yLabel;
        add(graphicsArea, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void add_item(double x, double y)
    {
        dataset.add(x, y);
    }
    
    public void open_chart()
    {
        graphicsArea.setChart(ChartFactory.createXYLineChart(title,"tempo (s)", yAxisLabel, new XYSeriesCollection(dataset),PlotOrientation.VERTICAL,true,true,false));
        /*XYPlot xyPlot = graphicsArea.getChart().getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) xyPlot.getRangeAxis();       
        if(title.equals("V1"))
        {
            rangeAxis.setTickUnit(new NumberTickUnit(20));
        }
        else
        {
            rangeAxis.setTickUnit(new NumberTickUnit(4));
        }*/
    }
}
