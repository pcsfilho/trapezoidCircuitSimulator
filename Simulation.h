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

    Simulation(Circuit circuit, double init_time, double end_time, double step_time);

    double get_initial_time();
    double get_end_time();
    double get_step_time();
    double get_current_time();
    Circuit* get_circuit();
    
    void set_initial_time(int i_time);
    void set_end_time(int e_time);
    void set_step_time(float s_time);
    void set_config_simulation(vector<string> data);
    string get_type_simulation();
    double** get_matrix_mna();
    vector<double> get_current_nodal_solution();
    void create_matrix_mna();
    void run_analysis();
    
private:
  Circuit circuit;
  double initial_time;
  double end_time;
  double step_time;
  string type_simulation;
  double current_time;
  double** matrix_mna;
  vector<double> current_nodal_solution;
  double get_parsed_value(string s);
  
  void build_matriz_mna();
  void update_matriz_mna();
  void init_nodal_solution();
};

#endif