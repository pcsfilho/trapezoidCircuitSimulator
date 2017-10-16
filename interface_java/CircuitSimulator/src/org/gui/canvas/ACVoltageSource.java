package org.gui.canvas;

public class ACVoltageSource extends Voltage {
	public ACVoltageSource(int xx, int yy)
        {
            super(xx, yy, WF_AC);
        }
	Class getDumpClass()
        {
            return Voltage.class;
        }
    }
