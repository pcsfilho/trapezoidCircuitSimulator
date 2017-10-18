package org.gui.canvas;        
import org.gui.MainWindow;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author paulo
 */
public class PanelCircuitArea extends JPanel implements ComponentListener, ActionListener, AdjustmentListener,
  MouseMotionListener, MouseListener, ItemListener, KeyListener{
    //region ATRIBUTTES
    Dimension winSize;
    Rectangle selectedArea;
    Image dbimage;
    static final int MODE_ADD_ELM = 0;
    static final int MODE_DRAG_ALL = 1;
    static final int MODE_DRAG_ROW = 2;
    static final int MODE_DRAG_COLUMN = 3;
    static final int MODE_DRAG_SELECTED = 4;
    static final int MODE_DRAG_POST = 5;
    static final int MODE_SELECT = 6;
    int draggingPost;
    int dragX, dragY, initDragX, initDragY;
    int mouseMode = MODE_SELECT;
    int pause = 10;
    int tempMouseMode = MODE_SELECT;
    String mouseModeStr = "Select";
    MainWindow frame_parent;
    CircuitElement plotXElm;
    CircuitElement plotYElm;
    CircuitCanvas cv;
    CheckboxMenuItem smallGridCheckItem;
    static EditDialog editDialog;
    MenuItem elmEditMenuItem;
    int mousePost = -1;
    int menuScope = -1;
    MenuItem optionsItem;
    PopupMenu elmMenu;
    Rectangle circuitArea;
    int gridSize, gridMask, gridRound, groundCount=0;
    ArrayList<CircuitElement> elmList;
    ArrayList<String> undoStack, redoStack;
    CircuitElement dragElm;
    CircuitElement menuElm, mouseElm, stopElm;
    static String muString = "u";
    static String ohmString = "ohm";
    boolean didSwitch = false;
    Switch heldSwitchElm;
    ArrayList<CircuitNode> nodeList;
    //endregion
    
    public PanelCircuitArea(MainWindow frame)
    {
        this.frame_parent=frame;
        init();
    }
    
    public void init() {
	CircuitElement.initClass(this);
	
	setLayout(new CircuitLayout());
	cv = new CircuitCanvas(this);
	cv.addComponentListener(this);
	cv.addMouseMotionListener(this);
	cv.addMouseListener(this);
	cv.addKeyListener(this);
	this.add(cv);

        smallGridCheckItem = getCheckItem("Small Grid");

        setClassElement(Wire.class.getCanonicalName());
	setClassElement(Resistor.class.getCanonicalName());

	
	setClassElement(Capacitor.class.getCanonicalName());
	setClassElement(Inductor.class.getCanonicalName());
	setClassElement(Switch.class.getCanonicalName());
	setClassElement(Ground.class.getCanonicalName());
	setClassElement(DCVoltageSource.class.getCanonicalName());
	setClassElement(ACVoltageSource.class.getCanonicalName());
        setClassElement(CurrentSource.class.getCanonicalName());
	setClassElement(Output.class.getCanonicalName());
	
	setGrid();
	elmList = new ArrayList<>();

	//cv.setBackground(Color.black);
	cv.setForeground(Color.BLACK);
	
        elmMenu = new PopupMenu();
	elmMenu.add(elmEditMenuItem = getMenuItem("Editar"));
        add(elmMenu);

        handleResize();
	requestFocus();
    }
    
    public CircuitElement getElm(int n) {
        
	if (n >= elmList.size())
	    return null;
	return elmList.get(n);
    }
    
    int min(int a, int b)
    {
        return (a < b) ? a : b;
    }
    int max(int a, int b)
    {
        return (a > b) ? a : b;
    }
    
    void handleResize() {
        winSize = cv.getSize();
	if (winSize.width == 0)
	    return;
	dbimage = createImage(winSize.width, winSize.height);
	circuitArea = new Rectangle(0, 0, winSize.width, winSize.height);
        
	int i;
	int minx = 1000, maxx = 0, miny = 1000, maxy = 0;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    // centered text causes problems when trying to center the circuit,
	    // so we special-case it here
	    if (!ce.isCenteredText()) {
		minx = min(ce.x, min(ce.x2, minx));
		maxx = max(ce.x, max(ce.x2, maxx));
	    }
	    miny = min(ce.y, min(ce.y2, miny));
	    maxy = max(ce.y, max(ce.y2, maxy));
	}
	// center circuit; we don't use snapGrid() because that rounds
	int dx = gridMask & ((circuitArea.width -(maxx-minx))/2-minx);
	int dy = gridMask & ((circuitArea.height-(maxy-miny))/2-miny);
	if (dx+minx < 0)
	    dx = gridMask & (-minx);
	if (dy+miny < 0)
	    dy = gridMask & (-miny);
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.move(dx, dy);
	}
    }
    
    
    MenuItem getMenuItem(String s) {
	MenuItem mi = new MenuItem(s);
	mi.addActionListener(this);
	return mi;
    }

    
    int snapGrid(int x)
    {
	return (x+gridRound) & gridMask;
    }
    
    CircuitElement constructElement(Class c, int x0, int y0) {
	// find element class
	Class carr[] = new Class[2];
	//carr[0] = getClass();
	carr[0] = carr[1] = int.class;
	Constructor cstr = null;
	try {
	    cstr = c.getConstructor(carr);
	} catch (NoSuchMethodException ee) {
	    System.out.println("caught NoSuchMethodException " + c);
	    return null;
	} catch (Exception ee) {
	    ee.printStackTrace();
	    return null;
	}

	// invoke constructor with starting coordinates
	Object oarr[] = new Object[2];
	oarr[0] = new Integer(x0);
	oarr[1] = new Integer(y0);
	try {
	    return (CircuitElement) cstr.newInstance(oarr);
	} catch (Exception ee) { ee.printStackTrace(); }
	return null;
    }
    
    void register(Class c, CircuitElement elm) {
	int t = elm.getDumpType();
	if (t == 0) {
	    System.out.println("no dump type: " + c);
	    return;
	}

	int s = elm.getShortcut();
	if ( elm.needsShortcut() && s == 0 )
	{
	    if ( s == 0 )
	    {
		System.err.println("no shortcut " + c + " for " + c);
		return;
	    }
	    else if ( s <= ' ' || s >= 127 )
	    {
		System.err.println("invalid shortcut " + c + " for " + c);
		return;
	    }
	}
    }
    
    void setClassElement(String t)
    {
	try
        {
            Class c = Class.forName(t);
            CircuitElement elm = constructElement(c, 0, 0);
            register(c, elm);
            elm.delete();
	} catch (ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(frame_parent,e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        }
    }

    void readSetup(byte b[], int len, boolean retain)
    {
	cv.repaint();
	
    }
    
    void setGrid()
    {
	gridSize = (smallGridCheckItem.getState()) ? 8 : 16;
	gridMask = ~(gridSize-1);
	gridRound = gridSize/2-1;
    }
    
    MenuItem getMenuItem(String s, String ac) {
	MenuItem mi = new MenuItem(s);
	mi.setActionCommand(ac);
	mi.addActionListener(this);
	return mi;
    }
    
    CheckboxMenuItem getCheckItem(String s) {
	CheckboxMenuItem mi = new CheckboxMenuItem(s);
	mi.addItemListener(this);
	mi.setActionCommand("");
	return mi;
    }
    
    /**
     * 
     * @param realg 
     */
    public void updateCircuit(Graphics realg)
    {
        if (editDialog != null && editDialog.elm instanceof CircuitElement)
        {
	    mouseElm = (CircuitElement) (editDialog.elm);
        }        
        Graphics2D g = null;
	g = (Graphics2D)dbimage.getGraphics();
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	CircuitElement.selectColor = Color.BLUE;
        CircuitElement.whiteColor = Color.black;
        CircuitElement.lightGrayColor = Color.black;
        g.setColor(Color.white);
	g.fillRect(0, 0, winSize.width, winSize.height);
	
	int i;
	for (i = 0; i != elmList.size(); i++)
        {
            getElm(i).draw(g);
	}

	if (dragElm != null && (dragElm.x != dragElm.x2 || dragElm.y != dragElm.y2))
        {
            dragElm.draw(g);
            //System.out.println("element: "+dragElm.toString());
            //System.out.println("x: "+dragElm.x + "y: "+dragElm.y);
            //System.out.println("x2: "+dragElm.x2 + "y2: "+dragElm.y2);
        }
	
	g.setColor(CircuitElement.whiteColor);
	
	if (selectedArea != null)
        {
	    g.setColor(CircuitElement.selectColor);
	    g.drawRect(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
	}
	realg.drawImage(dbimage, 0, 0, this);
    }
    
    //region ABSTRACTS_METHODS
    @Override
    public void componentResized(ComponentEvent e) {
        handleResize();
	cv.repaint(pause);
    }
    
    void removeZeroLengthElements() {
	int i;
	
	for (i = elmList.size()-1; i >= 0; i--) {
	    CircuitElement ce = getElm(i);
	    if (ce.x == ce.x2 && ce.y == ce.y2) {
		elmList.remove(i);
		ce.delete();
	    }
	}
	
    }

    @Override
    public void componentMoved(ComponentEvent e){ }

    void dragAll(int x, int y) {
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.move(dx, dy);
	}
	removeZeroLengthElements();
    }

    void dragRow(int x, int y) {
	int dy = y-dragY;
	if (dy == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    if (ce.y  == dragY)
		ce.movePoint(0, 0, dy);
	    if (ce.y2 == dragY)
		ce.movePoint(1, 0, dy);
	}
	removeZeroLengthElements();
    }
    
    void dragColumn(int x, int y) {
	int dx = x-dragX;
	if (dx == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    if (ce.x  == dragX)
		ce.movePoint(0, dx, 0);
	    if (ce.x2 == dragX)
		ce.movePoint(1, dx, 0);
	}
	removeZeroLengthElements();
    }

    boolean dragSelected(int x, int y) {
	boolean me = false;
	if (mouseElm != null && !mouseElm.isSelected())
	    mouseElm.setSelected(me = true);

	// snap grid, unless we're only dragging text elements
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    if ( ce.isSelected() && !(ce instanceof GraphicElement) )
		break;
	}
	if (i != elmList.size()) {
	    x = snapGrid(x);
	    y = snapGrid(y);
	}
	
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0) {
	    // don't leave mouseElm selected if we selected it above
	    if (me)
		mouseElm.setSelected(false);
	    return false;
	}
	boolean allowed = true;

	// check if moves are allowed
	for (i = 0; allowed && i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    if (ce.isSelected() && !ce.allowMove(dx, dy))
		allowed = false;
	}

	if (allowed) {
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElm(i);
		if (ce.isSelected())
		    ce.move(dx, dy);
	    }
	    
	}
	
	// don't leave mouseElm selected if we selected it above
	if (me)
	    mouseElm.setSelected(false);
	
	return allowed;
    }

    void dragPost(int x, int y) {
	if (draggingPost == -1) {
	    draggingPost =
		(distanceSq(mouseElm.x , mouseElm.y , x, y) >
		 distanceSq(mouseElm.x2, mouseElm.y2, x, y)) ? 1 : 0;
	}
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0)
	    return;
	mouseElm.movePoint(draggingPost, dx, dy);
	
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {

    }

    void selectArea(int x, int y) {
        
	int x1 = min(x, initDragX);
	int x2 = max(x, initDragX);
	int y1 = min(y, initDragY);
	int y2 = max(y, initDragY);
	selectedArea = new Rectangle(x1, y1, x2-x1, y2-y1);
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.selectRect(selectedArea);
	}
    }

    void setSelectedElm(CircuitElement cs) {
        
	int i;
        
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.setSelected(ce == cs);
	}
	mouseElm = cs;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        
        // ignore right mouse button with no modifiers (needed on PC)
	if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
	    int ex = e.getModifiersEx();
	    if ((ex & (MouseEvent.META_DOWN_MASK|
		       MouseEvent.SHIFT_DOWN_MASK|
		       MouseEvent.CTRL_DOWN_MASK|
		       MouseEvent.ALT_DOWN_MASK)) == 0)
		return;
	}
	if (!circuitArea.contains(e.getX(), e.getY()))
	    return;
	if (dragElm != null)
	    dragElm.drag(e.getX(), e.getY());
	boolean success = true;
	switch (tempMouseMode) {
	case MODE_DRAG_ALL:
	    dragAll(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_ROW:
	    dragRow(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_COLUMN:
	    dragColumn(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_POST:
	    if (mouseElm != null)
		dragPost(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_SELECT:
	    if (mouseElm == null)
		selectArea(e.getX(), e.getY());
	    else {
		tempMouseMode = MODE_DRAG_SELECTED;
		success = dragSelected(e.getX(), e.getY());
	    }
	    break;
	case MODE_DRAG_SELECTED:
	    success = dragSelected(e.getX(), e.getY());
	    break;
	}
	dragging = true;
	if (success) {
	    if (tempMouseMode == MODE_DRAG_SELECTED && mouseElm instanceof GraphicElement) {
		dragX = e.getX(); dragY = e.getY();
	    } else {
		dragX = snapGrid(e.getX()); dragY = snapGrid(e.getY());
	    }
	}
	cv.repaint(pause);
    }

    int distanceSq(int x1, int y1, int x2, int y2)
    {
	x2 -= x1;
	y2 -= y1;
	return x2*x2+y2*y2;
    }
    
       
    @Override
    public void mouseMoved(MouseEvent e) {
        if(getElm(0)!=null)
        
        
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
	    return;
	int x = e.getX();
	int y = e.getY();
	dragX = snapGrid(x); dragY = snapGrid(y);
	draggingPost = -1;
	int i;
	CircuitElement origMouse = mouseElm;
        if(origMouse!=null)
        
	mouseElm = null;
	mousePost = -1;
	plotXElm = plotYElm = null;
	int bestDist = 100000;
	int bestArea = 100000;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
        
	    if (ce.boundingBox.contains(x, y)) {
		int j;
		int area = ce.boundingBox.width * ce.boundingBox.height;
		int jn = ce.getNodesCount();
		if (jn > 2)
		    jn = 2;
		for (j = 0; j != jn; j++) {
		    Point pt = ce.getPost(j);
		    int dist = distanceSq(x, y, pt.x, pt.y);

		    // if multiple elements have overlapping bounding boxes,
		    // we prefer selecting elements that have posts close
		    // to the mouse pointer and that have a small bounding
		    // box area.
		    if (dist <= bestDist && area <= bestArea) {
			bestDist = dist;
			bestArea = area;
			mouseElm = ce;
		    }
		}
		if (ce.getNodesCount() == 0)
		    mouseElm = ce;
	    }
	}
	if (mouseElm == null) {
	    // the mouse pointer was not in any of the bounding boxes, but we
	    // might still be close to a post
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElm(i);
		int j;
		int jn = ce.getNodesCount();
		for (j = 0; j != jn; j++) {
		    Point pt = ce.getPost(j);
		    int dist = distanceSq(x, y, pt.x, pt.y);
		    if (distanceSq(pt.x, pt.y, x, y) < 26) {
			mouseElm = ce;
			mousePost = j;
			break;
		    }
		}
	    }
	} else {
	    mousePost = -1;
	    // look for post close to the mouse pointer
	    for (i = 0; i != mouseElm.getNodesCount(); i++) {
		Point pt = mouseElm.getPost(i);
		if (distanceSq(pt.x, pt.y, x, y) < 26)
		    mousePost = i;
	    }
	}
	if (mouseElm != origMouse)
	    cv.repaint();
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !didSwitch )
	    doEditMenu(e);
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
	    if (mouseMode == MODE_SELECT || mouseMode == MODE_DRAG_SELECTED)
		clearSelection();
	}
    }
    
    void clearSelection() {
        
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.setSelected(false);
	}
    }
   
    void doPopupMenu(MouseEvent e) {
	menuElm = mouseElm;
	if (mouseElm != null) {
	    elmMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }
    
    void doEditMenu(MouseEvent e) {
	if( mouseElm != null )
	    doEdit(mouseElm);
    }
    //Criação de dialogo para edição do componente
    void doEdit(Editable eable) {
        System.out.println("DOEDIT");
	clearSelection();
	if (editDialog != null) {
	    requestFocus();
	    editDialog.setVisible(false);
	    editDialog = null;
	}
	editDialog = new EditDialog(eable, this, frame_parent);
	editDialog.setVisible(true);
    }
    
    void doMainMenuChecks(Menu m) {
        
	int i;
	for (i = 0; i != m.getItemCount(); i++) {
	    MenuItem mc = m.getItem(i);
	    if (mc instanceof Menu)
		doMainMenuChecks((Menu) mc);
	    if (mc instanceof CheckboxMenuItem) {
		CheckboxMenuItem cmi = (CheckboxMenuItem) mc;
		cmi.setState(
		      mouseModeStr.compareTo(cmi.getActionCommand()) == 0);
	    }
	}
    }
    
    
    boolean doSwitch(int x, int y) {
        
	if (mouseElm == null || !(mouseElm instanceof Switch))
	    return false;
	Switch se = (Switch) mouseElm;
	se.toggle();
	if (se.momentary)
	    heldSwitchElm = se;
	
	return true;
    }
boolean dragging;
    int locateElm(CircuitElement elm) {
        
	int i;
	for (i = 0; i != elmList.size(); i++)
	    if (elm == elmList.get(i))
		return i;
	return -1;
    }
    Class addingClass;
    
    @Override
    public void mousePressed(MouseEvent e) {
        
        didSwitch = false;
        
	int ex = e.getModifiersEx();
	if ((ex & (MouseEvent.META_DOWN_MASK|
		   MouseEvent.SHIFT_DOWN_MASK)) == 0 && e.isPopupTrigger()) {
	    doPopupMenu(e);
	    return;
	}
	if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
	    // left mouse
	    tempMouseMode = mouseMode;
	    if ((ex & MouseEvent.ALT_DOWN_MASK) != 0 &&
		(ex & MouseEvent.META_DOWN_MASK) != 0)
		tempMouseMode = MODE_DRAG_COLUMN;
	    else if ((ex & MouseEvent.ALT_DOWN_MASK) != 0 &&
		     (ex & MouseEvent.SHIFT_DOWN_MASK) != 0)
		tempMouseMode = MODE_DRAG_ROW;
	    else if ((ex & MouseEvent.SHIFT_DOWN_MASK) != 0)
		tempMouseMode = MODE_SELECT;
	    else if ((ex & MouseEvent.ALT_DOWN_MASK) != 0)
		tempMouseMode = MODE_DRAG_ALL;
	    else if ((ex & (MouseEvent.CTRL_DOWN_MASK|
			    MouseEvent.META_DOWN_MASK)) != 0)
		tempMouseMode = MODE_DRAG_POST;
	} else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
	    // right mouse
	    if ((ex & MouseEvent.SHIFT_DOWN_MASK) != 0)
		tempMouseMode = MODE_DRAG_ROW;
	    else if ((ex & (MouseEvent.CTRL_DOWN_MASK|
			    MouseEvent.META_DOWN_MASK)) != 0)
		tempMouseMode = MODE_DRAG_COLUMN;
	    else
		return;
	}
	
	if (tempMouseMode != MODE_SELECT && tempMouseMode != MODE_DRAG_SELECTED)
	    clearSelection();
	if (mouseMode == MODE_SELECT && doSwitch(e.getX(), e.getY()))
	{
            didSwitch = true;
	    return;
	}
	initDragX = e.getX();
	initDragY = e.getY();
	dragging = true;
	if (tempMouseMode != MODE_ADD_ELM || addingClass == null)
	    return;
	
	int x0 = snapGrid(e.getX());
	int y0 = snapGrid(e.getY());
	if (!circuitArea.contains(x0, y0))
	    return;

	dragElm = constructElement(addingClass, x0, y0);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        int ex = e.getModifiersEx();
	if ((ex & (MouseEvent.SHIFT_DOWN_MASK|MouseEvent.CTRL_DOWN_MASK|
		   MouseEvent.META_DOWN_MASK)) == 0 && e.isPopupTrigger()) {
	    doPopupMenu(e);
	    return;
	}
	tempMouseMode = mouseMode;
	selectedArea = null;
	dragging = false;
	boolean circuitChanged = false;
	if (heldSwitchElm != null) {
	    heldSwitchElm.mouseUp();
	    heldSwitchElm = null;
	    circuitChanged = true;
	}
	if (dragElm != null) {
	    // if the element is zero size then don't create it
	    if (dragElm.x == dragElm.x2 && dragElm.y == dragElm.y2)
		dragElm.delete();
	    else {
		
                if(dragElm instanceof Ground)
                {
                    elmList.add(0,dragElm);
                    groundCount++;
                }
                else
                {
                    elmList.add(dragElm);
                }
		circuitChanged = true;
	    }
	    dragElm = null;
	}
	if (circuitChanged)
            cv.repaint();
	    
	if (dragElm != null)
	    dragElm.delete();
	dragElm = null;
	cv.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("EXITED");
	mouseElm = plotXElm = plotYElm = null;
	cv.repaint();
    }

     void setMouseMode(int mode)
    {
        
	mouseMode = mode;
	if ( mode == MODE_ADD_ELM )
	    cv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	else
	    cv.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void choice_circuit_element(String element)
    {
        int prevMouseMode = mouseMode;
        setMouseMode(MODE_ADD_ELM);
        String s=this.getClass().getPackage().toString();
        s=s.substring(8,s.length())+"."+element;
        if (s.length() > 0)
		mouseModeStr = s;
	    if (s.compareTo("DragAll") == 0)
		setMouseMode(MODE_DRAG_ALL);
	    else if (s.compareTo("DragRow") == 0)
		setMouseMode(MODE_DRAG_ROW);
	    else if (s.compareTo("DragColumn") == 0)
		setMouseMode(MODE_DRAG_COLUMN);
	    else if (s.compareTo("DragSelected") == 0)
		setMouseMode(MODE_DRAG_SELECTED);
	    else if (s.compareTo("DragPost") == 0)
		setMouseMode(MODE_DRAG_POST);
	    else if (s.compareTo("Select") == 0)
		setMouseMode(MODE_SELECT);
	    else if (s.length() > 0) {
		try {
		    addingClass = Class.forName(s);
		} catch (Exception ee) {
		    ee.printStackTrace();
		}
	    }
	    else
	    	setMouseMode(prevMouseMode);
	    tempMouseMode = mouseMode;
    }
    
    @Override
    public void itemStateChanged(ItemEvent e){
        
        Object mi = e.getItemSelectable();
	
	if (mi instanceof CheckboxMenuItem){
	    MenuItem mmi = (MenuItem) mi;
	    int prevMouseMode = mouseMode;
	    setMouseMode(MODE_ADD_ELM);
	    String s = mmi.getActionCommand();
            System.out.println("MMI: "+s);
	    if (s.length() > 0)
		mouseModeStr = s;
	    if (s.compareTo("DragAll") == 0)
		setMouseMode(MODE_DRAG_ALL);
	    else if (s.compareTo("DragRow") == 0)
		setMouseMode(MODE_DRAG_ROW);
	    else if (s.compareTo("DragColumn") == 0)
		setMouseMode(MODE_DRAG_COLUMN);
	    else if (s.compareTo("DragSelected") == 0)
		setMouseMode(MODE_DRAG_SELECTED);
	    else if (s.compareTo("DragPost") == 0)
		setMouseMode(MODE_DRAG_POST);
	    else if (s.compareTo("Select") == 0)
		setMouseMode(MODE_SELECT);
	    else if (s.length() > 0) {
		try {
		    addingClass = Class.forName(s);
                    System.out.println("AQUI: "+addingClass.getName());
		} catch (Exception ee) {
		    ee.printStackTrace();
		}
	    }
	    else
	    	setMouseMode(prevMouseMode);
	    tempMouseMode = mouseMode;
	}
    }

    void setMenuSelection(){
        
	if (menuElm != null) {
	    if (menuElm.selected)
		return;
	    clearSelection();
	    menuElm.setSelected(true);
	}
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
        if (e.getKeyChar() == 127)
	{
	    doDelete();
	    return;
	}
	if (e.getKeyChar() == ' ' || e.getKeyChar() == KeyEvent.VK_ESCAPE) {
	    setMouseMode(MODE_SELECT);
	    mouseModeStr = "Select";
	}
	tempMouseMode = mouseMode;
    }

    void doDelete() {
	int i;
	setMenuSelection();
	boolean hasDeleted = false;

	for (i = elmList.size()-1; i >= 0; i--) {
	    CircuitElement ce = getElm(i);
	    if (ce.isSelected()) {
		ce.delete();
		elmList.remove(i);
		hasDeleted = true;
                if(ce instanceof Ground)
                {
                    groundCount--;
                }
	    }
	}

	if ( !hasDeleted )
	{
	    for (i = elmList.size()-1; i >= 0; i--) {
		CircuitElement ce = getElm(i);
		if (ce == mouseElm) {
		    ce.delete();
		    elmList.remove(i);
		    hasDeleted = true;
		    mouseElm = null;
		    break;
		}
	    }
	}
        
        if (hasDeleted)
        {
            cv.repaint();
            
        }
	    
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    //endregion
    
    public void analyzeCircuit(){
	if (elmList.isEmpty())
        {
            JOptionPane.showMessageDialog(frame_parent, "Não há elementos para análise","Análise do circuito",JOptionPane.ERROR_MESSAGE);
        } 
        else
        {
            nodeList = new ArrayList<>();
            boolean gotGround = false;
            int i, j;
            // Procura por nó referência (terra)
            for (i = 0; i < elmList.size(); i++)
            {
                CircuitElement ce = getElm(i);
                if (ce instanceof Ground)
                {
                    gotGround = true;
                    break;
                }
            }
            if(!gotGround)
            {
                JOptionPane.showMessageDialog(frame_parent, "Não há um nó de referência","Análise do circuito",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // allocate nodes and voltage sources
                for (i = 0; i < elmList.size(); i++)
                {
                    CircuitElement ce = getElm(i);
                    int posts = ce.getNodesCount();
                    int node;
                    // allocate a node for each post and match posts to nodes
                        for(j = 0; j < posts; j++)
                        {
                            Point pt = ce.getPost(j);
                            int k;
                            for (k = 0; k < nodeList.size(); k++)
                            {
                                CircuitNode cn = getCircuitNode(k);
                                if (pt.x == cn.x && pt.y == cn.y)
                                {
                                   break;
                                }
                            }
                            if (k == nodeList.size()){
                                CircuitNode cn = new CircuitNode();
                                cn.x = pt.x;
                                cn.y = pt.y;
                                nodeList.add(cn);
                            }
                            node=k;
                            if(k==groundCount)
                            {
                                node=1;
                            }
                            else if(k<groundCount)
                            {
                                node=0;
                            }
                            else if(k>groundCount)
                            {
                                node=k-(groundCount-1);
                            }
                            ce.setNode(j, node);
                        }
                }   
                for(i=0;i<elmList.size();i++)
                {
                        System.out.println("Elemento: "+getElm(i));
                        for(int k=0;k<getElm(i).nodes.length;k++)
                        {
                            if(!(getElm(i) instanceof Wire) && !(getElm(i) instanceof Ground))
                            {
                                System.out.println("No "+(k+1)+" "+getElm(i).nodes[k]);
                            }                            
                        }
                }
            }
        }
    }
    
    private CircuitNode getCircuitNode(int n) {
	if (n >= nodeList.size())
	    return null;
	return nodeList.get(n);
    }
    
    
    
    
}
