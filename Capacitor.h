/* 
 * File:   Capacitor.h
 * Author: paulo
 *
 * Created on 21 de Setembro de 2017, 23:31
 */

#ifndef CAPACITOR_H
#define CAPACITOR_H
#include "Element.h"
#include "Circuit.h"

class Capacitor: public Element{
public:
    Capacitor();
    Capacitor(vector<string> element);
    Capacitor(double step_time);
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

#endif /* CAPACITOR_H */

