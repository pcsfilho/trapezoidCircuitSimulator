package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

    public class CurrentSource extends CircuitElement {
	
	public CurrentSource(int xx, int yy) {
	    super(xx, yy);
	    value = .01;
	}
	public CurrentSource(int xa, int ya, int xb, int yb, int f,
		   StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	    try {
		value = new Double(st.nextToken()).doubleValue();
	    } catch (Exception e) {
		value = .01;
	    }
	}
	
	public int getType()
        {
            return 'i'; 
        }
	
	Polygon arrow;
	Point ashaft1, ashaft2, center;
	public void setPoints() {
	    super.setPoints();
	    calcLeads(26);
	    ashaft1 = interpPoint(lead1, lead2, .25);
	    ashaft2 = interpPoint(lead1, lead2, .6);
	    center = interpPoint(lead1, lead2, .5);
	    Point p2 = interpPoint(lead1, lead2, .75);
	    arrow = calcArrow(center, p2, 4, 4);
	}
        
        public void set_name()
        {
            countCurrentSources++;
            name = "FC"+countCurrentSources;
        }
        
	public void draw(Graphics g) {
	    int cr = 12;
	    draw2Leads(g);
	    setVoltageColor(g, (volts[0]+volts[1])/2);
	    setPowerColor(g, false);
	    
	    drawThickCircle(g, center.x, center.y, cr);
	    drawThickLine(g, ashaft1, ashaft2);

	    g.fillPolygon(arrow);
	    setBbox(point1, point2, cr);
	    doDots(g);
		String s = getShortUnitText(value, "A");
		if (dx == 0 || dy == 0)
		    drawValues(g, s, cr);
	    drawPosts(g);
	}
	void stamp() {
	    current = value;
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0)
		return new EditInfo("Current (A)", value, 0, .1);
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    value = ei.value;
	}
	void getInfo(String arr[]) {
	    arr[0] = "current source";
	    getBasicInfo(arr);
	}
	public double getVoltageDiff() {
	    return volts[1] - volts[0];
	}
    }
