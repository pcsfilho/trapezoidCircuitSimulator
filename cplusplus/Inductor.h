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
    Inductor(vector<string> element, int num_var);
    void set_resistance(double step);
    double get_current_historic();
    double get_current();
    //void set_stamp(double** Yn,vector<double> nodal_solution,int num_vars);
    void set_stamp(double** Yn_original, double** Yn_solution, int num_vars);
    void update_historic(double** Yn_solution, int num_vars);
private:
    double current_historic;
    double current;
    //Methods
    void update_current(double** Yn_solution, int num_vars);
};

#endif /* INDUCTOR_H */

