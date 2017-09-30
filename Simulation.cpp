#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include "Simulation.h"
#include "CONSTANTES.h"
#include "matrix.h"

using namespace std;

Simulation::Simulation()
{
  initial_time=0;
  end_time=0;
  step_time=0;
  current_time=0;
}

Simulation::Simulation(Circuit circuit, double init_time, double end_time,double step_time)
{

}

void Simulation::set_config_simulation(vector<string> data)
{
    type_simulation=data[1];
    
    if(type_simulation=="TRAN")
    {
        step_time = get_parsed_value(data[2]);
        initial_time = get_parsed_value(data[3]);
        end_time = get_parsed_value(data[4]);
    }
}

void Simulation::create_matrix_mna()
{
    matrix_mna=init_matrix_mna(circuit.get_num_vars());
    print_matrix(get_circuit()->get_num_vars(), matrix_mna);
}

void Simulation::build_matriz_mna()
{
    Element* element;
    print_matrix(circuit.get_num_vars(), get_matrix_mna());
    for(int i=0; i<circuit.get_num_elements();i++)
    {
        element = circuit.get_element_by_index(i);        
        element->set_stamp(get_matrix_mna(),get_current_nodal_solution(),get_circuit()->get_num_vars());      
        print_matrix(circuit.get_num_vars(), get_matrix_mna());
    }
    
}

void Simulation::update_matriz_mna()
{
    cout<<"atualizando matriz mna"<<endl;
}



void Simulation::run_analysis()
{
    int iteration=0;
    init_nodal_solution();
    while(current_time<=end_time)
    {
        cout<<"tempo: "<< current_time<<endl;
        if(current_time==0)
        {
            build_matriz_mna();
        }
        else
        {
            update_matriz_mna();
        }
        current_time = current_time + step_time;
        print_matrix(get_circuit()->get_num_vars(),get_matrix_mna());
        iteration++;
    }
}



double** Simulation::get_matrix_mna()
{
  return matrix_mna;
}



double Simulation::get_initial_time()
{
  return initial_time;
}

vector<double> Simulation::get_current_nodal_solution()
{
    return current_nodal_solution;
}

double Simulation::get_current_time()
{
  return current_time;
}


double Simulation::get_end_time()
{
  return end_time;
}
double Simulation::get_step_time()
{
  return step_time;
}
Circuit* Simulation::get_circuit()
{
  return &circuit;
}
string Simulation::get_type_simulation()
{
  return type_simulation;
}

double Simulation::get_parsed_value(string s)
{
    istringstream os(s);
    double d;
    os >> d;
    return d;
}

void Simulation::init_nodal_solution()
{
    for(int i=0;i<circuit.get_num_vars();i++)
    {
        current_nodal_solution.push_back(0);
    }
}