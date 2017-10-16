package org.gui;

import javax.swing.JTextPane;

public class TrapezoidCircuitSimulator extends JTextPane {

    private static final long serialVersionUID = 1L;
    public final static String AUTHOR_EMAIL = "paulo.ecomp@gmail.com";
    public final static String NAME = "SimAC";
    public final static double VERSION = 3.0;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new MainWindow().setVisible(true);
    }

}