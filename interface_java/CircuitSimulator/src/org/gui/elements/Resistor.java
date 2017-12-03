package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

    public class Resistor extends CircuitElement
    {
       
        Point ps3, ps4;
        
	public Resistor(int xx, int yy) { super(xx, yy); value = 500; }
	public Resistor(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) 
        {
	    super(xa, ya, xb, yb, f);
            value=500;
	}
        @Override
	public int getType() 
        {
            return 'R'; 
        }
	
        
        @Override
        public void set_name()
        {
            countResistors++;
            name = "R"+countResistors;
        }
	
        
        @Override
	public void setPoints() {
	    super.setPoints();
	    calcLeads(32);
	    ps3 = new Point();
	    ps4 = new Point();
	}
	
	public void draw(Graphics g) {
	    int segments = 16;
	    int i;
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
                drawValues(g, name, 10);
                doDots(g);
                drawPosts(g);
                
	}
	
	public EditInfo getEditInfo(int n)
        {
            if(n==0)
            {
                return new EditInfo("Resistencia (OHMS)", value, 0, 0);
            }
            else if (n == 1)
            {
		EditInfo ei = new EditInfo("", 0, -1, -1);
                
                
		ei.checkbox = new Checkbox("PLOTAR TENSAO", plot_voltage);
		return ei;
	    }
            else if (n == 2)
            {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("PLOTAR CORRENTE", plot_current);
		return ei;
	    }
            return null;
	}
        
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
