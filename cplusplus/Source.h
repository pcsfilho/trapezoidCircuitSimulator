/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Source.h
 * Author: paulo
 *
 * Created on 21 de Setembro de 2017, 23:32
 */

#ifndef SOURCE_H
#define SOURCE_H

#include "Element.h"


class Source:public Element{
public:
    Source();
    Source(vector<string> element);
    void set_resistance(double r);
    //void set_stamp(double** Yn, vector<double> nodal_solution,int num_vars);
    void set_stamp(double** Yn_original, double** Yn_solution, int num_vars);
    void set_stamp_source(double** Yn_original,int num_vars, double time);
    double get_source_value(double time);
private:
    string current_type;
    double frequency;
    double sin_source(double time);
};
#endif /* SOURCE_H */