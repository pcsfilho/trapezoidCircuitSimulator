package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

public class Voltage extends CircuitElement {
    static final int FLAG_COS = 2;
    int waveform;
    static final int DC = 0;
    static final int AC = 1;
    static final int WF_VAR = 6;
    final int circleSize = 17;
    protected double frequency, freqTimeZero, bias,phaseShift, dutyCycle;
    
    Voltage(int xx, int yy, int wf)
    {
	super(xx, yy);
	waveform = wf;
	value = 100;
	frequency = 60;
	dutyCycle = .5;
    }
    
    public double get_frequency()
    {
        System.out.println("Frequencia: "+frequency);
        return frequency;
    }
    
    public void set_frequency(double f)
    {
        this.frequency=f;
    }
    
    public Voltage(int xa, int ya, int xb, int yb, int f,
		      StringTokenizer st) {
	super(xa, ya, xb, yb, f);
	value = 5;
	frequency = 40;
	waveform = DC;
	dutyCycle = .5;
	try {
	    waveform = new Integer(st.nextToken()).intValue();
	    frequency = new Double(st.nextToken()).doubleValue();
	    value = new Double(st.nextToken()).doubleValue();
	    bias = new Double(st.nextToken()).doubleValue();
	    phaseShift = new Double(st.nextToken()).doubleValue();
	    dutyCycle = new Double(st.nextToken()).doubleValue();
	} catch (Exception e) {
	}
	if ((flags & FLAG_COS) != 0) {
	    flags &= ~FLAG_COS;
	    phaseShift = pi/2;
	}
    }
    
    public int getType() 
    {
        return 'V';
    }
    
    public String get_type_current()
    {
        return null;
    }

    public void setPoints()
    {
	super.setPoints();
	calcLeads((waveform == DC || waveform == WF_VAR) ? 8 : circleSize*2);
    }
    
    public void draw(Graphics g) {
	setBbox(x_1, y_1, x_2, y_2);
	draw2Leads(g);
	if (waveform == DC) {
	    setPowerColor(g, false);
	    setVoltageColor(g, volts[0]);
	    interpPoint2(lead1, lead2, ps1, ps2, 0, 16);
	    drawThickLine(g, ps1, ps2);
	    setVoltageColor(g, volts[1]);
	    int hs = 10;
	    setBbox(point1, point2, hs);
	    interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
	    drawThickLine(g, ps1, ps2);
            drawValues(g, name, 20);
	} else {
	    setBbox(point1, point2, circleSize);
	    interpPoint(lead1, lead2, ps1, .5);
	    drawWaveform(g, ps1);
	}
	updateDotCount();
	if (sim.dragElm != this) {
	    if (waveform == DC)
		drawDots(g, point1, point2, curcount);
	    else {
		drawDots(g, point1, lead1, curcount);
		drawDots(g, point2, lead2, -curcount);
	    }
	}
	drawPosts(g);
    }
	
    void drawWaveform(Graphics g, Point center) {
	g.setColor(needsHighlight() ? selectColor : Color.gray);
	setPowerColor(g, false);
	int xc = center.x; int yc = center.y;
	drawThickCircle(g, xc, yc, circleSize);
	int wl = 8;
	adjustBbox(xc-circleSize, yc-circleSize,
		   xc+circleSize, yc+circleSize);
	int xc2;
	switch (waveform)
        {
            case DC:
            {
                break;
            }
            case AC:
            {
                int i;
                int xl = 10;
                int ox = -1, oy = -1;
                for (i = -xl; i <= xl; i++) {
                    int yy = yc+(int) (.95*Math.sin(i*pi/xl)*wl);
                    if (ox != -1)
                        drawThickLine(g, ox, oy, xc+i, yy);
                    ox = xc+i; oy = yy;
                }
                drawValues(g, name, circleSize);
                break;
            }
	}
        
    }
}
