package org.gui.plot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author paulo
 */
public class ManagePlot 
{
    private final ArrayList<WindowPlot> windows_plots;
    
    public ManagePlot()
    {
        windows_plots= new ArrayList<>();
    }
    
    public void open_chart(String path_file)
    {
        File file = new File(path_file);
        creatChart(file);
    }
    
    public void add_plot(String name, String yAxisLabel)
    {
        WindowPlot wp = new WindowPlot(name, yAxisLabel);
        windows_plots.add(wp);
    }
    
    private void creatChart(File file) {
        BufferedReader in = null;
        String line = null;
        
        try
        {
            in = new BufferedReader(new FileReader(file));
            line = in.readLine();
            while ((line = in.readLine()) != null)
            {
                String[] data_line;
                data_line = line.split(" ");
                
                for(int i=0;i<windows_plots.size();i++)
                {
                    windows_plots.get(i).add_item(Double.valueOf(data_line[0]).doubleValue(), Double.valueOf(data_line[1+i]).doubleValue());
                }
            }
            for(int i=0;i<windows_plots.size();i++)
            {
                windows_plots.get(i).open_chart();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
}
