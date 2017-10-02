#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include "Simulation.h"
#include "CONSTANTES.h"
#include "matrix.h"
#include "Switch.h"

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
    matrix_mna=new double*[circuit.get_num_vars()];
    matrix_mna_aux=new double*[circuit.get_num_vars()];
    init_matrix_mna(circuit.get_num_vars(),matrix_mna,matrix_mna_aux);
}

void Simulation::build_matriz_mna()
{
    Element* element;
    for(int i=0; i<circuit.get_num_elements();i++)
    {
        element = circuit.get_element_by_index(i);        
        element->set_stamp(matrix_mna,matrix_mna_aux,get_circuit()->get_num_vars());      
    }
    cout<<"BUILD"<<endl;
    print_matrix(get_circuit()->get_num_vars(), matrix_mna);
}

void Simulation::update_matriz_mna()
{
    Element* element;
    
    for(int i=0; i<circuit.get_num_elements();i++)
    {
        cout<<"UPDATE"<<endl;
        element = circuit.get_element_by_index(i);        
        if(element->get_type()=="C")
        {   
            Capacitor* c = dynamic_cast<Capacitor*>(element);
            c->update_historic(matrix_mna_aux, get_circuit()->get_num_vars());
            if(c->get_node_1()!=REFERENCIA)
            {
                matrix_mna[c->get_node_1()-1][get_circuit()->get_num_vars()]=-c->get_current_historic();
            }
            if(c->get_node_2()!=REFERENCIA)
            {
                matrix_mna[c->get_node_2()-1][get_circuit()->get_num_vars()]=c->get_current_historic();
            }
        }
        else if(element->get_type()=="L")
        {
            cout<<"ESTAMPA L"<<endl;
            Inductor* l = dynamic_cast<Inductor*>(element);
            l->update_historic(matrix_mna_aux, get_circuit()->get_num_vars());
            cout<<"il: "<<l->get_current()<<endl;
            cout<<"Il: "<<l->get_current_historic()<<endl;
            if(l->get_node_1()!=REFERENCIA)
            {
                matrix_mna[l->get_node_1()-1][get_circuit()->get_num_vars()]= -l->get_current_historic();
            }
            if(l->get_node_2()!=REFERENCIA)
            {
                matrix_mna[l->get_node_2()-1][get_circuit()->get_num_vars()]= l->get_current_historic();
            }
        }
        else if(element->get_type()=="S")
        {
            cout<<"ESTAMPA S"<<endl;
            Switch* s = dynamic_cast<Switch*>(element);
            s->set_stamp_switch(matrix_mna,current_time);
            
        }
        else  if(element->get_type()=="V")
        {
            cout<<"ESTAMPA V"<<endl;
        }
        
        print_matrix(get_circuit()->get_num_vars(), matrix_mna);
    }
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
        copy_matrix(get_circuit()->get_num_vars(),matrix_mna,matrix_mna_aux);
        if(solve(get_circuit()->get_num_vars(),matrix_mna_aux))
        {
            cout<<"Converge"<<endl;
            print_matrix(get_circuit()->get_num_vars(),matrix_mna_aux);
        }
        else
        {
            cout<<"Nao converge"<<endl;
            return;
        }
        current_time = current_time + step_time;
        iteration++;
    }
    print_matrix(get_circuit()->get_num_vars(),matrix_mna_aux);
}
double** Simulation::get_matrix_mna()
{
  return matrix_mna;
}



double Simulation::get_initial_time()
{
  return initial_time;
}

vector<double> Simulation::get_nodal_solution()
{
    return nodal_solution;
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
        nodal_solution.push_back(0);
    }
}
