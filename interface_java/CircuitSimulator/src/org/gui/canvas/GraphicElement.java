package org.gui.canvas;
public class GraphicElement extends CircuitElement
{

    public GraphicElement(int xx, int yy)
    {
	super(xx,yy);
    }

    public GraphicElement(int xa, int ya, int xb, int yb, int flags)
    {
	super(xa, ya, xb, yb, flags);
    }

    int getPostCount() { return 0; }
}

