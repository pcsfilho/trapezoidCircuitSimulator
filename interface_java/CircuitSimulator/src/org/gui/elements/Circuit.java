/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.gui.plot.ManagePlot;

/**
 *
 * @author paulo
*/
public class Circuit
{
    private ArrayList<CircuitElement> elements;    
    private ArrayList<CircuitElement> sources;  
    private ArrayList<CircuitElement> measuring_elements;  
    private String circuit_name;
    private String path_circuit_name;
    private int elmCount;
    private int plotCount;
    private ManagePlot manage_plot;
    
    public Circuit(ManagePlot mp)
    {
        elements=new  ArrayList<>();
        sources=new  ArrayList<>();
        measuring_elements=new  ArrayList<>();
        circuit_name="";
        path_circuit_name="";
        elmCount=0;
        plotCount=0;
        this.manage_plot=mp;
    }
    
    public void add_element(CircuitElement elm)
    {
        String type = ""+(char)elm.getType();
        switch (type) {
            case "V":
                sources.add(elm);
                elements.add(elm);
                break;
            case "M":
            case "A":
                measuring_elements.add(elm);
                break;
            default:
                elements.add(elmCount,elm);
                elmCount++;
                break;
        }
    }
    
    public int getNumPlots()
    {
        return plotCount;
    }
    
    public void set_circuit_name(String n)
    {
        this.circuit_name=n;
    }
    
    public void set_path_circuit_name(String n)
    {
        this.path_circuit_name=n;
    }
    
    public ArrayList<CircuitElement> get_elements()
    {
        return elements;
    }
    
    public ArrayList<CircuitElement> get_sources()
    {
        return sources;
    }
    /**
    * Criar netlist a partir da lista de elementos
    */
    public void get_netlist_circuit()
    {
        
    }
    
    public String get_circuit_name()
    {
        return circuit_name;
    }
    
    public String get_path_circuit_name()
    {
        return path_circuit_name;
    }
    
    public void create_netlist_circuit() throws FileNotFoundException, UnsupportedEncodingException
    {
        PrintWriter writer = new PrintWriter(path_circuit_name,"UTF-8");
        writer.println("circuito");        
        for(int i=0;i<elmCount;i++)
        {
            CircuitElement ce=elements.get(i);
            String type = ""+(char)ce.getType();
            
            int node_1=ce.nodes[0];
            int node_2=ce.nodes[1];
            String name = ce.get_name();
            
            
            String line;
            if(ce instanceof Switch)
            {
                int num_c = ((Switch)ce).get_time_coomutations().size();
                int state = (((Switch)ce).getState()) ? 1 : 0;
                line = type+" "+name+" "+node_1+" "+node_2+" "+state+" "+num_c;
                for(int j=0;j<num_c;j++)
                {
                    line+=" "+((Switch)ce).get_time_coomutations().get(j);
                }
                
            }
            else
            {
                double value=ce.get_value();
                line = type+" "+name+" "+node_1+" "+node_2+" "+value;
            }
            writer.print(line);
            if(i!=(elmCount-1))
            {
                writer.println();
            }
        }
        writer.println();
        for(int i=0;i<sources.size();i++)
        {
            CircuitElement ce=sources.get(i);
            String type = ""+(char)ce.getType();
            
            int node_1=ce.nodes[0];
            int node_2=ce.nodes[1];
            String name = ce.get_name();
            
            double value=ce.get_value();
            String line;
            line = type+" "+name+" "+((Voltage)ce).get_type_current()+" "+node_1+" "+node_2+" "+value;
            if(((Voltage)ce).get_type_current().equals("AC"))
            {
                line+=" "+((ACVoltageSource)ce).get_frequency();
            }
            writer.print(line);
            if(i!=(sources.size()-1))
            {
                writer.println();
            }
        }
        
        for(int i=0;i<elmCount;i++)
        {
            CircuitElement ce=elements.get(i);
            String name = ce.get_name();
            
            String line;
            if(ce.getPlotCurrent())
            {
                plotCount++;
                writer.println();
                line = ". PLOT A "+name;
                writer.print(line);
                //manage_plot.add_plot(name,"Corrente (A)");
            }
            
            if(ce.getPlotVoltage())
            {
                plotCount++;
                writer.println();
                line = ". PLOT V "+name;
                writer.print(line);
                //manage_plot.add_plot(name,"Tensao (V)");
            }
        }
        
        for(int i=0;i<measuring_elements.size();i++)
        {
            writer.println();
            CircuitElement ce=measuring_elements.get(i);
            String type = ""+(char)ce.getType();
            int node_1=ce.nodes[0];
            int node_2=ce.nodes[1];
            String name = ce.get_name();
            String line;
            if(type.equals("M"))
            {
                type="V";
                manage_plot.add_plot(name,"Tensao (V)");
            }
            else
            {
                manage_plot.add_plot(name,"Corrente (A)");
            }
            
            line = ". PLOT "+type+" "+node_1+" "+node_2;
            
            writer.print(line);
            //System.out.println("Linha: "+line);
        }
        writer.close();
    }
}
