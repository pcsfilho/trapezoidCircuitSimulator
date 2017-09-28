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
    Resistor(vector<string> element);
    void set_stamp(double** Yn);
private:

};

#endif /* RESISTOR_H */

