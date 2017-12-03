package org.gui.canvas;
import java.util.ArrayList;
import org.gui.elements.CircuitElement;

public class CircuitNode {
    private int x, y,num,ground;
    private boolean reference;
    private ArrayList<CircuitNodeLink> links;
    private ArrayList<CircuitElement> elements;
    
    public CircuitNode()
    {
        links= new ArrayList<>();
        elements= new ArrayList<>();
    }
    
    public boolean IsReferenceNode()
    {
        return reference;
    }
    
    public void setLikeReferenceNode()
    {
        reference=true;
    }
    
    public int getGroundNode()
    {
        return ground;
    }
    
    public void setGroundNode(int g)
    {
        this.ground=g;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getNum()
    {
        return num;
    }
    
    public void setNum(int num)
    {
        this.num=num;
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
    
    public ArrayList<CircuitNodeLink> getLinks()
    {
        return links;
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
