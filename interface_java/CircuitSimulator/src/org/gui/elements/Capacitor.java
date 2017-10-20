package org.gui.elements;
import java.awt.*;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

    public class Capacitor extends CircuitElement {
	double capacitance;
	Point plate1[], plate2[];
	public Capacitor(int xx, int yy) {
	    super(xx, yy);
	    capacitance = 1e-5;
	}
	public Capacitor(int xa, int ya, int xb, int yb, int f,StringTokenizer st)
        {
	    super(xa, ya, xb, yb, f);
	    capacitance = new Double(st.nextToken()).doubleValue();
	}
	
        public int getDumpType()
        {
            return 'c';
        }

        public String dump()
        {
	    return super.dump() + " " + capacitance;
	}
	
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
            String s = getShortUnitText(capacitance, "F");
            drawValues(g, name, hs);
	}

        public void set_name()
        {
            countCapacitors++;
            name = "C"+countCapacitors;
            System.out.println(name);
        }
        
        void getInfo(String arr[])
        {
	    arr[0] = "capacitor";
	    getBasicInfo(arr);
	    arr[3] = "C = " + getUnitText(capacitance, "F");
	    arr[4] = "P = " + getUnitText(getPower(), "W");
	}
	
        public EditInfo getEditInfo(int n)
        {
            if(n==0)
            {
                return new EditInfo("Capacitância (F)", capacitance, 0, 0);
            }
            return null;
	}
        
	public void setEditValue(int n, EditInfo ei)
        {
            capacitance = ei.value;
	}
    }
