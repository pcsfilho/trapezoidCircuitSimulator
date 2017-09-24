#include "Element.h"
#include <vector>
#include <string>
#include "CONSTANTES.h"
#include <iostream>

using namespace std;

Element::Element()
{
}

Element::Element(vector<string> element)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
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
  node_1=stoi(n1);
  node_2=stoi(n2);
}

void Element::set_value(string v)
{
  value=stof(v);
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