#include "Element.h"
#include "Circuit.h"
#include "Capacitor.h"
#include "Inductor.h"
#include "Source.h"
#include "Resistor.h"
#include <iostream>
#include <sstream>  // for string streams
#include <string>  // for string
using namespace std;

//Constructors
Circuit::Circuit()
{
    numElements=0;
    numNodes=0;
    numVars=0;
    name="circuit";
}

Circuit::Circuit(string name, int numElements, int numNodes, vector<Element*> elements)
{

}

//methods

void Circuit::add_element(vector<string> tokens)
{
  //Element *element = new Element(tokens);
  Element *element;
  
  if(tokens[0]=="V"||tokens[0]=="I")
  {
     element= new Source(tokens);
  }
  else if(tokens[0]=="C")
  {
    element = new Capacitor(tokens);
  }
  else if(tokens[0]=="L")
  {
    element = new Inductor(tokens);
  }
  else if(tokens[0]=="R")
  {
    element = new Resistor(tokens);
  }
  else
  {
    //chave
  }
  
  elements.push_back(element);
  numElements++;
  
  if(tokens[0]=="V")
  {
      add_var("I_"+element->get_name());
  }
}

void Circuit::add_var(string var)
{
    cout<<"Variavel: "<<var<<endl;
    numVars++;
    if(elements.back()->get_type()=="V")
    {
        elements.back()->set_num_var(numVars);
    }
    vars.push_back(var);
}

//Sets
void Circuit::add_node(string var)
{
    add_var(var);
    numNodes++;
}

void Circuit::set_name(string n)
{
  name=n;
}
//Gets
string Circuit::get_name()
{
  return name;
}
vector<Element*> Circuit::get_elements()
{
  return elements;
}

int Circuit::get_num_vars()
{
    return numVars;
}

int Circuit::get_num_elements()
{
    return numElements;
}

int Circuit::get_num_nodes()
{
    return numNodes;
}

vector<string> Circuit::get_vars()
{
    return vars;
}