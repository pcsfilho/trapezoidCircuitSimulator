#include <iostream>
#include <string>
#include <vector>
#include "Simulation.h"
#include "CONSTANTES.h"

using namespace std;

Simulation::Simulation()
{
  initial_time=0;
  end_time=0;
  step_time=0;
}

Simulation::Simulation(Circuit circuit, float init_time, float end_time, float step_time)
{

}

void Simulation::set_config_simulation(vector<string> data)
{
    type_simulation=data[1];
    
    if(type_simulation=="TRAN")
    {
        cout<<"Passo: "<<data[2]<<endl;
        cout<<"Tempo Inicial: "<<data[3]<<endl;
        cout<<"Tempo final: "<<data[4]<<endl;
    }
}

float Simulation::get_initial_time()
{
  return initial_time;
}
float Simulation::get_end_time()
{
  return end_time;
}
float Simulation::get_step_time()
{
  return step_time;
}
Circuit Simulation::get_circuit()
{
  return circuit;
}
string Simulation::get_type_simulation()
{
  return type_simulation;
}