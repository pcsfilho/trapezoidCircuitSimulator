package org.canvas;    
public class DCVoltageSource extends Voltage
{
	public DCVoltageSource(int xx, int yy) { super(xx, yy, WF_DC); }
	Class getDumpClass() { return Voltage.class; }
	int getShortcut() { return 'v'; }
}
