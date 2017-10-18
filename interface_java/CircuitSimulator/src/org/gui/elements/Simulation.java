/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

/**
 *
 * @author paulo
 */
public class Simulation
{
    private String type;
    private double start_time;
    private double end_time;
    private double step_time;
    private Circuit circuit;
    
    public Simulation(String type, double s_t, double e_t, double s, Circuit c)
    {
        this.type=type;
        this.start_time=s_t;
        this.end_time=e_t;
        this.step_time=s;
        this.circuit=c;
    }
    
    
    
}
