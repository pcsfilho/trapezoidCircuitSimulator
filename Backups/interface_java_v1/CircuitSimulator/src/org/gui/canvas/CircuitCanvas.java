package org.gui.canvas;
import java.awt.*;

class CircuitCanvas extends Canvas {
    PanelCircuitArea pg;
    CircuitCanvas(PanelCircuitArea p) {
	pg = p;
    }
    public Dimension getPreferredSize() {
	return new Dimension(400,500);
    }
    public void update(Graphics g) {
	pg.updateCircuit(g);
    }
    public void paint(Graphics g) {
	pg.updateCircuit(g);
    }
};
