/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;
import static org.gui.elements.CircuitElement.countammeters;
import static org.gui.elements.CircuitElement.drawThickCircle;

/**
 *
 * @author paulo
 */
public class Voltmeter extends CircuitElement
{
    public Voltmeter(int xx, int yy) {
	    super(xx, yy);
	}
	public Voltmeter(int xa, int ya, int xb, int yb, int f,
		   StringTokenizer st) {
	    super(xa, ya, xb, yb, f);
	}
	
	public int getType()
        {
            return 'M'; 
        }
	
	Point ashaft1, ashaft2, center;
	public void setPoints() 
        {
	    super.setPoints();
	    calcLeads(26);
	    ashaft1 = interpPoint(lead1, lead2, .25);
	    ashaft2 = interpPoint(lead1, lead2, .6);
	    center = interpPoint(lead1, lead2, .5);
	    Point p2 = interpPoint(lead1, lead2, .75);
	}
        
        public void set_name()
        {
            countammeters++;
            name = "V"+countammeters;
        }
        
	public void draw(Graphics g) {
	    int cr = 15;
	    draw2Leads(g);
	    setVoltageColor(g, (volts[0]+volts[1])/2);
	    setPowerColor(g, false);
	    
	    drawThickCircle(g, center.x, center.y, cr);
            
            g.drawString("V", center.x-3, center.y+(cr-10));
            
	    setBbox(point1, point2, cr);
	    doDots(g);
		//String s = getShortUnitText(value, "A");
		//if (dx == 0 || dy == 0)
		  //  drawValues(g, s, cr);
	    drawPosts(g);
	}
}
