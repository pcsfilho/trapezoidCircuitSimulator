package org.gui;

import javax.swing.JTextPane;

public class TrapezoidCircuitSimulator{

    private static final long serialVersionUID = 1L;
    public final static String AUTHOR_EMAIL = "paulo.ecomp@gmail.com";
    public final static String NAME = "SimAC";
    public final static double VERSION = 3.0;
    public static TrapezoidCircuitSimulator trapezoid_circuit_simulator;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        
        MainWindow mw=new MainWindow(TrapezoidCircuitSimulator.get_trapezoid_circuit_simulator());
        mw.setVisible(true);
    }

    public static TrapezoidCircuitSimulator get_trapezoid_circuit_simulator()
    {
        if(trapezoid_circuit_simulator==null)
        {
            return new TrapezoidCircuitSimulator();
        }
        return trapezoid_circuit_simulator;
    }
    
    public static void create_netlist_file()
    {
        
    }
}