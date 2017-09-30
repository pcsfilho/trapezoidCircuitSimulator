/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Inductor.cpp
 * Author: paulo
 * 
 * Created on 21 de Setembro de 2017, 23:31
 */
#include "Inductor.h"
#include "CONSTANTES.h"

Inductor::Inductor()
{
    current=0;
    current_historic=0;
}

Inductor::Inductor(vector<string> element):Element(element)
{
    current=0;
    current_historic=0;
}


void Inductor::update_current(vector<double> nodal_solution)
{
    if(node_2==REFERENCIA)
    {
        current = ((1/resistance)*(nodal_solution[node_1])) + current_historic;
    }
    else
    {
        current = ((1/resistance)*(nodal_solution[node_1]-nodal_solution[node_2])) + current_historic;
    }
}

void Inductor::update_historic(vector<double> nodal_solution)
{
    update_current(nodal_solution);
    if(node_2==REFERENCIA)
    {
       current_historic = - current-((1/resistance)*nodal_solution[node_1]);
    }
    else
    {
        current_historic = - current-((1/resistance)*(nodal_solution[node_1]-nodal_solution[node_2]));
    }
}

void Inductor::set_stamp(double** Yn, vector<double> nodal_solution, int num_vars)
{
    cout<<"ESTAMPA INDUTOR"<<endl;
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