package org.gui.canvas;
import org.gui.elements.CircuitElement;
import java.util.ArrayList;

public class CircuitNode {
    private int x, y;
    private ArrayList<CircuitElement> elements;
    
    public CircuitNode()
    {
        elements= new ArrayList<>();
    }
    
    public int getX()
    {
        return x;
    }
    
    public void setX(int x)
    {
        this.x=x;
    }
    
    public void setY(int y)
    {
        this.y=y;
    }
    
    public int getY()
    {
        return y;
    }
    
    public ArrayList<CircuitElement> getElements()
    {
        return elements;
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
