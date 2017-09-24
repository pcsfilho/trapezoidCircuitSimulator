#include "Element.h"
#include "Circuit.h"
#include <iostream>
using namespace std;

Circuit::Circuit()
{
  numElements=0;
    numNodes=0;
    name="circuit";
}

Circuit::Circuit(string name, int numElements, int numNodes, vector<Element> elements)
{

}

string Circuit::get_name()
{
  return name;
}

void Circuit::add_element(vector<string> tokens)
{
  Element element(tokens);
  elements.push_back(element);
}

vector<Element> Circuit::get_elements()
{
  return elements;
}

void Circuit::set_name(string n)
{
  name=n;
}
void Circuit::set_num_nodes(int n)
{
  numNodes=n;
}