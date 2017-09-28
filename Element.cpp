#include "Element.h"
#include <vector>
#include <string>
#include "CONSTANTES.h"
#include <iostream>
#include <string.h>     /* atoi */

using namespace std;

Element::Element()
{
    name="";
    type="";
    value=0;
    node_1=0;
    node_2=0;
    var=0;
}

Element::Element(vector<string> element)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
  var=0;
}

void Element::set_type(string t)
{
  type=t;
}

void Element::set_name(string n)
{
    name=n;
}

void Element::set_nodes(string n1,string n2)
{
  node_1=atoi(n1.c_str());
  node_2=atoi(n2.c_str());
}

void Element::set_value(string v)
{
  value=atof(v.c_str());
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
float Element::get_value()
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