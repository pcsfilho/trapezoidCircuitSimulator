package org.gui.elements;
import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.gui.canvas.EditInfo;

public class Switch extends CircuitElement {
    protected boolean state;
    private ArrayList<Double> time_commutations;
    Point ps, ps2;
    
    public Switch(int xx, int yy)
    {
	super(xx, yy);
	state = false;
        time_commutations= new ArrayList<>();
    }
    
    Switch(int xx, int yy, boolean mm) 
    {
	super(xx, yy);
	state = mm;
        time_commutations= new ArrayList<>();
    }
    
    public Switch(int xa, int ya, int xb, int yb, int f,
		     StringTokenizer st) {
	super(xa, ya, xb, yb, f);
	String str = st.nextToken();
    }
    
    public void changeState()
    {
        state =!state;
    }
    
    public void addTimeCommutations(double time)
    {
        time_commutations.add(time);
    }
    public boolean getState()
    {
        return state;
    }
    
    public void set_name()
    {
        countSwitches++;
        name = "CH"+countSwitches;
    }
    
    public int getType()
    {
        return 'S';
    }
    
    
    public void setPoints() {
	super.setPoints();
	calcLeads(32);
	ps  = new Point();
	ps2 = new Point();
    }
	
    public void draw(Graphics g) {
	int openhs = 16;
	int hs1 = (state) ? 0 : 2;
	int hs2 = (state) ? openhs : 2;
	setBbox(point1, point2, openhs);

	draw2Leads(g);
	    
	if (!state)
	    doDots(g);
	    
	if (!needsHighlight())
	    g.setColor(whiteColor);
	interpPoint(lead1, lead2, ps,  0, hs1);
	interpPoint(lead1, lead2, ps2, 1, hs2);
	    
	drawThickLine(g, ps, ps2);
	drawPosts(g);
        drawValues(g, name, 10);
    }
    
    @Override
    public EditInfo getEditInfo(int n)
    {
	if (n == 0)
        {
	    EditInfo ei = new EditInfo("Estado Inicial: ", 0, -1, -1);
	    ei.checkbox = new Checkbox("Fechado", state);
	    return ei;
	}
        
	return null;
    }
    
    @Override
    public void setEditValue(int n, EditInfo ei)
    {
	if (n == 0)
	    state = ei.checkbox.getState();
    }
}
