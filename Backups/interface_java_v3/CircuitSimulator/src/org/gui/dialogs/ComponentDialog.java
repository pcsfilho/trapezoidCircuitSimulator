/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.dialogs;

import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author paulo
 */
public class ComponentDialog extends JDialog{
    JLabel lValue = new JLabel("Valor");
    JLabel lName = new JLabel("Nome");
    JTextField valueField = new JTextField();
    JTextField nameField = new JTextField();
    private String title;
    private String[] dataComponents = new String[2];

    public ComponentDialog(Frame owner, String title)
    {
        super(owner, title);
        init();
    }

    private void init()
    {
        this.setLayout(new GridLayout(2, 2));
        this.add(lName);
        this.add(nameField);
        this.add(lValue);
        this.add(valueField);
    }
  
    public String[] getDataComponents() {
        dataComponents[0] = nameField.getText();
        dataComponents[1] = valueField.getText();
        return dataComponents;
  }
    
}
