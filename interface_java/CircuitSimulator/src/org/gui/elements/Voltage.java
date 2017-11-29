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
    protected double frequency, freqTimeZero, bias,phaseShift, dutyCycle;
    
    Voltage(int xx, int yy, int wf)
    {
	super(xx, yy);
	waveform = wf;
	value = 100;
	frequency = 40;
	dutyCycle = .5;
    }
    
    public double get_frequency()
    {
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

    double getVoltage() {
	//double w = 2*pi*(sim.t-freqTimeZero)*frequency + phaseShift;
        double w=0;
	switch (waveform) {
	case DC: return value+bias;
	case AC: return Math.sin(w)*value+bias;
	default: return 0;
	}
    }
    final int circleSize = 17;
    
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
	
    public int getVoltageSourceCount() {
	return 1;
    }
    double getPower() { return -getVoltageDiff()*current; }
    public double getVoltageDiff() { return volts[1] - volts[0]; }
    void getInfo(String arr[]) {
	switch (waveform)
        {
            case DC: case WF_VAR:
                arr[0] = "fonte de tensão";
                break;
            case AC:       
                arr[0] = "fonte A/C"; 
                break;
	}
	arr[1] = "I = " + getCurrentText(getCurrent());
	if (waveform != DC && waveform != WF_VAR) {
	    arr[3] = "f = " + getUnitText(frequency, "Hz");
	    arr[4] = "Vmax = " + getVoltageText(value);
	    int i = 5;
	    if (bias != 0)
		arr[i++] = "Voff = " + getVoltageText(bias);
	    else if (frequency > 500)
		arr[i++] = "wavelength = " + getUnitText(2.9979e8/frequency, "m");
	    arr[i++] = "P = " + getUnitText(getPower(), "W");
	}
    }
    public EditInfo getEditInfo(int n) {
	if (n == 0)
	    return new EditInfo(waveform == DC ? "Tensão" :
				"Amplitude", value, -20, 20);
	if (n == 1) {
	    EditInfo ei =  new EditInfo("Waveform", waveform, -1, -1);
	    ei.choice = new Choice();
	    ei.choice.add("D/C");
	    ei.choice.add("A/C");
	    return ei;
	}
	if (waveform == DC)
	    return null;
	if (n == 2)
	    return new EditInfo("Frequência (Hz)", frequency, 4, 500);
	if (n == 3)
	    return new EditInfo("Deslocamento DC (V)", bias, -20, 20);
	if (n == 4)
	    return new EditInfo("Deslocamento de fase (graus)", phaseShift*180/pi,
				-180, 180).setDimensionless();
	
	return null;
    }
    public void setEditValue(int n, EditInfo ei) {
	if (n == 0)
	    value = ei.value;
	if (n == 3)
	    bias = ei.value;
	if (n == 2) {
	    // adjust time zero to maintain continuity ind the waveform
	    // even though the frequency has changed.
	    double oldfreq = frequency;
	    frequency = ei.value;
	    //double maxfreq = 1/(8*sim.timeStep);
            double maxfreq = 0;
	    if (frequency > maxfreq)
		frequency = maxfreq;
	    double adj = frequency-oldfreq;
	    //freqTimeZero = sim.t-oldfreq*(sim.t-freqTimeZero)/frequency;
            freqTimeZero = 0;
	}
	if (n == 1) {
	    int ow = waveform;
	    waveform = ei.choice.getSelectedIndex();
	    if (waveform == DC && ow != DC)
            {
		ei.newDialog = true;
		bias = 0;
	    } else if (waveform != DC && ow == DC) 
            {
		ei.newDialog = true;
	    }
	    setPoints();
	}
	if (n == 4)
	    phaseShift = ei.value*pi/180;
	if (n == 5)
	    dutyCycle = ei.value*.01;
    }
}
