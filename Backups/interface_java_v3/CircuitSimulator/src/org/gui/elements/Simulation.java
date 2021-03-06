/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author paulo
 */
public class Simulation
{
    private String type;
    private double start_time;
    private double end_time;
    private Circuit circuit;
    private double step;
    
    public Simulation(String type, double s_t, double e_t, double st,Circuit c)
    {
        this.type=type;
        this.start_time=s_t;
        this.end_time=e_t;
        this.circuit=c;
        this.step = st;
    }
    /**
    * 
     * @return 
     * @throws java.io.IOException
    */
    public String create_simulation_file() throws IOException
    {
        //circuit.create_netlist_circuit();
        FileWriter fw = new FileWriter(circuit.get_path_circuit_name(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);        
        out.println();
        out.print(". "+type+" "+step+" "+start_time+" "+end_time);
        out.close();
        return circuit.get_path_circuit_name();
    }
    
    public Circuit getCircuit()
    {
        return circuit;
    }
    
    public void set_step_size(double step)
    {
        this.step=step;
    }
    
    public double get_step_size()
    {
        return this.step;
    }
}
