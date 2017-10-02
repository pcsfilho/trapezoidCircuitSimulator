/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Resistor.cpp
 * Author: paulo
 * 
 * Created on 21 de Setembro de 2017, 23:32
 */

#include "Resistor.h"

Resistor::Resistor() 
{
}

Resistor::Resistor(vector<string> element):Element(element)
{}

void Resistor::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
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
}