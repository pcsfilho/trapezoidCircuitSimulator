#include "Output.h"

using namespace std;

Output::Output()
{
    this->type="";
    this->node_1=0;
    this->node_2=0;
}

Output::Output(string node_1, string node_2,string type)
{
  set_type(type);
  set_nodes(node_1, node_2);
}

void Output::set_type(string t)
{
  type=t;
}

void Output::set_nodes(string n1,string n2)
{
  node_1=atoi(n1.c_str());
  node_2=atoi(n2.c_str());
}

string Output::get_type()
{
  return type;
}

double Output::get_value(double** Yn_original, int num_vars)
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
    
    if(type.compare("A")==0)
    {
        //cout<<"no 1: " <<get_node_1() <<endl <<"no 1: " <<get_node_2()<< endl <<"CORRENTE: " << (voltage_node_starting-voltage_node_ending)/(1e-6) <<endl;
        return (voltage_node_starting-voltage_node_ending)/W;
    }
    //cout<<"no 1: " <<get_node_1() <<endl <<"no 1: " <<get_node_2()<< endl <<"DDP: " << (voltage_node_starting-voltage_node_ending) <<endl;
    return (voltage_node_starting-voltage_node_ending);
}
int Output::get_node_1()
{
  return node_1;
}
int Output::get_node_2()
{
  return node_2;
}