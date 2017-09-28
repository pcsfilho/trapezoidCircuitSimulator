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

void Source::set_stamp(double** Yn)
{
    
}