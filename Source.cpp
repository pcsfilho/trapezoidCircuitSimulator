/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Source.cpp
 * Author: paulo
 * 
 * Created on 21 de Setembro de 2017, 23:32
 */
#include "Element.h"
#include "Source.h"

Source::Source() {
}

Source::Source(vector<string> element)
{
    type=element[0];
    name=element[1];
    current_type = element[2];
    node_1=atoi(element[3].c_str());
    node_2=atoi(element[4].c_str());
    value=atof(element[5].c_str());
    var=0;
}

void Source::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    if(type=="V")
    {
        if(get_node_1()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_var()-1] = 1;
            Yn_original[get_var()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_var()-1];
        }
        if(get_node_2()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_var()-1] = -1;
            Yn_original[get_var()-1][get_node_2()-1] = Yn_original[get_node_2()-1][get_var()-1];
        }
        if(get_node_1()!= REFERENCIA && get_node_2()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_node_2()-1] = Yn_original[get_node_1()-1][get_node_2()-1]-(1/get_resistance());
            Yn_original[get_node_2()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_2()-1];
        }
        Yn_original[get_var()-1][num_vars] = get_value();
    }
    else
    {
        if(get_node_1()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_var()] = Yn_original[get_node_1()-1][get_var()] - get_value();
        }
        if(get_node_2()!=REFERENCIA)
        {
            Yn_original[get_node_2()-1][get_var()] = Yn_original[get_node_2()-1][get_var()] + get_value();
        }
    }
}