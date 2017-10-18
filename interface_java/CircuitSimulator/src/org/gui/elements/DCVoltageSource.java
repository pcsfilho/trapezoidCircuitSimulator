package org.gui.elements;    
public class DCVoltageSource extends Voltage
{
	public DCVoltageSource(int xx, int yy) { super(xx, yy, WF_DC); }
	public Class getDumpClass() { return Voltage.class; }
	public int getShortcut() { return 'v'; }
        
        public void set_name()
        {
            countDCVoltageSources++;
            name = "F"+countDCVoltageSources;
        }
}
