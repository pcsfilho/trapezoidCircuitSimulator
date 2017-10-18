package org.gui.canvas;
import org.gui.elements.CircuitElement;
import java.util.ArrayList;

class CircuitNode {
    int x, y;
    ArrayList<CircuitNodeLink> links;
    boolean internal;
    ArrayList<CircuitElement> elements;
    CircuitNode()
    {
        links = new ArrayList<>();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object != null && object instanceof CircuitNode)
        {
            if(((CircuitNode)object).x==x && ((CircuitNode)object).y==y)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.x;
        hash = 41 * hash + this.y;
        return hash;
    }
}
