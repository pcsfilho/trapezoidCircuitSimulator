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
    void set_stamp(double** Yn, vector<double> nodal_solution,int num_vars);
private:
    string current_type;
};

#endif /* SOURCE_H */

