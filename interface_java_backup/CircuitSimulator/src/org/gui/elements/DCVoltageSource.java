package org.gui.elements;    
public class DCVoltageSource extends Voltage
{
	public DCVoltageSource(int xx, int yy)
        {
            super(xx, yy, DC);
        }
        
        public String get_type_current()
        {
            return "DC";
        }
        
	public Class getDumpClass()
        {
            return Voltage.class; 
        }
	
        
        public void set_name()
        {
            countDCVoltageSources++;
            name = "F"+countDCVoltageSources;
        }
}
