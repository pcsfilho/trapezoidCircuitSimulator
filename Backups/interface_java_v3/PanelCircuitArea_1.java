package org.gui.canvas;        

import org.gui.elements.Editable;
import org.gui.elements.GraphicElement;
import org.gui.elements.CurrentSource;
import org.gui.elements.Inductor;
import org.gui.elements.Resistor;
import org.gui.elements.ACVoltageSource;
import org.gui.elements.Ground;
import org.gui.elements.Wire;
import org.gui.elements.CircuitElement;
import org.gui.elements.Capacitor;
import org.gui.elements.Switch;
import org.gui.elements.DCVoltageSource;
import org.gui.elements.Output;
import org.gui.MainWindow;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.gui.elements.Ammeter;
import org.gui.elements.Circuit;
import org.gui.elements.Simulation;
import org.gui.elements.Voltmeter;


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
    public static final int MODE_ADD_ELM = 0;
    public static final int MODE_DRAG_ALL = 1;
    public static final int MODE_DRAG_ROW = 2;
    public static final int MODE_DRAG_COLUMN = 3;
    public static final int MODE_DRAG_SELECTED = 4;
    public static final int MODE_DRAG_POST = 5;
    public static final int MODE_SELECT = 6;
    public static String muString = "u";
    public static String ohmString = "ohm";
    int draggingPost;
    int dragX, dragY, initDragX, initDragY;
    public int mouseMode = MODE_SELECT;
    int tempMouseMode = MODE_SELECT;
    String mouseModeStr = "Select";
    MainWindow frame_parent;
    public CircuitElement plotXElm,plotYElm;
    CircuitCanvas cv;
    static EditDialog editDialog;
    int mousePost = -1;
    Rectangle circuitArea;
    private int gridSize, gridMask, gridRound, groundCount=0, wirePos=0,wireCount=0, elmCount=0;
    private ArrayList<CircuitElement> elmList;
    public CircuitElement dragElm;
    public CircuitElement menuElm, mouseElm, stopElm;
    Circuit circuit;
    Switch heldSwitchElm;
    ArrayList<CircuitNode> nodeList;
    private boolean circuitChanged;
    //endregion
    private Simulation sim;
    private boolean timeChanged;
    
    /**
     * Contrutor que recebe o Frame principal
     * @param frame 
     */
    public PanelCircuitArea(MainWindow frame)
    {
        this.frame_parent=frame;
        init();
    }
    /**
     * Retorna se houve alguma mudança no circuito
     * @return 
     */
    public boolean getChanged()
    {
        return circuitChanged;
    }
    
    /**
     * Retorna uma lista com os elementos adicionados no canvas
     * @return 
     */
    public ArrayList<CircuitElement> getElementsList()
    {
        return elmList;
    }
    /**
    * Retorna o objeto de circuito criado para descrever um determinado circuito
    * @return 
    */
    public Circuit get_circuit()
    {
        return circuit;
    }
    /**
    *Inicializa os componentes para desenho de um circuito. 
    */
    public void init() {
        
	CircuitElement.initClass(this);
	circuit = new Circuit();
	setLayout(new CircuitLayout());
	cv = new CircuitCanvas(this);
	cv.addComponentListener(this);
	cv.addMouseMotionListener(this);
	cv.addMouseListener(this);
	cv.addKeyListener(this);
	this.add(cv);

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
        setClassElement(Ammeter.class.getCanonicalName());
        setClassElement(Voltmeter.class.getCanonicalName());
	
	setGrid();
	elmList = new ArrayList<>();

	//cv.setBackground(Color.black);
	cv.setForeground(Color.BLACK);

        handleResize();
	requestFocus();
    }
    /**
    * Retorna um elemento de circuito adicionado a lista de elementos de circuito pelo indice
    * @param n
    * @return 
    */
    public CircuitElement getElm(int n) {
	if (n < elmList.size())
        {
            return elmList.get(n);
        }
        return null;
    }
    /**
     * Retorna o menor valor
     * @param a
     * @param b
     * @return 
     */
    int min(int a, int b)
    {
        return (a < b) ? a : b;
    }
    /**
     * Retorna o maior valor
     * @param a
     * @param b
     * @return 
     */
    int max(int a, int b)
    {
        return (a > b) ? a : b;
    }
    /**
     * 
     */
    void handleResize() {
        winSize = this.getSize();//Pega tamanho do canvas
	if (winSize.width == 0)
        {
	    return;
        }
	dbimage = createImage(this.getSize().width, this.getSize().height);
	circuitArea = new Rectangle(0, 0, this.getSize().width,this.getSize().height);
        
	int i;
	int minx = 1000, maxx = 0, miny = 1000, maxy = 0;
	for (i = 0; i != elmList.size(); i++)
        {
	    CircuitElement ce = getElm(i);
	    // centered text causes problems when trying to center the circuit,
	    // so we special-case it here
	    if (!ce.isCenteredText()) {
		minx = min(ce.getX1(), min(ce.getX2(), minx));
		maxx = max(ce.getX1(), max(ce.getX2(), maxx));
	    }
	    miny = min(ce.getY1(), min(ce.getY2(), miny));
	    maxy = max(ce.getY1(), max(ce.getY2(), maxy));
	}
	// center circuit; we don't use snapGrid() because that rounds
	int dx = gridMask & ((circuitArea.width -(maxx-minx))/2-minx);
	int dy = gridMask & ((circuitArea.height-(maxy-miny))/2-miny);
	if (dx+minx < 0)
	    dx = gridMask & (-minx);
	if (dy+miny < 0)
	    dy = gridMask & (-miny);
	for (i = 0; i != elmList.size(); i++)
        {
	    CircuitElement ce = getElm(i);
	    ce.move(dx, dy);
	}
    }
    /**
     * 
     * @param x
     * @return 
     */
    public int snapGrid(int x)
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
	int t = elm.getType();
	if (t == 0) {
	    return;
	}

	int s = elm.getShortcut();
	if ( elm.needsShortcut() && s == 0 )
	{
	    if ( s == 0 )
	    {
		return;
	    }
	    else if ( s <= ' ' || s >= 127 )
	    {
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
	gridSize = 16;
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
        
        if (tempMouseMode == MODE_DRAG_ROW || tempMouseMode == MODE_DRAG_COLUMN ||
	    tempMouseMode == MODE_DRAG_POST || tempMouseMode == MODE_DRAG_SELECTED)
        {
	    for (i = 0; i != elmList.size(); i++)
            {
		CircuitElement ce = getElm(i);
		ce.drawPost(g, ce.getX1() , ce.getY1());
		ce.drawPost(g, ce.getX2(), ce.getY2());
	    }
        }
	if (dragElm != null && (dragElm.getX1() != dragElm.getX2() || dragElm.getY1() != dragElm.getY2()))
        {
            dragElm.draw(g);
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
	cv.repaint();
    }
    
    void removeZeroLengthElements() {
	int i;
	
	for (i = elmList.size()-1; i >= 0; i--) {
	    CircuitElement ce = getElm(i);
	    if (ce.getX1() == ce.getX2() && ce.getY1() == ce.getY2()) {
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
	    if (ce.getY1()  == dragY)
		ce.movePoint(0, 0, dy);
	    if (ce.getY2() == dragY)
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
	    if (ce.getX1()  == dragX)
		ce.movePoint(0, dx, 0);
	    if (ce.getX2() == dragX)
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

    void dragPost(int x, int y)
    {
	if (draggingPost == -1) {
	    draggingPost =
		(distanceSq(mouseElm.getX1() , mouseElm.getY1() , x, y) >
		 distanceSq(mouseElm.getX2(), mouseElm.getY2(), x, y)) ? 1 : 0;
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
	cv.repaint();
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
	plotXElm = plotYElm = null;
	int bestDist = 100000;
	int bestArea = 100000;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
        
	    if (ce.getBOundingBox().contains(x, y)) {
		int j;
		int area = ce.getBOundingBox().width * ce.getBOundingBox().height;
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
	if (mouseElm == null){
	    // the mouse pointer was not in any of the bounding boxes, but we
	    // might still be close to a post
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElm(i);
		int j;
		int jn = ce.getNodesCount();
		for (j = 0; j != jn; j++) {
		    Point pt = ce.getPost(j);
		    if (distanceSq(pt.x, pt.y, x, y) < 26) {
			mouseElm = ce;
			break;
		    }
		}
	    }
	} 
	if (mouseElm != origMouse)
	    cv.repaint();
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
	    doEditMenu(e);
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
	    if (mouseMode == MODE_SELECT || mouseMode == MODE_DRAG_SELECTED)
		clearSelection();
	}
    }
    
    private void clearSelection()
    {
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElm(i);
	    ce.setSelected(false);
	}
    }
   
    
    void doEditMenu(MouseEvent e) {
	if( mouseElm != null )
	    doEdit(mouseElm);
    }
    //Criação de dialogo para edição do componente
    void doEdit(Editable eable) {
	clearSelection();
	if (editDialog != null) {
	    requestFocus();
	    editDialog.setVisible(false);
	    editDialog = null;
	}
	editDialog = new EditDialog(eable, this, frame_parent);
	editDialog.setVisible(true);
        circuitChanged=true;
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
        se.changeState();
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
        
	int ex = e.getModifiersEx();
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
	tempMouseMode = mouseMode;
	selectedArea = null;
	dragging = false;
	circuitChanged = false;
	if (heldSwitchElm != null)
        {
	    circuitChanged = true;
            heldSwitchElm = null;
	}
	if (dragElm != null){
	    // if the element is zero size then don't create it
	    if (dragElm.getX1() == dragElm.getX2() && dragElm.getY1() == dragElm.getY2())
            {
		dragElm.delete();
            }
	    else
            {
                if(dragElm instanceof Ground)
                {
                    elmList.add(0,dragElm);
                    groundCount++;
                }
                else if((dragElm instanceof Wire) || (dragElm instanceof Ammeter))
                {
                    if(wireCount==0)
                    {
                        wirePos=elmList.size();
                    }
                    elmList.add(dragElm);
                    wireCount++;
                    if(dragElm instanceof Ammeter)
                    {
                        dragElm.set_name();
                    }
                }
                else
                {
                    if(wireCount>0)
                    {
                        elmList.add(wirePos, dragElm);
                    }
                    else
                    {
                        elmList.add(dragElm);
                    }
                    elmCount++;
                    wirePos++;
                    dragElm.set_name();
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
	mouseElm = plotXElm = plotYElm = null;
	cv.repaint();
    }
    /**
    * Seta o ponteiro de mouse no modo de seleção 
    */
    public void setMousePointer()
    {
        setMouseMode(MODE_SELECT);
	mouseModeStr = "Select";
    }
    
    private void setMouseMode(int mode)
    {
	mouseMode = mode;
	if ( mode == MODE_ADD_ELM )
	    cv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	else
	    cv.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    /**
    *Seleciona elemento para desenho no canvas 
    * @param element 
    */
    public void choice_circuit_element(String element)
    {
        int prevMouseMode = mouseMode;
        setMouseMode(MODE_ADD_ELM);
        String s= "org.gui.elements."+element;
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
	    else if (s.length() > 0)
            {
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
    public void itemStateChanged(ItemEvent e){}
    /**
     * 
    */
    private void setMenuSelection(){
	if (menuElm != null)//Se há algum elemento selecionado
        {
	    if(menuElm.isSelected())
            {
                return;
            }
	    clearSelection();
	    menuElm.setSelected(true);
	}
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
        if (e.getKeyChar() == 127)
	{
	    doDelete();
            circuitChanged=true;
	    return;
	}
	if (e.getKeyChar() == ' ' || e.getKeyChar() == KeyEvent.VK_ESCAPE)
        {
	    setMousePointer();
	}
	tempMouseMode = mouseMode;
    }
    /**
     * 
    */
    void doDelete(){
	int i;
	setMenuSelection();
	boolean hasDeleted = false;

	for (i = elmList.size()-1; i >= 0; i--) {
	    CircuitElement ce = getElm(i);
	    if (ce.isSelected()) 
            {
		ce.delete();
		elmList.remove(i);
		hasDeleted = true;
                if(ce instanceof Ground)
                {
                    groundCount--;
                    wirePos--;
                }
                else if((ce instanceof Wire) || (ce instanceof Ammeter))
                {
                    wireCount--;
                }
                else
                {
                    elmCount--;
                    wirePos--;
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
                if(ce instanceof Ground)
                {
                    groundCount--;
                    wirePos--;
                }
                else if((ce instanceof Wire) || (ce instanceof Ammeter))
                {
                    wireCount--;
                }
                else
                {
                    elmCount--;
                    wirePos--;
                }
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
    /**
     * 
     * @param e 
    */
    @Override
    public void keyPressed(KeyEvent e){}
    /**
     * 
     * @param e 
    */
    @Override
    public void keyReleased(KeyEvent e){}
    //endregion
    
    private void connect_elements_with_wires(Map<String, CircuitNode> nodes)
    {
        //System.out.println("CONEXOES");
        for(int i=(groundCount+elmCount);i<elmList.size();i++)
        {
            CircuitElement ce = elmList.get(i);
            if(ce instanceof Wire || ce instanceof Ammeter)
            {
                int min_node_local;
                int max_node_local;
                if(ce.getNode(0)>ce.getNode(1))
                {
                    min_node_local=ce.getNode(1);
                    max_node_local=ce.getNode(0);
                }
                else
                {
                    max_node_local=ce.getNode(1);
                    min_node_local=ce.getNode(0);
                }
          //      System.out.println("No minimo: "+min_node_local);
            //    System.out.println("No maximo: "+max_node_local);
                String key = Integer.toString((max_node_local+groundCount)-1);
                
                CircuitNode c_n = nodes.get(key);
              //  System.out.println("No recuperado: "+c_n.getNum());
                for(int g=0;g<c_n.getElements().size();g++)
                {
                    CircuitElement c_e = c_n.getElements().get(g);
                    if(!(c_e instanceof Wire) && !(c_e instanceof Ammeter))
                    {
                //        System.out.println("Elemento: "+ (char)c_e.getType());
                        for(int p=0;p < c_e.getNodes().length;p++)
                        {
                  //              System.out.println("No "+ (p+1)+" "+c_e.getNodes()[p]);
                                            if(c_e.getNodes()[p]==max_node_local)
                                            {
                    //                            System.out.println("Troca no"+(c_e.getNodes()[p])+" por: "+ min_node_local);
                                                c_e.setNode(p,min_node_local);
                                            }
                                        }
                                    }
                                }
                            }
        }
    }
    
    private void define_nodes(int start, int limit,Map<String, CircuitNode> nodes, ArrayList<Integer> groundList)
    {
        for (int i = start; i < limit; i++)
                    {
                        CircuitElement ce = getElm(i);
                        int posts = ce.getNodesCount();
                        // 
                        for(int j = 0; j < posts; j++)
                        {
                            Point pt = ce.getPost(j);
                            int k;
                            for (k = 0; k < nodeList.size(); k++)
                            {
                                CircuitNode cn = getCircuitNode(k);
                                if (pt.x == cn.getX() && pt.y == cn.getY())
                                {
                                    break;
                                }
                            }
                            CircuitNode cn;
                            if (k == nodeList.size())
                            {
                                cn = new CircuitNode();
                                cn.setX(pt.x);
                                cn.setY(pt.y);
                		CircuitNodeLink cnl = new CircuitNodeLink(j,ce);
                                cn.getElements().add(ce);
                                cn.getLinks().add(cnl);
                                cn.setNum(k);
                                nodeList.add(cn);
                      //          System.out.println("CHAVE: "+k);
                                nodes.put(Integer.toString(k),cn);
                                
                                if(ce instanceof Ground)
                                {
                                    groundList.add(k);
                                }
                            }
                            else
                            {
                                cn=getCircuitNode(k);
                                cn.getElements().add(ce);
                                CircuitNodeLink cnl = new CircuitNodeLink(j,ce);
                                cn.getLinks().add(cnl);
                            }
                            ce.setNode(j, k);
                            if(!(ce instanceof Ground))
                                {
                                    if(groundList.contains(k))
                                    {
                                        ce.setNode(j, 0);
                                    }
                                    else
                                    {
                                        ce.setNode(j, k);
                                    }
                                }
                        }
                        
                            //add elementos que descrevem o circuito
                            if(!(ce instanceof Wire) && !(ce instanceof Ground))
                            {
                                circuit.add_element(ce);
                            }
                    }
    }
    
    private void adjustment_nodes()
    {
        //System.out.println("Ajuste");
        for(int i=groundCount;i<groundCount+wireCount+elmCount;i++)
        {
            CircuitElement ce = elmList.get(i);
          //  System.out.println("Elemento: "+(char)ce.getType());
            for(int j=0;j<ce.getNodesCount();j++)
            {
            //    System.out.println("No: "+(j+1)+" = "+ce.getNodes()[j]);
                if(ce.getNodes()[j]>=groundCount)
                {
                    int temp_num_node=0;
                    int value_node=(ce.getNodes()[j]-groundCount)+1;
                    if(j==0)
                    {
                        temp_num_node=1;
                    }
                    if(value_node!=ce.getNodes()[temp_num_node])
                    {
                        ce.setNode(j,value_node);
                    }
              //      System.out.println("Substitui por: "+value_node);
                }
            }
        }
    }
    /**
     * Faz análise do circuito desenhado no canvas e mapeia os elementos para análise.
    */
    public void create_circuit_description()
    {
	if (elmList.isEmpty())
        {
            JOptionPane.showMessageDialog(frame_parent, "Não há elementos para análise","Análise do circuito",JOptionPane.ERROR_MESSAGE);
        } 
        else
        {
            if(groundCount==0)
            {
                JOptionPane.showMessageDialog(frame_parent, "Não há um nó de referência","Análise do circuito",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                if(circuitChanged)
                {
                    nodeList = new ArrayList<>();
                    ArrayList<Integer> groundList = new ArrayList<>();
                    Map<String, CircuitNode> nodes = new HashMap<>();
                    int i, j;
                    circuit = new Circuit();
                    //Mapeia nos de referencia
                    define_nodes(0,groundCount,nodes, groundList);
                /*    System.out.println("ANALIZE TERRA");
                    for(i=0;i<groundCount;i++)
                    {
                        CircuitElement ce=elmList.get(i);
                        System.out.println("Elemento: "+ (char)ce.getType());
                        for(int k=0;k<ce.getNodes().length;k++)
                        {
                            System.out.println("No "+(k+1)+" "+ce.getNodes()[k]);
                        }
                    }
                */  
             
                    
                    //Mapeia demais elementos
                    define_nodes(groundCount,elmList.size(),nodes, groundList);
                /*    System.out.println("ANALIZE ELEMENTOS E FIOS");
                    for(i=0;i<elmList.size();i++)
                    {
                        CircuitElement ce=elmList.get(i);
                        System.out.println("Elemento: "+ (char)ce.getType());
                        for(int k=0;k<ce.getNodes().length;k++)
                        {
                            System.out.println("No "+(k+1)+" "+ce.getNodes()[k]);
                        }
                    }
                  */  
                    //Ajusta a numeraçao dos nos na ordem correta
                    adjustment_nodes();  
                   /* System.out.println("AJUSTE NOS");
                    for(i=0;i<elmList.size();i++)
                    {
                        CircuitElement ce=elmList.get(i);
                        System.out.println("Elemento: "+ (char)ce.getType());
                        for(int k=0;k<ce.getNodes().length;k++)
                        {
                            System.out.println("No "+(k+1)+" "+ce.getNodes()[k]);
                        }
                    }
                    */
                    //Se existir conexoes no circuito
                    if(wireCount>0)
                    {
                        connect_elements_with_wires(nodes);
                           System.out.println("CONECTA FIOS");
                        for(i=0;i<elmList.size();i++)
                        {
                            CircuitElement ce=elmList.get(i);
                            System.out.println("Elemento: "+ (char)ce.getType());
                            for(int k=0;k<ce.getNodes().length;k++)
                            {
                                System.out.println("No "+(k+1)+" "+ce.getNodes()[k]);
                            }
                        }
                    }                  
                    
                    //Cria arquivo 
                    try
                    {   
                        if(!circuit.get_path_circuit_name().equals(""))
                        {   
                            circuit.create_netlist_circuit();
                        }
                        else
                        {
                            if(save_circuit())
                            {
                                circuit.create_netlist_circuit();
                            }
                            else
                            {
                                return;
                            }
                        }
                        circuitChanged=false;
                    }
                    catch(FileNotFoundException e)
                    {
                        JOptionPane.showMessageDialog(frame_parent, "Erro ao criar o arquivo de descrição do circuito","Criação Netlist",JOptionPane.ERROR_MESSAGE);
                    }
                    catch(UnsupportedEncodingException e)
                    {
                        JOptionPane.showMessageDialog(frame_parent, "Houve algum erro ao criar o arquivo de descrição do circuito","Criação Netlist",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    System.out.println("Não mudou");
                }
            }
        }
        System.out.println("create "+circuitChanged);
    }
    
    /**
     * 
     * @param time 
     * @return  
     */
    public String analysis_circuit(String time)
    {
        if(getChanged())
        {
            create_circuit_description();
        }
        
        if(circuit.get_path_circuit_name()!=null && !circuit.get_path_circuit_name().equals(""))
        {
            try
            {
                sim = new Simulation("TRAN",0.0,Double.parseDouble(time), circuit);
                timeChanged=false;
                return sim.create_simulation_file();
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(frame_parent, "Houve algum erro na análise do circuito","Análise do circuito",JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
    
    public void setTimeChanged(boolean s)
    {
        this.timeChanged=s;
    }
    /**
     * Requisita um caminho ao usuário para salvar o circuito presente no canvas
     * @return se o circuito foi salvo com sucesso
    */
    public boolean save_circuit()
    {
        // Open a file chooser
        JFileChooser fileChoose = new JFileChooser();
        FileNameExtensionFilter netFilter = new FileNameExtensionFilter("arquivo netlist (*.net)", "net");
        fileChoose.addChoosableFileFilter(netFilter);
        fileChoose.setFileFilter(netFilter);
        // Open the file, only this time we call
        int option = fileChoose.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {   
            circuit.set_path_circuit_name(fileChoose.getSelectedFile().getPath()+".net");
            return true;
        }
        return false;
    }
    /**
     *Retorna elemento de circuito na lista de elementos de circuito pelo indice
     * @param n
     * @return 
    */
    public CircuitNode getCircuitNode(int n)
    {
	if (n < nodeList.size())
            return nodeList.get(n);
	return null;
    }
}
