package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

    public class Capacitor extends CircuitElement {
	Point plate1[], plate2[];
                
	public Capacitor(int xx, int yy) 
        {
	    super(xx, yy);
	    value = 1e-6;
	}
	public Capacitor(int xa, int ya, int xb, int yb, int f,StringTokenizer st)
        {
	    super(xa, ya, xb, yb, f);
            value=1e-6;
	}
	
        
        
        @Override
        public int getType()
        {
            return 'C';
        }
	
        @Override
        public void setPoints() {
	    super.setPoints();
	    double f = (dn/2-4)/dn;
	    // calc leads
	    lead1 = interpPoint(point1, point2, f);
	    lead2 = interpPoint(point1, point2, 1-f);
	    // calc plates
	    plate1 = newPointArray(2);
	    plate2 = newPointArray(2);
	    interpPoint2(point1, point2, plate1[0], plate1[1], f, 12);
	    interpPoint2(point1, point2, plate2[0], plate2[1], 1-f, 12);
	}
               
        @Override
	public void draw(Graphics g) {
	    int hs = 12;
	    setBbox(point1, point2, hs);
	    
	    // draw first lead and plate
	    setVoltageColor(g, volts[0]);
	    drawThickLine(g, point1, lead1);
	    setPowerColor(g, true);
	    drawThickLine(g, plate1[0], plate1[1]);
            g.setColor(Color.gray);

	    // draw second lead and plate
	    setVoltageColor(g, volts[1]);
	    drawThickLine(g, point2, lead2);
	    setPowerColor(g, true);
	    drawThickLine(g, plate2[0], plate2[1]);
	    
	    updateDotCount();
	    if (sim.dragElm != this) {
		drawDots(g, point1, lead1, curcount);
		drawDots(g, point2, lead2, -curcount);
	    }
	    drawPosts(g);
            String s = getShortUnitText(value, "F");
            drawValues(g, name, hs);
	}

        @Override
        public void set_name()
        {
            countCapacitors++;
            name = "C"+countCapacitors;
        }
        
        @Override
        public EditInfo getEditInfo(int n)
        {
            if(n==0)
            {
                return new EditInfo("Capacitância (F)", value, 0, 0);
            }
            else if (n == 1)
            {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("PLOTAR TENSAO", false);
		return ei;
	    }
            else if (n == 2)
            {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("PLOTAR CORRENTE", false);
		return ei;
	    }
            return null;
	}
        
        @Override
	public void setEditValue(int n, EditInfo ei)
        {
            if (n == 0 && ei.value > 0)
            {
		value = ei.value;
            }
            else if (n == 1) 
            {
                plot_voltage=false;
		if (ei.checkbox.getState())
                {
                    plot_voltage=true;
                }
	    }
            else if (n == 2) 
            {
                plot_current=false;
		if (ei.checkbox.getState())
                {
                    plot_current=true;
                }
	    }
	}
    }
