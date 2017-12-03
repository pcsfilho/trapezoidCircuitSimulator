/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Switch.h
 * Author: paulo
 *
 * Created on 1 de Outubro de 2017, 21:02
 */

#ifndef SWITCH_H
#define SWITCH_H
#include "Element.h"
class Switch:public Element{
public:
    Switch();
    Switch(vector<string> element);
    void set_stamp(double** Yn_original, double** Yn_solution, int num_vars);
    void commutate_switch(double current_time);
    bool has_event(double current_time);
    void set_resistance(double r);
    bool get_status();
    bool set_change(bool s);
    int get_count_changes();
    void set_stamp_switch(double** Yn_original, double time);
private:
    bool status;//aberto=false; fechado=true;
    int count_changes;//conta o numero de comutaçoes
    vector<double> time_changes;
    bool change;
};

#endif /* SWITCH_H */

