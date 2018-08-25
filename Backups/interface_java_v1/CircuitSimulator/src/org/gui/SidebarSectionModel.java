/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui;
import javax.swing.JComponent;
/**
 *
 * @author paulo
 */
public class SidebarSectionModel {
    
private String title;
private JComponent sectionContent;
private String supplementaryText;;
 
public SidebarSectionModel(String title,
  JComponent sectionContent,
  String supplementaryText) {
 this.title = title;
 this.sectionContent = sectionContent;
 this.supplementaryText = supplementaryText;
}
 
public String getText() {
 return title;
}
 
public JComponent getSectionContent(){
 return sectionContent;
}
 
public String getSupplementaryText() {
 return supplementaryText;
}
}
