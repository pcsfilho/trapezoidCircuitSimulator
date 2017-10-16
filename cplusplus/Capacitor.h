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

using namespace std;

class Capacitor: public Element{
public:
    Capacitor();
    Capacitor(vector<string> element, int num_var);
    
    double get_current_historic();
    double get_current();
    //void set_stamp(double** Yn,vector<double> nodal_solution ,int num_vars);
    void update_historic(double** Yn_solution, int num_vars);
    void set_stamp(double** Yn_original, double** Yn_solution, int num_vars);
private:
    double current_historic;
    double current;
    //Methods
    void update_current(double** Yn_solution,int num_vars);
};

#endif /* CAPACITOR_H */

