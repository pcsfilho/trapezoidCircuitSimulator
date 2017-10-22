package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

    public class Resistor extends CircuitElement {
	public Resistor(int xx, int yy) { super(xx, yy); value = 100; }
	public Resistor(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) 
        {
	    super(xa, ya, xb, yb, f);
	    value = new Double(st.nextToken()).doubleValue();
	}
	public int getType() { return 'r'; }
	
        
        public void set_name()
        {
            countResistors++;
            name = "R"+countResistors;
        }
	Point ps3, ps4;
	public void setPoints() {
	    super.setPoints();
	    calcLeads(32);
	    ps3 = new Point();
	    ps4 = new Point();
	}
	
	public void draw(Graphics g) {
	    int segments = 16;
	    int i;
	    int ox = 0;
	    int hs = 8;
	    double v1 = volts[0];
	    double v2 = volts[1];
	    setBbox(point1, point2, hs);
	    draw2Leads(g);
	    setPowerColor(g, true);
	    double segf = 1./segments;
	    
		// draw rectangle
		setVoltageColor(g, v1);
		interpPoint2(lead1, lead2, ps1, ps2, 0, hs);
		drawThickLine(g, ps1, ps2);
		for (i = 0; i != segments; i++) {
		    double v = v1+(v2-v1)*i/segments;
		    setVoltageColor(g, v);
		    interpPoint2(lead1, lead2, ps1, ps2, i*segf, hs);
		    interpPoint2(lead1, lead2, ps3, ps4, (i+1)*segf, hs);
		    drawThickLine(g, ps1, ps3);
		    drawThickLine(g, ps2, ps4);
		}
		interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
		drawThickLine(g, ps1, ps2);
                doDots(g);
                drawPosts(g);
                drawValues(g, name, 12);
	}
    
	public void calculateCurrent() {
	    current = (volts[0]-volts[1])/value;
	    //System.out.print(this + " res current set to " + current + "\n");
	}
	void stamp() {
	}
	void getInfo(String arr[]) {
	    arr[0] = "resistor";
	    getBasicInfo(arr);
	    arr[3] = "R = " + getUnitText(value, sim.ohmString);
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	}
	public EditInfo getEditInfo(int n) {
	    // ohmString doesn't work here on linux
	    if (n == 0)
		return new EditInfo("Resistência (ohms)", value, 0, 0);
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    if (ei.value > 0)
	        value = ei.value;
	}
	public int getShortcut() { return 'r'; }
    }
