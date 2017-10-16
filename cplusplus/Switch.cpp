/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Switch.cpp
 * Author: paulo
 * 
 * Created on 1 de Outubro de 2017, 21:02
 */
#include "Element.h"
#include "Switch.h"

Switch::Switch()
{
}

Switch::Switch(vector<string> element)
{
    type=element[0];
    name=element[1];
    node_1=atoi(element[2].c_str());
    node_2=atoi(element[3].c_str());
    if(element[4]=="1")//fechado
    {
        status=true;
    }
    else
    {
        status=false;
    }
    
    for(int i=0;i<atoi(element[5].c_str());i++)
    {
        time_changes.push_back(atoi(element[6+i].c_str()));
    }
    value=RS;
    resistance = value;
    
    count_changes = 0;
}

void Switch::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    set_stamp_switch(Yn_original,0);
}

void Switch::set_stamp_switch(double** Yn_original, double time)
{
    
    commutate_switch(time);
    double admittance;
    if(status)
    {
        admittance = resistance;
    }
    else
    {
        admittance = 1/resistance;
    }
    
    if(get_node_1()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_1()-1]+admittance;
    }
    if(get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_2()-1][get_node_2()-1] = Yn_original[get_node_2()-1][get_node_2()-1]+admittance;
    }
    if(get_node_1()!= REFERENCIA && get_node_2()!=REFERENCIA)
    {
        Yn_original[get_node_1()-1][get_node_2()-1] = Yn_original[get_node_1()-1][get_node_2()-1]-admittance;
        Yn_original[get_node_2()-1][get_node_1()-1] = Yn_original[get_node_1()-1][get_node_2()-1];
    }
}

void Switch::commutate_switch(double current_time)
{
    if ((count_changes < time_changes.size()))
    {
        if (current_time>=time_changes[count_changes])
        {
            status = !status; 
            count_changes++;
        }
    }
}

int Switch::get_count_changes()
{
    return count_changes;
}

bool Switch::get_status()
{
    return status;
}