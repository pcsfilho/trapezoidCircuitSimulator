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
#include <math.h>       /* sin */
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
    
    if(current_type=="AC")
    {
        frequency=atof(element[6].c_str());
    }
    var=0;
}

void Source::set_resistance(double r)
{
  resistance=r;
}

void Source::set_stamp_source(double** Yn_original,int num_vars, double time)
{
    double v;
    if(current_type.compare("AC")==0)
    {
        v = sin_source(time);
    }
    else
    {
        v = get_value();
    }
    
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
        //cout<<"V: "<<v<<endl;
        Yn_original[get_var()-1][num_vars] = v;
        
    }
    else
    {
        if(get_node_1()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_var()] = Yn_original[get_node_1()-1][get_var()] - v;
        }
        if(get_node_2()!=REFERENCIA)
        {
            Yn_original[get_node_2()-1][get_var()] = Yn_original[get_node_2()-1][get_var()] + v;
        }
    }
}


void Source::set_stamp(double** Yn_original, double** Yn_solution, int num_vars)
{
    double v;
        if(current_type.compare("AC")==0)
        {
            v = sin_source(0);
        }
        else
        {
            v = get_value();
        }
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
        //cout<<"V: "<<v<<endl;
        Yn_original[get_var()-1][num_vars] = v;
        
    }
    else
    {
        if(get_node_1()!=REFERENCIA)
        {
            Yn_original[get_node_1()-1][get_var()] = Yn_original[get_node_1()-1][get_var()] - v;
        }
        if(get_node_2()!=REFERENCIA)
        {
            Yn_original[get_node_2()-1][get_var()] = Yn_original[get_node_2()-1][get_var()] + v;
        }
    }
}

double Source::get_source_value(double time=0)
{
    if(current_type.compare("AC") ==0)
    {
        return sin_source(time);
    }
    else
    {
        return get_value();
    }
}
/**
 * Retorna o valor senoidal da fonte em @time
 * @param time
 * @return 
 */
double Source::sin_source(double time)
{
    return value*sin(2*PI*frequency*time);
}
