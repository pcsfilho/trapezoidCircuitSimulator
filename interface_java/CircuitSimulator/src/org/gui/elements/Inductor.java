package org.gui.elements;

import java.awt.Checkbox;
import java.awt.Graphics;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

public class Inductor extends CircuitElement{
    public static final int FLAG_BACK_EULER = 2;
    int nodes[];
    int flags;
      
    public Inductor(int xx, int yy) {
	    super(xx, yy);
	    value = 1;
	}
	public Inductor(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    value = new Double(st.nextToken()).doubleValue();
	    current = new Double(st.nextToken()).doubleValue();
	}
        
        public void set_name()
        {
            countInductors++;
            name = "L"+countInductors;
        }
        
	public int getType() { return 'L'; }

	public void setPoints() {
	    super.setPoints();
	    calcLeads(32);
	}
	public void draw(Graphics g) {
	    double v1 = volts[0];
	    double v2 = volts[1];
	    int i;
	    int hs = 8;
	    setBbox(point1, point2, hs);
	    draw2Leads(g);
	    setPowerColor(g, false);
	    drawCoil(g, 8, lead1, lead2, v1, v2);
		String s = getShortUnitText(value, "H");
		drawValues(g, name, hs);
	    doDots(g);
	    drawPosts(g);
	}

        void getInfo(String arr[]) {
	    arr[0] = "inductor";
	    getBasicInfo(arr);
	    arr[3] = "L = " + getUnitText(value, "H");
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	}
	public EditInfo getEditInfo(int n) {
            if (n == 0)
            return new EditInfo("Indutância (H)", value, 0, 0);
            
            return null;
	}
        
	public void setEditValue(int n, EditInfo ei) {
            value = ei.value;
	}
    
}
