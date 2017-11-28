#include "Element.h"
#include <vector>
#include <string>
#include "CONSTANTES.h"
#include "matrix.h"
#include <iostream>
#include <string.h>     /* atoi */
#include <sstream>



using namespace std;

Element::Element()
{
    this->name="";
    this->type="";
    this->value=0;
    this->node_1=0;
    this->node_2=0;
    this->var=0;
    this->resistance=0;
}

Element::Element(vector<string> element,int num_var)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
  var=num_var;
}

Element::Element(vector<string> element)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
  var=0;
  resistance=get_value();
}
double Element:: get_voltage(double** Yn_original, int num_vars)
{
    double voltage_node_starting=0;
    double voltage_node_ending=0;
    if(node_1 != 0)
    {
        voltage_node_starting= Yn_original[node_1-1][num_vars];
    }
    if(node_2 != 0)
    {
        voltage_node_ending= Yn_original[node_2-1][num_vars];
    }
    //cout<<get_name()<<": "<<voltage_node_starting<<"-"<<voltage_node_ending<<"="<<(voltage_node_starting-voltage_node_ending)<<endl;
    return (voltage_node_starting-voltage_node_ending);
}

void Element::set_type(string t)
{
  type=t;
}

void Element::set_name(string n)
{
  name=n;
}

double Element::get_resistance()
{
  return resistance;   
}


void Element::set_nodes(string n1,string n2)
{
  node_1=atoi(n1.c_str());
  node_2=atoi(n2.c_str());
}

void Element::set_value(string v)
{
  value=get_parsed_value(v);
}

void Element::set_num_var(int num_var)
{
   var=num_var;
}

string Element::get_name()
{
  return name;
}
string Element::get_type()
{
  return type;
}
double Element::get_value()
{
  return value;
}
int Element::get_node_1()
{
  return node_1;
}
int Element::get_node_2()
{
  return node_2;
}
int Element::get_var()
{
    return var;
}

double Element::get_parsed_value(string s)
{
    istringstream os(s);
    
    double d;
    os >> d;
    return d;
}

bool Element::getPlotCurrent()
{
    return plot_current;
}

bool Element::getPlotVoltage()
{
    return plot_voltage;
}

void Element::setPlotCurrent()
{
    plot_current=true;
}

void Element::setPlotVoltage()
{
    plot_voltage=true;;
}