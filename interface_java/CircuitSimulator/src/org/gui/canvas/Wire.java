package org.gui.canvas;
import java.awt.*;
import java.util.StringTokenizer;

    public class Wire extends Resistor {
	public static boolean ideal = false;
	private static final double defaultResistance = 1E-06;
	public Wire(int xx, int yy) { super(xx, yy); resistance = defaultResistance; }
	public Wire(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) {
	    super(xa, ya, xb, yb, f, new StringTokenizer("0.0"));
	    resistance = defaultResistance;
	}
	static final int FLAG_SHOWCURRENT = 1;
	static final int FLAG_SHOWVOLTAGE = 2;
	void draw(Graphics g) {
	    setVoltageColor(g, volts[0]);
	    drawThickLine(g, point1, point2);
	    doDots(g);
	    setBbox(point1, point2, 3);
	    if (mustShowCurrent()) {
	        String s = getShortUnitText(Math.abs(getCurrent()), "A");
	        drawValues(g, s, 4);
	    } else if (mustShowVoltage()) {
	        String s = getShortUnitText(volts[0], "V");
	        drawValues(g, s, 4);
	    }
	    drawPosts(g);
	}

	void calculateCurrent() {
	    if (!ideal) {
		super.calculateCurrent();
	    }
	}
	void stamp() {
	    if (ideal) {
	    } else {
	    }
	}
	boolean mustShowCurrent() {
	    return (flags & FLAG_SHOWCURRENT) != 0;
	}
	boolean mustShowVoltage() {
	    return (flags & FLAG_SHOWVOLTAGE) != 0;
	}
	int getVoltageSourceCount() {
	    if(ideal) {
		return 1;
	    } else {
		return super.getVoltageSourceCount();
	    }
	}
	void getInfo(String arr[]) {
	    arr[0] = "wire";
	    arr[1] = "I = " + getCurrentDText(getCurrent());
	    arr[2] = "V = " + getVoltageText(volts[0]);
	}
	double getPower() {
	    if (ideal) {
		return 0;
	    } else {
		return super.getPower();
	    }
	}
	double getVoltageDiff() {
	    if (ideal) {
		return volts[0];
	    } else {
		return super.getVoltageDiff();
	    }
	}
	boolean isWire() {
	    return ideal;
	}
	public EditInfo getEditInfo(int n) {
	    if (n == 0) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Show Current", mustShowCurrent());
		return ei;
	    }
	    if (n == 1) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Show Voltage", mustShowVoltage());
		return ei;
	    }
	    return null;
	}
	public void setEditValue(int n, EditInfo ei) {
	    if (n == 0) {
		if (ei.checkbox.getState())
		    flags = FLAG_SHOWCURRENT;
		else
		    flags &= ~FLAG_SHOWCURRENT;
	    }
	    if (n == 1) {
		if (ei.checkbox.getState())
		    flags = FLAG_SHOWVOLTAGE;
		else
		    flags &= ~FLAG_SHOWVOLTAGE;
	    }
	}
        int getShortcut() { return 'f'; }
	int getDumpType() { return 'f'; }
	String dump() {
	    int t = getDumpType();
	    return (t < 127 ? ((char)t)+" " : t+" ") + x + " " + y + " " +
		x2 + " " + y2 + " " + flags;
	}
    }
