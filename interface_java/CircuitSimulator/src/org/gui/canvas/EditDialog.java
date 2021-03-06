package org.gui.canvas;
import org.gui.elements.Editable;
import org.gui.MainWindow;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.gui.elements.Switch;

public class EditDialog extends Dialog implements AdjustmentListener, ActionListener, ItemListener {
    Editable elm;
    MainWindow cframe;
    PanelCircuitArea panel_circuit;
    Button applyButton, okButton;
    EditInfo einfos[];
    int einfocount;
    final int barmax = 1000;
    NumberFormat noCommaFormat;
    ArrayList<JTextField> text_array_switch;

    EditDialog(Editable ce, PanelCircuitArea p,MainWindow f) {
        
	super(f, "Editar Componente", false);
        setLocationRelativeTo(null);
	cframe = f;
        this.panel_circuit = p;
	elm = ce;
        text_array_switch = new ArrayList<>();
        
	setLayout(new EditDialogLayout());
	einfos = new EditInfo[10];
	noCommaFormat = DecimalFormat.getInstance();
	noCommaFormat.setMaximumFractionDigits(10);
	noCommaFormat.setGroupingUsed(false);
	int i;
	for (i = 0;i<10; i++)
        {
	    einfos[i] = elm.getEditInfo(i);
	    if (einfos[i] == null)
            {
		break;
            }
            
	    EditInfo ei = einfos[i];
	    add(new Label(ei.name));
	    if (ei.choice != null) {
		add(ei.choice);
		ei.choice.addItemListener(this);
	    } else if (ei.checkbox != null) {
		add(ei.checkbox);
		ei.checkbox.addItemListener(this);
	    } else {
		add(ei.textf =new TextField(unitString(ei), 10));
		if (ei.text != null)
		    ei.textf.setText(ei.text);
		ei.textf.addActionListener(this);
	    }
	}
        
        if(elm instanceof Switch)
        {
            
            String option=null;
            int rowCnt=((Switch)elm).get_time_coomutations().size();
            
            if(rowCnt==0)
            {
                JTextField numCom = new JTextField();
                Object[] message = {"Numero de Comutaçoes", numCom.getText()};
                option = JOptionPane.showInputDialog(null, message, "Numero de Comutaçoes da Chave", JOptionPane.OK_CANCEL_OPTION);
                if (option == null)
                {
                    this.closeDialog();
                }
                else
                {
                    rowCnt=Integer.parseInt(option);
                }
            }
            
            
            JTextField g = new JTextField("Tempos de Comutaçao");
            g.setEditable(false);
            JTextField sp=new JTextField();
            add(g);
            for(int j=0;j<rowCnt;j++)
            {
                g = new JTextField(5);
                text_array_switch.add(g);
                add(new Label("Tempo "+(j+1)+": "));
                if(((Switch)elm).get_time_coomutations().size()>0)
                {
                    g.setText(""+((Switch)elm).get_time_coomutations().get(j));
                }
                add(g);
            }
        }
        
        
	einfocount = i;
	add(applyButton = new Button("Aplicar"));
	applyButton.addActionListener(this);
	add(okButton = new Button("OK"));
	okButton.addActionListener(this);
        Dimension d = getSize();
        setLocationRelativeTo(null);
        
	addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				closeDialog();
			}
		}
	);
    }

    String unitString(EditInfo ei) {
	double v = ei.value;
	double va = Math.abs(v);
	if (ei.dimensionless)
	    return noCommaFormat.format(v);
	if (v == 0) return "0";
	if (va < 1e-9)
	    return noCommaFormat.format(v*1e12) + "p";
	if (va < 1e-6)
	    return noCommaFormat.format(v*1e9) + "n";
	if (va < 1e-3)
	    return noCommaFormat.format(v*1e6) + "u";
	if (va < 1 && !ei.forceLargeM)
	    return noCommaFormat.format(v*1e3) + "m";
	if (va < 1e3)
	    return noCommaFormat.format(v);
	if (va < 1e6)
	    return noCommaFormat.format(v*1e-3) + "k";
	if (va < 1e9)
	    return noCommaFormat.format(v*1e-6) + "M";
	return noCommaFormat.format(v*1e-9) + "G";
    }

    double parseUnits(EditInfo ei) throws java.text.ParseException {
	String s = ei.textf.getText();
	s = s.trim();
	int len = s.length();
	char uc = s.charAt(len-1);
	double mult = 1;
	switch (uc) {
	case 'p': case 'P': mult = 1e-12; break;
	case 'n': case 'N': mult = 1e-9; break;
	case 'u': case 'U': mult = 1e-6; break;
	    
	// for ohm values, we assume mega for lowercase m, otherwise milli
	case 'm': mult = (ei.forceLargeM) ? 1e6 : 1e-3; break;
	
	case 'k': case 'K': mult = 1e3; break;
	case 'M': mult = 1e6; break;
	case 'G': case 'g': mult = 1e9; break;
	}
	if (mult != 1)
	    s = s.substring(0, len-1).trim();
	return noCommaFormat.parse(s).doubleValue() * mult;
    }
	
    void apply() {
	int i;
	for (i = 0; i != einfocount; i++) 
        {
	    EditInfo ei = einfos[i];
	    if (ei.textf == null)
		continue;
	    if (ei.text == null) {
		try {
		    double d = parseUnits(ei);
		    ei.value = d;
		} catch (Exception ex) { /* ignored */ }
	    }
	    elm.setEditValue(i, ei);
            
            
	}
        if(elm instanceof Switch)
            {
                System.out.println("E chave");
                ((Switch)elm).get_time_coomutations().clear();
                
                int num_size = text_array_switch.size();
                System.out.println("Numero de tempos: "+num_size);
                for(int j=0;j<num_size;j++)
                {
                    String temp = text_array_switch.get(j).getText();
                    System.out.println("Tempo: "+temp);
                    if(!temp.equals(""))
                    {
                        double time = Double.parseDouble(text_array_switch.get(j).getText());
                        ((Switch)elm).addTimeCommutations(time);
                    }
                }
            }
    }
	
    public void actionPerformed(ActionEvent e) {
	int i;
	Object src = e.getSource();
	for (i = 0; i != einfocount; i++) {
	    EditInfo ei = einfos[i];
	    if (src == ei.textf) {
		if (ei.text == null) {
		    try {
			double d = parseUnits(ei);
			ei.value = d;
		    } catch (Exception ex) { /* ignored */ }
		}
		elm.setEditValue(i, ei);
	    }
	}
	if (e.getSource() == okButton) {
	    apply();
	    closeDialog();
	}
	if (e.getSource() == applyButton)
	    apply();
    }
	
    public void adjustmentValueChanged(AdjustmentEvent e) 
    {
    }

    public void itemStateChanged(ItemEvent e) {
	Object src = e.getItemSelectable();
	int i;
	boolean changed = false;
	for (i = 0; i != einfocount; i++) {
	    EditInfo ei = einfos[i];
	    if (ei.choice == src || ei.checkbox == src) {
		elm.setEditValue(i, ei);
		if (ei.newDialog)
		    changed = true;
	    }
	}
	if (changed) {
	    setVisible(false);
	    panel_circuit.editDialog = new EditDialog(elm, panel_circuit,cframe);
	    panel_circuit.editDialog.show();
	}
    }
	
    public boolean handleEvent(Event ev) {
	if (ev.id == Event.WINDOW_DESTROY) {
	    closeDialog();
	    return true;
	}
	return super.handleEvent(ev);
    }

    protected void closeDialog()
    {
	cframe.requestFocus();
	setVisible(false);
	panel_circuit.editDialog = null;
    }
}

