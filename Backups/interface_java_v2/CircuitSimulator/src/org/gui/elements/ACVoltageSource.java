package org.gui.elements;

public class ACVoltageSource extends Voltage {
	public ACVoltageSource(int xx, int yy)
        {
            super(xx, yy, AC);
        }
	Class getDumpClass()
        {
            return Voltage.class;
        }
        
        public void set_name()
        {
            countACSources++;
            name = "FAC"+countACSources;
        }
        
        public String get_type_current()
        {
            return "AC";
        }
    }
