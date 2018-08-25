package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;

    public class Wire extends Resistor {
	public static boolean ideal = false;
	private static final double defaultResistance = 1E-06;
        
	public Wire(int xx, int yy) 
        {
            super(xx, yy);
            value = defaultResistance;
        }
        
	public Wire(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) 
        {
	    super(xa, ya, xb, yb, f, new StringTokenizer("0.0"));
	    value = defaultResistance;
	}
	
	public void draw(Graphics g) 
        {
	    setVoltageColor(g, volts[0]);
	    drawThickLine(g, point1, point2);
	    doDots(g);
	    setBbox(point1, point2, 3);
	    
	    drawPosts(g);
	}

        @Override
        public void set_name()
        {
            countWires++;
            name = "W"+countWires;
        }
	
	public int getType()
        {
            return 'W'; 
        }
        
    }
