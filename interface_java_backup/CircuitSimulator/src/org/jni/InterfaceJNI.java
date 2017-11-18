/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jni;
/**
 *
 * @author paulo
 */
public class InterfaceJNI
{
    //declaração do método nativo
    public native String run_analysis(String path);
    //Bloco estático para carregar a biblioteca "simulation"
    static
    {
        System.loadLibrary("simulation");
    }
}
