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
    public static final double step = 1e-6;
    
    public Simulation(String type, double s_t, double e_t, Circuit c)
    {
        this.type=type;
        this.start_time=s_t;
        this.end_time=e_t;
        this.circuit=c;
    }
    /**
    * 
    */
    public void create_simulation_file() throws IOException
    {
        FileWriter fw = new FileWriter(circuit.get_path_circuit_name(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);        
        out.println();
        out.println("."+type+" "+step+" "+start_time+" "+end_time);
        out.close();
    }
}
