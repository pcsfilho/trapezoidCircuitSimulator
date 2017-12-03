/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;

/**
 *
 * @author paulo
 */
public class Ammeter extends Wire
{
    public static boolean ideal = false;
    private static final double defaultResistance = 1E-06;
    
    public Ammeter(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) 
        {
	    super(xa, ya, xb, yb, f, new StringTokenizer("0.0"));
	    value = defaultResistance;
	}
    
    public Ammeter(int xx, int yy) {
	    super(xx, yy);
            value = defaultResistance;
	}
	
	public int getType()
        {
            return 'A'; 
        }
	
        
        
	Point ashaft1, ashaft2, center;
	public void setPoints() 
        {
	    super.setPoints();
	    calcLeads(26);
	    ashaft1 = interpPoint(lead1, lead2, 0);
	    ashaft2 = interpPoint(lead1, lead2, .6);
	    center = interpPoint(lead1, lead2, .5);
	}
        
        public void set_name()
        {
            countAmmeters++;
            name = "I"+countAmmeters;
        }
        
	public void draw(Graphics g) {
	    	    int cr = 15;
	    draw2Leads(g);
	    setVoltageColor(g, (volts[0]+volts[1])/2);
	    setPowerColor(g, false);
	    
	    drawThickCircle(g, center.x, center.y, cr);
            
            g.drawString("A", center.x-3, center.y+(cr-10));
            
	    setBbox(point1, point2, cr);
	    doDots(g);
		//String s = getShortUnitText(value, "A");
		//if (dx == 0 || dy == 0)
		  //  drawValues(g, s, cr);
            drawValues(g, name, cr);
	    drawPosts(g);
	}
}
