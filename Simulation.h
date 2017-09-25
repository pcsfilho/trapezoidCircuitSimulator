#ifndef SIMULATION_H
#define SIMULATION_H
#include <string>
#include <vector>
#include "Circuit.h"

using namespace std;

class Simulation
{
public:
    Simulation();

    Simulation(Circuit circuit, float init_time, float end_time, float step_time);

    float get_initial_time();
    float get_end_time();
    float get_step_time();
    Circuit get_circuit();
    
    void set_initial_time(int i_time);
    void set_end_time(int e_time);
    void set_step_time(float s_time);
    
    void set_config_simulation(vector<string> data);
    string get_type_simulation();

private:
  Circuit circuit;
  float initial_time;
  float end_time;
  float step_time;
  string type_simulation;
};

#endif