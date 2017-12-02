package org.gui.canvas;
import java.awt.*;

class CircuitCanvas extends Canvas {
    PanelCircuitArea pg;
    int width, height, rows, columns;
    
    
    CircuitCanvas(PanelCircuitArea p) 
    {
	pg = p;
        setSize(width = 600, height = 700);
        rows = 30;
        columns = 35;
    }
    
    CircuitCanvas(PanelCircuitArea p, int w, int h, int r, int c) 
    {
	pg = p;
        setSize(width = w, height = h);
        rows = r;
        columns = c;
    }
    /*public Dimension getPreferredSize() {
	return new Dimension(400,500);
    }*/
    public void update(Graphics g) {
	pg.updateCircuit(g);
    }
    public void paint(Graphics g) {
	pg.updateCircuit(g);
    }
    
    public void grid(Graphics g) 
    {
        int k;
        width = getSize().width;
        height = getSize().height;

        int htOfRow = height / (rows);
        g.setColor(Color.LIGHT_GRAY);
        for (k = 0; k < rows; k++)
            
          g.drawLine(0, k * htOfRow , width, k * htOfRow );

        int wdOfRow = width / (columns);
        for (k = 0; k < columns; k++)
          g.drawLine(k*wdOfRow , 0, k*wdOfRow , height);
    }
};
