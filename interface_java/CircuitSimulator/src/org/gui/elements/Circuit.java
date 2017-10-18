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

/**
 *
 * @author paulo
 */

public class Circuit
{
    private ArrayList<CircuitElement> elements;    
    private String circuit_name;
    private String path_circuit_name;
    
    public Circuit()
    {
        elements=new  ArrayList<>();
        circuit_name="";
        path_circuit_name="";
    }
    
    public void add_element(CircuitElement elm)
    {
        elements.add(elm);
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
        
        for(int i=0;i<elements.size();i++)
        {
            CircuitElement ce=elements.get(i);
            int node_1=ce.nodes[0];
            int node_2=ce.nodes[1];
            String name = ce.get_name();
            writer.print(name+" "+node_1+" "+node_2);
            if(i!=(elements.size()-1))
            {
                writer.println();
            }
        }
        writer.close();
    }
}
