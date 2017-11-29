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
#include "matrix.h"

Inductor::Inductor()
{
    current=0;
    current_historic=0;
    plot_current=false;
    plot_voltage=false;
}

Inductor::Inductor(vector<string> element, int nv):Element(element,nv)
{
    current=0;
    current_historic=0;
    plot_current=false;
    plot_voltage=false;
}

void Inductor::set_resistance(double step)
{
    double temp=2*(value);
    resistance = temp/step;
}

double Inductor::get_current_historic()
{
    return current_historic;
}

double Inductor::get_current()
{
    return current;
}

void Inductor::update_current(double** Yn_solution, int num_vars)
{
    double ad=(1/get_resistance());
    if(node_2==REFERENCIA)
    {
        current=((ad)*(Yn_solution[node_1-1][num_vars])) + current_historic;
    }
    else
    {
        current=((ad)*(Yn_solution[node_1-1][num_vars]-Yn_solution[node_2-1][num_vars])) + current_historic;
    }    
}

void Inductor::update_historic(double** Yn_solution, int num_vars)
{
    update_current(Yn_solution,num_vars);
    double ad=(1/get_resistance());
    if(node_2==REFERENCIA)
    {
       current_historic = current + ((ad)*Yn_solution[node_1-1][num_vars]);
    }
    else
    {
        current_historic = current+((ad)*(Yn_solution[node_1-1][num_vars]-Yn_solution[node_2-1][num_vars]));
    }
    
}

void Inductor::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    if(get_node_1()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_1()-1] + (1/get_resistance());
    }
    
    if(get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_2()-1][get_node_2()-1] = Yn_original[get_node_2()-1][get_node_2()-1]+ (1/get_resistance());
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