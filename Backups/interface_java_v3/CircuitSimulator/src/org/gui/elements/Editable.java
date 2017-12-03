/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.elements;

import org.gui.canvas.EditInfo;

public interface Editable {
    EditInfo getEditInfo(int n);
    public void setEditValue(int n, EditInfo ei);
}
