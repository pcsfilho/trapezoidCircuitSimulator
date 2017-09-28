/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Inductor.h
 * Author: paulo
 *
 * Created on 21 de Setembro de 2017, 23:31
 */

#ifndef INDUCTOR_H
#define INDUCTOR_H

#include "Element.h"

class Inductor: public Element{
public:
    Inductor();
    Inductor(vector<string> element);
    Inductor(double step_time);
    double get_resistance();
    void calculate_historic(double* nodal_solution);
    double get_current_historic();
    double get_current();
    void set_stamp(double** Yn);
private:
    double resistance;
    double current_historic;
    double current;
    
    void calculate_current(double* nodal_solution);
};

#endif /* INDUCTOR_H */

