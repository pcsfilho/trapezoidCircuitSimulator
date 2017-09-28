/* 
 * File:   Capacitor.cpp
 * Author: paulo
 * 
 * Created on 21 de Setembro de 2017, 23:31
 */
#include "Capacitor.h"
#include "CONSTANTES.h"

Capacitor::Capacitor()
{
    current=0;
    current_historic=0;
}

Capacitor::Capacitor(vector<string> element):Element(element)
{}


Capacitor::Capacitor(double step_time)
{
    current=0;
    current_historic=0;
    resistance = step_time/2*value;
}

double Capacitor:: get_resistance()
{
    return resistance;
}

void Capacitor::calculate_current(double* nodal_solution)
{
    if(node_2==REFERENCIA)
    {
        current=((1/resistance)*(nodal_solution[node_1])) + current_historic;
    }
    else
    {
        current=((1/resistance)*(nodal_solution[node_1]-nodal_solution[node_2])) + current_historic;
    }
}

void Capacitor::calculate_historic(double* nodal_solution)
{
    calculate_current(nodal_solution);
    if(node_2==REFERENCIA)
    {
       current_historic = - current-((1/resistance)*nodal_solution[node_1]);
    }
    else
    {
        current_historic = - current-((1/resistance)*(nodal_solution[node_1]-nodal_solution[node_2]));
    }
}

void Capacitor::set_stamp(double** Yn)
{
    if(get_node_1()!=REFERENCIA)
    {
        Yn[get_node_1()][get_node_1()] = Yn[get_node_1()][get_node_1()]+(1/get_resistance());
    }
    if(get_node_2()!=REFERENCIA)
    {
        Yn[get_node_2()][get_node_2()] = Yn[get_node_2()][get_node_2()]+(1/get_resistance());
    }
    if(get_node_1()!= REFERENCIA && get_node_2()!=REFERENCIA)
    {
        Yn[get_node_1()][get_node_2()] = Yn[get_node_1()][get_node_2()]-(1/get_resistance());
        Yn[get_node_2()][get_node_1()] = Yn[get_node_1()][get_node_2()];
    }
}