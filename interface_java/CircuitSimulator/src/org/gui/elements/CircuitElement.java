package org.gui.elements;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.gui.canvas.EditInfo;
import org.gui.canvas.PanelCircuitArea;

public abstract class CircuitElement implements Editable{
    public static double voltageRange = 5;
    public static int colorScaleCount = 32;
    public static int countCapacitors = 0;
    public static int countInductors = 0;
    public static int countResistors = 0;
    public static int countSwitches = 0;
    public static int countDCVoltageSources = 0;
    public static int countACSources = 0;
    public static int countCurrentSources = 0;
    public static int countAmmeters = 0;
    public static int countVoltmeters = 0;
    public static int countWires = 0;
    public static Color colorScale[];
    public static double currentMult, powerMult;
    public static Point ps1, ps2;
    static PanelCircuitArea sim;
    public static Color whiteColor, selectColor, lightGrayColor;
    public static Font unitsFont;
    public static NumberFormat showFormat, shortFormat, noCommaFormat;
    public static final double pi = 3.14159265358979323846;
    protected String name;
    protected boolean plot_voltage;
    protected boolean plot_current;

    protected int x_1, y_1, x_2, y_2, flags, nodes[], voltSource;
    int dx, dy, dsign;
    double dn, dpx1, dpy1;
    Point point1, point2, lead1, lead2;
    double volts[];
    double current, curcount;
    private Rectangle boundingBox;
    boolean noDiagonal;
    private boolean selected;
    protected double value;
    
    public boolean isSelected()
    {
       return selected; 
    }
    
    public void set_value(double v)
    {
        this.value=v;
    }
    
    public boolean getPlotVoltage()
    {
        return plot_voltage;
    }
        
    public boolean getPlotCurrent()
    {
        return plot_current;
    }
    
    public double get_value()
    {
        return this.value;
    }
    
    public void set_name(String name)
    {
        this.name=name;
    }
    
    public Rectangle getBOundingBox()
    {
        return boundingBox;
    }
    
    public int getType()
    {
        return 0;
    }
    
    public int[] getNodes()
    {
        return nodes;
    }
    Class getDumpClass() { return getClass(); }
    int getDefaultFlags() { return 0; }
    public int getFlags()
    {
        return flags;
    }
    
    public void setFlags(int flags)
    {
        this.flags=flags;
    }
    
    public int getX1()
    {
        return x_1;
    }
    public int getY1()
    {
        return y_1;
    }
    
    public void setX1(int x)
    {
        this.x_1=x;
    }
    
    public void setY1(int y)
    {
        this.y_1=y;
    }
    
    public int getX2()
    {
        return x_2;
    }
    public int getY2()
    {
        return y_2;
    }
    
    public void setX2(int x)
    {
        this.x_2=x;
    }
    
    public void setY2(int y)
    {
        this.y_2=y;
    }
    public static void initClass(PanelCircuitArea s) {
	unitsFont = new Font("SansSerif", 0, 10);
	sim = s;
	
	colorScale = new Color[colorScaleCount];
	int i;
	for (i = 0; i != colorScaleCount; i++) {
	    double v = i*2./colorScaleCount - 1;
	    if (v < 0) {
		int n1 = (int) (128*-v)+127;
		int n2 = (int) (127*(1+v));
		colorScale[i] = new Color(n1, n2, n2);
	    } else {
		int n1 = (int) (128*v)+127;
		int n2 = (int) (127*(1-v));
		colorScale[i] = new Color(n2, n1, n2);
	    }
	}
	
	ps1 = new Point();
	ps2 = new Point();

	showFormat = DecimalFormat.getInstance();
	showFormat.setMaximumFractionDigits(2);
	shortFormat = DecimalFormat.getInstance();
	shortFormat.setMaximumFractionDigits(1);
	noCommaFormat = DecimalFormat.getInstance();
	noCommaFormat.setMaximumFractionDigits(10);
	noCommaFormat.setGroupingUsed(false);
    }
    
    public CircuitElement(int xx, int yy) {
	x_1 = x_2 = xx;
	y_1 = y_2 = yy;
	flags = getDefaultFlags();
	allocNodes();
	initBoundingBox();
    }
    public CircuitElement(int xa, int ya, int xb, int yb, int f) {
	x_1 = xa;
        y_1 = ya;
        x_2 = xb;
        y_2 = yb;
        flags = f;
	allocNodes();
	initBoundingBox();
    }
    
    void initBoundingBox() {
	boundingBox = new Rectangle();
	boundingBox.setBounds(min(x_1, x_2), min(y_1, y_2),
			      abs(x_2-x_1)+1, abs(y_2-y_1)+1);
    }
    
    public void set_name()
    {
        name = "ELM";
    }
    void allocNodes() {
	nodes = new int[getNodesCount()+getInternalNodeCount()];
	volts = new double[getNodesCount()+getInternalNodeCount()];
    }
    
    
    public void draw(Graphics g) {}
    public void setCurrent(int x, double c) { current = c; }
    public double getCurrent() { return current; }
    public void doStep() {}
    public void delete() {}
    public void startIteration() {}
    public double getPostVoltage(int x) { return volts[x]; }
    public void setNodeVoltage(int n, double c) {
	volts[n] = c;
	calculateCurrent();
    }
    public void calculateCurrent() {}
    
    public void setPoints() 
    {
	dx = x_2-x_1;
        dy = y_2-y_1;
	dn = Math.sqrt(dx*dx+dy*dy);
	dpx1 = dy/dn;
	dpy1 = -dx/dn;
	dsign = (dy == 0) ? sign(dx) : sign(dy);
	point1 = new Point(x_1 , y_1 );
	point2 = new Point(x_2, y_2);
        
    }
    public void calcLeads(int len) {
	if (dn < len || len == 0) {
	    lead1 = point1;
	    lead2 = point2;
	    return;
	}
	lead1 = interpPoint(point1, point2, (dn-len)/(2*dn));
	lead2 = interpPoint(point1, point2, (dn+len)/(2*dn));
    }
    public Point interpPoint(Point a, Point b, double f) {
	Point p = new Point();
	interpPoint(a, b, p, f);
	return p;
    }
    public void interpPoint(Point a, Point b, Point c, double f) {
	int xpd = b.x-a.x;
	int ypd = b.y-a.y;
	/*double q = (a.x*(1-f)+b.x*f+.48);
	  System.out.println(q + " " + (int) q);*/
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+.48);
    }
    public void interpPoint(Point a, Point b, Point c, double f, double g) {
	int xpd = b.x-a.x;
	int ypd = b.y-a.y;
	int gx = b.y-a.y;
	int gy = a.x-b.x;
	g /= Math.sqrt(gx*gx+gy*gy);
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+g*gx+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+g*gy+.48);
    }
    public Point interpPoint(Point a, Point b, double f, double g) {
	Point p = new Point();
	interpPoint(a, b, p, f, g);
	return p;
    }
    public void interpPoint2(Point a, Point b, Point c, Point d, double f, double g) {
	int xpd = b.x-a.x;
	int ypd = b.y-a.y;
	int gx = b.y-a.y;
	int gy = a.x-b.x;
	g /= Math.sqrt(gx*gx+gy*gy);
	c.x = (int) Math.floor(a.x*(1-f)+b.x*f+g*gx+.48);
	c.y = (int) Math.floor(a.y*(1-f)+b.y*f+g*gy+.48);
	d.x = (int) Math.floor(a.x*(1-f)+b.x*f-g*gx+.48);
	d.y = (int) Math.floor(a.y*(1-f)+b.y*f-g*gy+.48);
    }
    public void draw2Leads(Graphics g) {
	// draw first lead
	setVoltageColor(g, volts[0]);
	drawThickLine(g, point1, lead1);

	// draw second lead
	setVoltageColor(g, volts[1]);
	drawThickLine(g, lead2, point2);
    }
    public Point [] newPointArray(int n) {
	Point a[] = new Point[n];
	while (n > 0)
	    a[--n] = new Point();
	return a;
    }
	
    public void drawDots(Graphics g, Point pa, Point pb, double pos) {
	if (pos == 0)
	    return;
	int dx = pb.x-pa.x;
	int dy = pb.y-pa.y;
	double dn = Math.sqrt(dx*dx+dy*dy);
	g.setColor(Color.yellow);
	int ds = 16;
	pos %= ds;
	if (pos < 0)
	    pos += ds;
	double di = 0;
	for (di = pos; di < dn; di += ds) {
	    int x0 = (int) (pa.x+di*dx/dn);
	    int y0 = (int) (pa.y+di*dy/dn);
	    g.fillRect(x0-1, y0-1, 4, 4);
	}
    }

    public Polygon calcArrow(Point a, Point b, double al, double aw) {
	Polygon poly = new Polygon();
	Point p1 = new Point();
	Point p2 = new Point();
	int adx = b.x-a.x;
	int ady = b.y-a.y;
	double l = Math.sqrt(adx*adx+ady*ady);
	poly.addPoint(b.x, b.y);
	interpPoint2(a, b, p1, p2, 1-al/l, aw);
	poly.addPoint(p1.x, p1.y);
	poly.addPoint(p2.x, p2.y);
	return poly;
    }

    public Polygon calcArrowReverse(Point a, Point b, double al, double aw) {
    	Polygon poly = new Polygon();
	Point p1 = new Point();
	Point p2 = new Point();
	double adx = b.x-a.x;
	double ady = b.y-a.y;
	double l = Math.sqrt(adx*adx+ady*ady);
	if (l > 0)
	{
	    adx /= l;
	    ady /= l;
	    double bdx = -ady; // orthogonal unit vector
	    double bdy = adx;  //
	    poly.addPoint((int)Math.round(b.x+1 - adx*al),
	                  (int)Math.round(b.y+1 - ady*al));
	    poly.addPoint((int)Math.round(b.x+1 - bdx*al),
	                  (int)Math.round(b.y+1 - bdy*aw));
	    poly.addPoint((int)Math.round(b.x+1 + bdx*al),
	                  (int)Math.round(b.y+1 + bdy*aw));
	}
	return poly;
    }

    public Polygon createPolygon(Point a, Point b, Point c) {
	Polygon p = new Polygon();
	p.addPoint(a.x, a.y);
	p.addPoint(b.x, b.y);
	p.addPoint(c.x, c.y);
	return p;
    }
    public Polygon createPolygon(Point a, Point b, Point c, Point d) {
	Polygon p = new Polygon();
	p.addPoint(a.x, a.y);
	p.addPoint(b.x, b.y);
	p.addPoint(c.x, c.y);
	p.addPoint(d.x, d.y);
	return p;
    }
    public Polygon createPolygon(Point a[]) {
	Polygon p = new Polygon();
	int i;
	for (i = 0; i != a.length; i++)
	    p.addPoint(a[i].x, a[i].y);
	return p;
    }
    
    public void drag(int xx, int yy) {
	xx = sim.snapGrid(xx);
	yy = sim.snapGrid(yy);
	
        if (noDiagonal) {
	    if (Math.abs(x_1-xx) < Math.abs(y_1-yy)) {
		xx = x_1;
	    } else {
		yy = y_1;
	    }
	}
	x_2 = xx; 
        y_2 = yy;
	setPoints();
    }
    public void move(int dx, int dy) {
	x_1 += dx; y_1 += dy; x_2 += dx; y_2 += dy;
	boundingBox.move(dx, dy);
	setPoints();
    }

    // determine if moving this element by (dx,dy) will put it on top of another element
    public boolean allowMove(int dx, int dy) {
	int nx = x_1+dx;
	int ny = y_1+dy;
	int nx2 = x_2+dx;
	int ny2 = y_2+dy;
	int i;
	for (i = 0; i != sim.getElementsList().size(); i++)
        {
	    CircuitElement ce = sim.getElm(i);
	    if (ce.x_1 == nx && ce.y_1 == ny && ce.x_2 == nx2 && ce.y_2 == ny2)
		return false;
	    if (ce.x_1 == nx2 && ce.y_1 == ny2 && ce.x_2 == nx && ce.y_2 == ny)
		return false;
	}
	return true;
    }
    
    public void movePoint(int n, int dx, int dy) {
	if (n == 0)
        {
	    x_1 += dx;
            y_1 += dy;
	}
        else
        {
	    x_2 += dx; 
            y_2 += dy;
	}
	setPoints();
    }
    public void drawPosts(Graphics g)
    {
	int i;
	for (i = 0; i != 2; i++)
        {
	    Point p = getPost(i);
            drawPost(g, p.x, p.y);
	}
    }
    
    public void drawVerticalLine(Graphics g)
    {
	int i;
	for (i = 0; i != 2; i++)
        {
            Point p = getPost(i);
            Point px = new Point(p.x,p.y+25);
            Point py = new Point(p.x,p.y+25);
            setVoltageColor(g, volts[0]);
            drawThickLine(g, px, py);
	    
            drawPost(g, p.x, p.y+25);
	}
    }
    
    public int getVoltageSourceCount() { return 0; }
    public int getInternalNodeCount() { return 0; }
    public void setNode(int p, int n)
    {
        nodes[p] = n;
    }
    public void setVoltageSource(int n, int v) { voltSource = v; }
    public int getVoltageSource() { return voltSource; }
    public double getVoltageDiff() {
	return volts[0] - volts[1];
    }
    public boolean nonLinear() { return false; }
    public int getNodesCount()
    {
        return 2;
    }
    public int getNode(int n)
    {
       return nodes[n];
    }
    public Point getPost(int n)
    {
	return (n == 0) ? point1 : (n == 1) ? point2 : null;
    }
    
    
    
    
    public void drawPost(Graphics g, int x0, int y0)
    {
	g.setColor(whiteColor);
	g.fillOval(x0-3, y0-3, 7, 7);
        
    }
    void setBbox(int x1, int y1, int x2, int y2)
    {
	if (x1 > x2)
        {
            int q = x1;
            x1 = x2;
            x2 = q;
        }
	if (y1 > y2)
        {
            int q = y1;
            y1 = y2;
            y2 = q; 
        }
	boundingBox.setBounds(x1, y1, x2-x1+1, y2-y1+1);
    }
    void setBbox(Point p1, Point p2, double w)
    {
	setBbox(p1.x, p1.y, p2.x, p2.y);
	int gx = p2.y-p1.y;
	int gy = p1.x-p2.x;
	int dpx = (int) (dpx1*w);
	int dpy = (int) (dpy1*w);
	adjustBbox(p1.x+dpx, p1.y+dpy, p1.x-dpx, p1.y-dpy);
    }
    void adjustBbox(int x1, int y1, int x2, int y2) {
	if (x1 > x2) { int q = x1; x1 = x2; x2 = q; }
	if (y1 > y2) { int q = y1; y1 = y2; y2 = q; }
	x1 = min(boundingBox.x, x1);
	y1 = min(boundingBox.y, y1);
	x2 = max(boundingBox.x+boundingBox.width-1,  x2);
	y2 = max(boundingBox.y+boundingBox.height-1, y2);
	boundingBox.setBounds(x1, y1, x2-x1, y2-y1);
    }
    public void adjustBbox(Point p1, Point p2) {
	adjustBbox(p1.x, p1.y, p2.x, p2.y);
    }
    public boolean isCenteredText()
    {
        return false;
    }
	
    public void drawCenteredText(Graphics g, String s, int x, int y, boolean cx) {
	FontMetrics fm = g.getFontMetrics();
	int w = fm.stringWidth(s);
	if (cx)
	    x -= w/2;
	g.drawString(s, x, y+fm.getAscent()/2);
	adjustBbox(x, y-fm.getAscent()/2,
		   x+w, y+fm.getAscent()/2+fm.getDescent());
    }

    void drawValues(Graphics g, String s, double hs) {
	if (s == null)
	    return;
	g.setFont(unitsFont);
	FontMetrics fm = g.getFontMetrics();
	int w = fm.stringWidth(s);
        if(this instanceof Voltmeter || this instanceof Ammeter)
        {
            Font myFont = new Font ("Arial Black", 1, 12);
            g.setFont (myFont);
            g.setColor(Color.BLACK);
        }
	
	int ya = fm.getAscent()/2;
	int xc, yc;
	    xc = (x_2+x_1)/2;
	    yc = (y_2+y_1)/2;
	int dpx = (int) (dpx1*hs);
	int dpy = (int) (dpy1*hs);
	if (dpx == 0) {
	    g.drawString(s, xc-w/2, yc-abs(dpy)-2);
	} else {
	    int xx = xc+abs(dpx)+2;
	    if (this instanceof Voltage || (x_1 < x_2 && y_1 > y_2))
		xx = xc-(w+abs(dpx)+2);
	    g.drawString(s, xx, yc+dpy+ya);
	}
    }
    
    void drawCoil(Graphics g, int hs, Point p1, Point p2,
		  double v1, double v2) {
	double len = distance(p1, p2);
	int segments = 30; // 10*(int) (len/10);
	int i;
	double segf = 1./segments;
	    
	ps1.setLocation(p1);
	for (i = 0; i != segments; i++) {
	    double cx = (((i+1)*6.*segf) % 2)-1;
	    double hsx = Math.sqrt(1-cx*cx);
	    if (hsx < 0)
		hsx = -hsx;
	    interpPoint(p1, p2, ps2, i*segf, hsx*hs);
	    double v = v1+(v2-v1)*i/segments;
	    setVoltageColor(g, v);
	    drawThickLine(g, ps1, ps2);
	    ps1.setLocation(ps2);
	}
    }
    static void drawThickLine(Graphics g, int x, int y, int x2, int y2) {
	g.drawLine(x, y, x2, y2);
	g.drawLine(x+1, y, x2+1, y2);
	g.drawLine(x, y+1, x2, y2+1);
	g.drawLine(x+1, y+1, x2+1, y2+1);
    }

    static void drawThickLine(Graphics g, Point pa, Point pb) {
	g.drawLine(pa.x, pa.y, pb.x, pb.y);
	g.drawLine(pa.x+1, pa.y, pb.x+1, pb.y);
	g.drawLine(pa.x, pa.y+1, pb.x, pb.y+1);
	g.drawLine(pa.x+1, pa.y+1, pb.x+1, pb.y+1);
    }

    static void drawThickPolygon(Graphics g, int xs[], int ys[], int c) {
	int i;
	for (i = 0; i != c-1; i++)
	    drawThickLine(g, xs[i], ys[i], xs[i+1], ys[i+1]);
	drawThickLine(g, xs[i], ys[i], xs[0], ys[0]);
    }
    
    static void drawThickPolygon(Graphics g, Polygon p) {
	drawThickPolygon(g, p.xpoints, p.ypoints, p.npoints);
    }
    
    static void drawThickCircle(Graphics g, int cx, int cy, int ri) {
	int a;
	double m = pi/180;
	double r = ri*.98;
	for (a = 0; a != 360; a += 20) {
	    double ax = Math.cos(a*m)*r + cx;
	    double ay = Math.sin(a*m)*r + cy;
	    double bx = Math.cos((a+20)*m)*r + cx;
	    double by = Math.sin((a+20)*m)*r + cy;
	    drawThickLine(g, (int) ax, (int) ay, (int) bx, (int) by);
	}
    }
    
    static String getVoltageDText(double v) {
	return getUnitText(Math.abs(v), "V");
    }
    static String getVoltageText(double v)
    {
	return getUnitText(v, "V");
    }
    
    public String get_name()
    {
        return name;
    }
    static String getUnitText(double v, String u) {
	double va = Math.abs(v);
	if (va < 1e-14)
	    return "0 " + u;
	if (va < 1e-9)
	    return showFormat.format(v*1e12) + " p" + u;
	if (va < 1e-6)
	    return showFormat.format(v*1e9) + " n" + u;
	if (va < 1e-3)
	    return showFormat.format(v*1e6) + " " + PanelCircuitArea.muString + u;
	if (va < 1)
	    return showFormat.format(v*1e3) + " m" + u;
	if (va < 1e3)
	    return showFormat.format(v) + " " + u;
	if (va < 1e6)
	    return showFormat.format(v*1e-3) + " k" + u;
	if (va < 1e9)
	    return showFormat.format(v*1e-6) + " M" + u;
	return showFormat.format(v*1e-9) + " G" + u;
    }
    static String getShortUnitText(double v, String u) {
	double va = Math.abs(v);
	if (va < 1e-13)
	    return null;
	if (va < 1e-9)
	    return shortFormat.format(v*1e12) + "p" + u;
	if (va < 1e-6)
	    return shortFormat.format(v*1e9) + "n" + u;
	if (va < 1e-3)
	    return shortFormat.format(v*1e6) + PanelCircuitArea.muString + u;
	if (va < 1)
	    return shortFormat.format(v*1e3) + "m" + u;
	if (va < 1e3)
	    return shortFormat.format(v) + u;
	if (va < 1e6)
	    return shortFormat.format(v*1e-3) + "k" + u;
	if (va < 1e9)
	    return shortFormat.format(v*1e-6) + "M" + u;
	return shortFormat.format(v*1e-9) + "G" + u;
    }
    static String getCurrentText(double i) {
	return getUnitText(i, "A");
    }
    
    static String getCurrentDText(double i) {
	return getUnitText(Math.abs(i), "A");
    }

    void updateDotCount() {
	curcount = updateDotCount(current, curcount);
    }
    double updateDotCount(double cur, double cc) {
	double cadd = cur*currentMult;
	/*if (cur != 0 && cadd <= .05 && cadd >= -.05)
	  cadd = (cadd < 0) ? -.05 : .05;*/
	cadd %= 8;
	/*if (cadd > 8)
	  cadd = 8;
	  if (cadd < -8)
	  cadd = -8;*/
	return cc + cadd;
    }
    
    void doDots(Graphics g) {
	updateDotCount();
	if (sim.dragElm != this)
	    drawDots(g, point1, point2, curcount);
    }
    void doAdjust() {}
    void setupAdjust() {}
    void getInfo(String arr[]) {
    }
    int getBasicInfo(String arr[]) {
	arr[1] = "I = " + getCurrentDText(getCurrent());
	arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
	return 3;
    }
    void setVoltageColor(Graphics g, double volts) {
	if (needsHighlight()) {
	    g.setColor(selectColor);
	    return;
	}
	int c = (int) ((volts+voltageRange)*(colorScaleCount-1)/
		       (voltageRange*2));
	if (c < 0)
	    c = 0;
	if (c >= colorScaleCount)
	    c = colorScaleCount-1;
	g.setColor(colorScale[c]);
    }
    void setPowerColor(Graphics g, boolean yellow) {
	/*if (conductanceCheckItem.getState()) {
	  setConductanceColor(g, current/getVoltageDiff());
	  return;
	  }*/
	setPowerColor(g, getPower());
    }
    void setPowerColor(Graphics g, double w0) {
	w0 *= powerMult;
	//System.out.println(w);
	double w = (w0 < 0) ? -w0 : w0;
	if (w > 1)
	    w = 1;
	int rg = 128+(int) (w*127);
	int b  = (int) (128*(1-w));
	/*if (yellow)
	  g.setColor(new Color(rg, rg, b));
	  else */
	if (w0 > 0)
	    g.setColor(new Color(rg, b, b));
	else
	    g.setColor(new Color(b, rg, b));
    }
    
    double getPower() { return getVoltageDiff()*current; }
    
    public EditInfo getEditInfo(int n)
    {
        return null;
    }
    
    public void setEditValue(int n, EditInfo ei){}
    
    boolean getConnection(int n1, int n2)
    {
        return true;
    }
    
    boolean hasGroundConnection(int n1)
    {
        return false;
    }
    boolean isWire() { return false; }
    boolean comparePair(int x1, int x2, int y1, int y2) {
	return ((x1 == y1 && x2 == y2) || (x1 == y2 && x2 == y1));
    }
    boolean needsHighlight()
    {
        return sim.mouseElm == this || selected;
    }
    
    
    
    public void setSelected(boolean s)
    {
        selected = s;
    }
    public void selectRect(Rectangle r)
    {
	selected = r.intersects(boundingBox);
    }
    public static int abs(int x)
    {
        return x < 0 ? -x : x;
    }
    public static int sign(int x) 
    {
        return (x < 0) ? -1 : (x == 0) ? 0 : 1; 
    }
    public static int min(int a, int b)
    {
        return (a < b) ? a : b; 
    }
    public static int max(int a, int b)
    {
        return (a > b) ? a : b; 
    }
    public static double distance(Point p1, Point p2)
    {
	double x = p1.x-p2.x;
	double y = p1.y-p2.y;
	return Math.sqrt(x*x+y*y);
    }
    public Rectangle getBoundingBox() 
    {
        return boundingBox;
    }
    public boolean needsShortcut()
    {
        return getShortcut() > 0; 
    }
    public int getShortcut()
    {
        return 0;
    }

    boolean isGraphicElmt()
    {
        return false;
    }
}