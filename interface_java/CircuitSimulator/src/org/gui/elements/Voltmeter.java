/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.StringTokenizer;
import static org.gui.elements.CircuitElement.drawThickCircle;

/**
 *
 * @author paulo
 */
public class Voltmeter extends CircuitElement
{
    final int filament_len = 40;        
    Point filament[],  center;
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
	
	public void setPoints() 
        {
	    super.setPoints();
            filament = newPointArray(2);
            filament[0] = interpPoint(point1, point2, 0, filament_len);
            filament[1] = interpPoint(point1, point2, 1, filament_len);
	    //center = interpPoint(point1, point2, .5);
	}
        
        public void set_name()
        {
            countVoltmeters++;
            name = "V"+countVoltmeters;
        }
        
	public void draw(Graphics g) {
	    int cr = 40;
	    setVoltageColor(g, (volts[0]+volts[1])/2);
	    setPowerColor(g, false);
	    double v1 = volts[0];
            double v2 = volts[1];
	    
            //g.drawString("V", center.x-3, center.y);
            
            setBbox(point1, point2, 30);
            setPowerColor(g, true);
            g.setColor(Color.white);
            setVoltageColor(g, v1);
            drawThickLine(g, point1, filament[0]);
            setVoltageColor(g, v2);
            drawThickLine(g, point2, filament[1]);
            setVoltageColor(g, (v1+v2));
            drawThickLine(g, filament[0], filament[1]);
            //drawThickCircle(g, center.x, center.y, cr);
            drawValues(g, name, cr);
            drawPosts(g);
	}
}
