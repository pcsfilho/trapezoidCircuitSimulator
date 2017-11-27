package org.gui.elements;

import java.awt.Checkbox;
import java.awt.Graphics;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

public class Inductor extends CircuitElement{
    
    int nodes[];
    
      
    public Inductor(int xx, int yy) 
    {
	    super(xx, yy);
	    value = 1;
    }
    public Inductor(int xa, int ya, int xb, int yb, int f,StringTokenizer st)
    {
        super(xa, ya, xb, yb, f);
        value = 1;
    }
        
    /**
     *
     */
    @Override
        public void set_name()
        {
            countInductors++;
            name = "L"+countInductors;
        }
        
    @Override
	public int getType() 
        {
            return 'L';
        }

    @Override
	public void setPoints() 
        {
	    super.setPoints();
	    calcLeads(32);
	}
    @Override
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

        
    @Override
	public EditInfo getEditInfo(int n)
        {
            if(n==0)
            {
                return new EditInfo("Indutância (H)", value, 0, 0);
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
