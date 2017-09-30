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
    
    double get_current_historic();
    double get_current();
    void set_stamp(double** Yn,vector<double> nodal_solution ,int num_vars);
private:
    double resistance;
    double current_historic;
    double current;
    
    void update_historic(vector<double> nodal_solution);    
    void update_current(vector<double> nodal_solution);
};

#endif /* CAPACITOR_H */

