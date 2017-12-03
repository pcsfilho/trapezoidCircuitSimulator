/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Resistor.h
 * Author: paulo
 *
 * Created on 21 de Setembro de 2017, 23:31
 */

#ifndef RESISTOR_H
#define RESISTOR_H

#include "Element.h"
class Resistor:public Element {
public:
    Resistor();
    void set_resistance(double r);
    Resistor(vector<string> element);
    //void set_stamp(double** Yn, vector<double> nodal_solution,int num_vars);
    void set_stamp(double** Yn_original, double** Yn_solution, int num_vars);
private:

};

#endif /* RESISTOR_H */