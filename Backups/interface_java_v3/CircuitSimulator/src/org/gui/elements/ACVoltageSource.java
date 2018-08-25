package org.gui.elements;

import org.gui.canvas.EditInfo;

public class ACVoltageSource extends Voltage {
	public ACVoltageSource(int xx, int yy)
        {
            super(xx, yy, AC);
        }
        /**
        * 
        * @return 
        */
	Class getDumpClass()
        {
            return Voltage.class;
        }
        
        public void set_name()
        {
            countACSources++;
            name = "FAC"+countACSources;
        }
        /**
        * 
        * @return 
        */
        public String get_type_current()
        {
            return "AC";
        }
        
        public EditInfo getEditInfo(int n) {
	if (n == 0)
	    return new EditInfo(waveform == DC ? "Tensão" :
				"Amplitude", value, -20, 20);
	if (n == 1)
	    return new EditInfo("Frequência (Hz)", frequency, 4, 500);
	
	
	return null;
    }
    public void setEditValue(int n, EditInfo ei) {
	if (n == 0)
        {
	    value = ei.value;
        }
	if (n == 1) 
        {
	    frequency = ei.value;
	}
    }
    }
