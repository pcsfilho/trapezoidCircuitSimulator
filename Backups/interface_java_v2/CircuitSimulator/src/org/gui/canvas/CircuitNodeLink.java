/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.canvas;

import org.gui.elements.CircuitElement;

/**
 *
 * @author paulo
 */
class CircuitNodeLink 
{
    private int num;
    private CircuitElement elm;
    
    public CircuitNodeLink(int n, CircuitElement e)
    {
        this.num=n;
        this.elm=e;
    }
    
    public int getNum()
    {
        return num;
    }
    
    public CircuitElement getElm()
    {
        return elm;
    }
}
