#include "Circuit.h"
#include "Switch.h"
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
  Element *element;
  
  if(tokens[0]=="V"||tokens[0]=="I")
  {
    element= new Source(tokens);
    element->set_resistance(0);
    
  }
  else if(tokens[0]=="C")
  {
    element = new Capacitor(tokens,numVars);    
    //double temp=2*(element->get_value());
    //double rc = step_time/temp;
    //element->set_resistance(rc);
  }
  else if(tokens[0]=="L")
  {
    element = new Inductor(tokens,numVars);
    //double temp=2*(element->get_value());
    //double rl =  temp/step_time;
    //element->set_resistance(rl);
  }
  else if(tokens[0]=="R")
  {
    element = new Resistor(tokens);
  }
  else
  {
    element= new Switch(tokens);
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
    Node* node=  new Node(var);
    add_var(var);
    nodes.push_back(node);
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

Element* Circuit::get_element_by_index(int index)
{
    return elements[index];
}

vector<Node*> Circuit::get_nodes()
{
    return nodes;
}
/**
 * 
 * @param matrix_solution
 */
string Circuit::set_node_values(double** matrix_solution)
{
    Node* node;
    string temp="";
    for(int i=0;i<get_nodes().size();i++)
    {
        stringstream ss (stringstream::in | stringstream::out);
        ss << matrix_solution[i][get_num_vars()];
        temp += ss.str() + " ";
        
        node = get_nodes()[i];
        
        node->add_solution(matrix_solution[i][get_num_vars()]);
    }
    temp = temp.substr(0, temp.size()-1);
    return temp;
}
/**
 */
void Circuit::print_nodes_solutions()
{
    Node* node;
    for(int i=0;i<get_nodes().size();i++)
    {
        node = get_nodes()[i];
        cout<<"No: "<<node->get_var_node()<<endl;
        for(int j=0;j<node->get_node_solutions().size();j++)
        {
            cout<<"Valor: "<<node->get_node_solutions()[j] <<endl;
        }
    }
}