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
        System.loadLibrary("simulation_jni");
    }
}
