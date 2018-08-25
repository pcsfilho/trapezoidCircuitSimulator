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
				"Amplitude", this.value, -20, 20);
	if (n == 1)
	    return new EditInfo("Frequência (Hz)", this.frequency, 4, 500);
	
	
	return null;
    }
    public void setEditValue(int n, EditInfo ei) {
	if (n == 0)
        {
	    this.value = ei.value;
        }
	if (n == 1) 
        {
	    this.frequency = ei.value;
	}
    }
    }
