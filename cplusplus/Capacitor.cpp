/* 
 * File:   Capacitor.cpp
 * Author: paulo
 * 
 * Created on 21 de Setembro de 2017, 23:31
 */
#include "Capacitor.h"
#include "CONSTANTES.h"
#include <iostream>

using namespace std;

Capacitor::Capacitor()
{
    current=0;
    current_historic=0;
}

Capacitor::Capacitor(vector<string> element, int nv):Element(element,nv)
{
    current=0;
    current_historic=0;
}


void Capacitor::update_current(double** Yn_solution, int num_vars)
{
    if(node_2==REFERENCIA)
    {
        current=((1/get_resistance())*(Yn_solution[node_1-1][num_vars])) + current_historic;
    }
    else
    {
        current=((1/get_resistance())*(Yn_solution[node_1-1][num_vars]-Yn_solution[node_2-1][num_vars])) + current_historic;
    }
}

double Capacitor::get_current()
{
    return current;
}

double Capacitor::get_current_historic()
{
    return current_historic;
}

void Capacitor::update_historic(double** Yn_solution, int num_vars)
{   
    update_current(Yn_solution,num_vars);
    
    if(node_2==REFERENCIA)
    {
       current_historic = - current-((1/get_resistance())*Yn_solution[node_1-1][num_vars]);
    }
    else
    {
        current_historic = - current-((1/get_resistance())*(Yn_solution[node_1-1][num_vars]-Yn_solution[node_2-1][num_vars]));
    }
}

void Capacitor::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    if(get_node_1()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_1()-1]+(1/get_resistance());
    }
    if(get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_2()-1][get_node_2()-1] = Yn_original[get_node_2()-1][get_node_2()-1]+(1/get_resistance());
    }
    if(get_node_1()!= REFERENCIA && get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][get_node_2()-1] = Yn_original[get_node_1()-1][get_node_2()-1]-(1/get_resistance());
        Yn_original[get_node_2()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_2()-1];
    }
    update_historic(Yn_solution,num_vars);
    
    if(get_node_1()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][num_vars] = Yn_original[get_node_1()-1][num_vars] - current_historic;
    }

    if(get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_2()-1][num_vars] = Yn_original[get_node_2()-1][num_vars] + current_historic;
    }
}