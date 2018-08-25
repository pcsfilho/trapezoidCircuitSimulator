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
    if(element[4].compare("1")==0)//fechado
    {
        status=true;
    }
    else
    {
        status=false;
    }
    for(int i=0;i<atoi(element[5].c_str());i++)
    {
        time_changes.push_back(get_parsed_value(element[6+i].c_str()));
    }
    
    value=RS;
    resistance = value;
    
    count_changes = 0;
    change=false;
}

void Switch::set_resistance(double r)
{
  resistance=r;
}


void Switch::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    set_stamp_switch(Yn_original,0);
}

bool Switch::set_change(bool s)
{
    change=s;
}

void Switch::set_stamp_switch(double** Yn_original, double time)
{    
    commutate_switch(time);
    
    if(time==0 || change)
    {
        //cout<<"Entrou pra mudar estampa"<<endl;
        //cout<<"Estado: "<<status<<endl;
        double admittance;
        if(status)
        {
            admittance = resistance;
        }
        else
        {
            admittance = 1/resistance;
        }
        //cout<<"Condutância: "<<admittance<<endl;
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
        change=false;
    }
    
}

bool Switch::has_event(double current_time)
{
    
    if ((count_changes < time_changes.size()))
    {
        if (current_time >= time_changes[count_changes])
        {
          //  cout<<"Verdade"<<endl;
            return true;
        }
    }
    //cout<<"Falso"<<endl;
    return false;
}

void Switch::commutate_switch(double current_time)
{
    /*cout<<"tempo corrente: "<<current_time<<"Array: "<<time_changes[count_changes]<<endl;
    if ((count_changes < time_changes.size()))
    {
        cout<<"if 1"<<endl;
        if (current_time >= time_changes[count_changes])
        {
            cout<<"if 2"<<endl;
            status = !status; 
            count_changes++;
            change=true;
        }
    }*/
    if(has_event(current_time))
    {
        status = !status; 
        count_changes++;
        change=true;
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