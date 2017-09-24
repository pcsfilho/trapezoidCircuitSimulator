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
TYPE_SIMULATION Simulation::get_type_simulation()
{
  return type_simulation;
}