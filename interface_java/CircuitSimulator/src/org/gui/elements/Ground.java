package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;

    public class Ground extends CircuitElement {
	public Ground(int xx, int yy) { super(xx, yy); }
	public Ground(int xa, int ya, int xb, int yb, int f,
			 StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	}
	public int getDumpType() { return 'g'; }
	public int getNodesCount() { return 1; }
	public void draw(Graphics g) {
	    setVoltageColor(g, 0);
	    drawThickLine(g, point1, point2);
	    int i;
	    for (i = 0; i != 3; i++) {
		int a = 10-i*4;
		int b = i*5; // -10;
		interpPoint2(point1, point2, ps1, ps2, 1+b/dn, a);
		drawThickLine(g, ps1, ps2);
	    }
	    doDots(g);
	    interpPoint(point1, point2, ps2, 1+11./dn);
	    setBbox(point1, ps2, 11);
	    drawPost(g, x_1, y_1, nodes[0]);
	}
	public void setCurrent(int x, double c) { current = -c; }
	void stamp() {
	}
	public double getVoltageDiff() { return 0; }
	public int getVoltageSourceCount() { return 1; }
	void getInfo(String arr[]) {
	    arr[0] = "ground";
	    arr[1] = "I = " + getCurrentText(getCurrent());
	}
	boolean hasGroundConnection(int n1) { return true; }
	public int getShortcut() { return 'g'; }
    }
