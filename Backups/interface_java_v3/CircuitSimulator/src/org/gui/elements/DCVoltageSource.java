package org.gui.elements;    

import org.gui.canvas.EditInfo;
import static org.gui.elements.Voltage.DC;

public class DCVoltageSource extends Voltage
{
    /**
     * 
     * @param xx
     * @param yy 
     */
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
        
        public EditInfo getEditInfo(int n) {
	if (n == 0)
	    return new EditInfo(waveform == DC ? "Tensão" :
				"Amplitude", value, -20, 20);
	return null;
    }
    public void setEditValue(int n, EditInfo ei) {
	if (n == 0)
        {
	    value = ei.value;
        }
    }
}
