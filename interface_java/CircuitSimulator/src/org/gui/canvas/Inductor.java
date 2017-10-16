package org.gui.canvas;

import java.awt.Checkbox;
import java.awt.Graphics;
import java.util.StringTokenizer;

public class Inductor extends CircuitElement{
    public static final int FLAG_BACK_EULER = 2;
    int nodes[];
    int flags;
    
    double inductance;
    double compResistance, current;
    double curSourceValue;
   
    public Inductor(int xx, int yy) {
	    super(xx, yy);
	    inductance = 1;
	}
	public Inductor(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    inductance = new Double(st.nextToken()).doubleValue();
	    current = new Double(st.nextToken()).doubleValue();
	}
        int getShortcut() { return 'l'; }
	int getDumpType() { return 'l'; }
	String dump() {
	    return super.dump() + " " + inductance + " " + current;
	}
	void setPoints() {
	    super.setPoints();
	    calcLeads(32);
	}
	void draw(Graphics g) {
	    double v1 = volts[0];
	    double v2 = volts[1];
	    int i;
	    int hs = 8;
	    setBbox(point1, point2, hs);
	    draw2Leads(g);
	    setPowerColor(g, false);
	    drawCoil(g, 8, lead1, lead2, v1, v2);
		String s = getShortUnitText(inductance, "H");
		drawValues(g, s, hs);
	    doDots(g);
	    drawPosts(g);
	}

        void getInfo(String arr[]) {
	    arr[0] = "inductor";
	    getBasicInfo(arr);
	    arr[3] = "L = " + getUnitText(inductance, "H");
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	}
	public EditInfo getEditInfo(int n) {
            if (n == 0)
            return new EditInfo("Indutância (H)", inductance, 0, 0);
            
            return null;
	}
        
	public void setEditValue(int n, EditInfo ei) {
            inductance = ei.value;
	}
    
}
