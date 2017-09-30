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

Capacitor::Capacitor(vector<string> element):Element(element)
{
    current=0;
    current_historic=0;
}


void Capacitor::update_current(vector<double> nodal_solution)
{
    cout<<"UPDATE CURRENT "<< nodal_solution.size()<<endl;
    if(node_2==REFERENCIA)
    {
        current=((1/get_resistance())*(nodal_solution[node_1-1])) + current_historic;
    }
    else
    {
        current=((1/get_resistance())*(nodal_solution[node_1-1]-nodal_solution[node_2-1])) + current_historic;
    }
}

void Capacitor::update_historic(vector<double> nodal_solution)
{   
    cout<<"UPDATE HISTORIC"<<endl;
    update_current(nodal_solution);
    
    if(node_2==REFERENCIA)
    {
        cout<<"IF 1"<<endl;
       current_historic = - current-((1/get_resistance())*nodal_solution[node_1-1]);
    }
    else
    {
        current_historic = - current-((1/get_resistance())*(nodal_solution[node_1-1]-nodal_solution[node_2-1]));
    }
}

void Capacitor::set_stamp(double** Yn, vector<double> nodal_solution,int num_vars)
{
    cout<<"ESTAMPA CAPACITOR"<<endl;
    
    if(get_node_1()!=REFERENCIA)
    {
        Yn[get_node_1()-1][get_node_1()-1] = Yn[get_node_1()-1][get_node_1()-1]+(1/get_resistance());
    }
    if(get_node_2()!=REFERENCIA)
    {
        Yn[get_node_2()-1][get_node_2()-1] = Yn[get_node_2()-1][get_node_2()-1]+(1/get_resistance());
    }
    if(get_node_1()!= REFERENCIA && get_node_2()!=REFERENCIA)
    {
        Yn[get_node_1()-1][get_node_2()-1] = Yn[get_node_1()-1][get_node_2()-1]-(1/get_resistance());
        Yn[get_node_2()-1][get_node_1()-1] = Yn[get_node_1()-1][get_node_2()-1];
    }
    
    update_historic(nodal_solution);
    if(get_node_1()!=REFERENCIA)
    {
        Yn[get_node_1()-1][num_vars] = Yn[get_var()-1][num_vars] - current_historic;
    }

    if(get_node_2()!=REFERENCIA)
    {
        Yn[get_node_2()-1][num_vars] = Yn[get_var()-1][num_vars] + current_historic;
    }
}