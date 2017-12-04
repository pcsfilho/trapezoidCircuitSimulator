#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <fstream>
#include "Simulation.h"
#include "CONSTANTES.h"
#include "matrix.h"
#include "Switch.h"
#include "Output.h"
//#include "utils.h"

using namespace std;

Simulation::Simulation()
{
  initial_time=0;
  end_time=0;
  step_time=0;
  current_time=0;
  
}

Simulation::Simulation(Circuit circuit, double init_t, double end_t,double step_t)
{
    initial_time=init_t;
    end_time=end_t;
    step_time=step_t;
    current_time=0;
}

void Simulation::set_config_simulation(vector<string> data)
{
    
    if(data[1].compare("TRAN")==0)
    {
        type_simulation=data[1];
        step_time = get_parsed_value(data[2]);
        initial_time = get_parsed_value(data[3]);
        end_time = get_parsed_value(data[4]);
    }
    else if(data[0].compare("M") == 0 || data[0].compare("A")==0)
    {
        Output* out = new Output(data[2], data[3] ,data[0]);
        outputs.push_back(out);
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
    //cout<<"BUILD"<<endl;

    for(int i=0; i<circuit.get_num_elements();i++)
    {
        element = circuit.get_element_by_index(i);

        if((element->get_type().compare("C") ==0 || element->get_type().compare("L")==0) && current_time==0 )
        {
            element->set_resistance(step_time);
        }

        if(element->get_type().compare("S")==0 && current_time!=0)
        {
            Switch* s = dynamic_cast<Switch*>(element);
            s->set_change(true);
            s->set_stamp_switch(matrix_mna,current_time);
        }
        else if(element->get_type().compare("V")==0 && current_time!=0)
        {
            Source* s = dynamic_cast<Source*>(element);
            s->set_stamp_source(matrix_mna,get_circuit()->get_num_vars(),current_time);
        }
        else
        {
            element->set_stamp(matrix_mna,matrix_mna_aux,get_circuit()->get_num_vars());
        }

        //cout<<"Elemento: "<<element->get_name()<<endl;
        //print_matrix(get_circuit()->get_num_vars(), matrix_mna);
    }
    //print_matrix(get_circuit()->get_num_vars(), matrix_mna);
}

void Simulation::update_matriz_mna()
{
    Element* element;
    //cout<<"UPDATE"<<endl;

    for (int k=0; k<circuit.get_num_vars(); k++)
    {
        //for (int j=0; j<circuit.get_num_vars()+1; j++)
        //{
            matrix_mna[k][circuit.get_num_vars()]=0;
        //}
    }

    for(int i=0; i<circuit.get_num_elements();i++)
    {
        //cout<<"UPDATE"<<endl;
        element = circuit.get_element_by_index(i);
        if(element->get_type()=="C")
        {
    //        cout<<"CAPACITOR"<<endl;
            Capacitor* c = dynamic_cast<Capacitor*>(element);
            c->update_historic(matrix_mna_aux, get_circuit()->get_num_vars());
            if(c->get_node_1()!=REFERENCIA)
            {
                matrix_mna[c->get_node_1()-1][get_circuit()->get_num_vars()]=matrix_mna[c->get_node_1()-1][get_circuit()->get_num_vars()] - (c->get_current_historic());
            }
            if(c->get_node_2()!=REFERENCIA)
            {
                matrix_mna[c->get_node_2()-1][get_circuit()->get_num_vars()]=matrix_mna[c->get_node_2()-1][get_circuit()->get_num_vars()]+ (c->get_current_historic());
            }
        }
        else if(element->get_type()=="L")
        {
    //        cout<<"INDUTOR"<<endl;
            Inductor* l = dynamic_cast<Inductor*>(element);
            l->update_historic(matrix_mna_aux, get_circuit()->get_num_vars());
            if(l->get_node_1()!=REFERENCIA)
            {
                matrix_mna[l->get_node_1()-1][get_circuit()->get_num_vars()]= matrix_mna[l->get_node_1()-1][get_circuit()->get_num_vars()] - l->get_current_historic();
            }
            if(l->get_node_2()!=REFERENCIA)
            {
                matrix_mna[l->get_node_2()-1][get_circuit()->get_num_vars()]=matrix_mna[l->get_node_2()-1][get_circuit()->get_num_vars()] + l->get_current_historic();
            }
        }
        else if(element->get_type()=="S")
        {
    //        cout<<"CHAVE"<<endl;
            Switch* s = dynamic_cast<Switch*>(element);
            s->set_stamp_switch(matrix_mna,current_time);
        }
        else  if(element->get_type()=="V")
        {
    //        cout<<"FONTE"<<endl;
            Source* s = dynamic_cast<Source*>(element);
            matrix_mna[s->get_var()-1][get_circuit()->get_num_vars()]=s->get_source_value(current_time);
        }
        else
        {
    //        cout<<"RESISTOR"<<endl;
        }
        
    }
    //print_matrix(get_circuit()->get_num_vars(),matrix_mna);
}
/**
 * Funçao que faz iteraçoes a partir do netlist para obter a soluçao do sistema de acordo com as
 * configuraçoes de simulaçao definidas pelo usuario.
 */
bool Simulation::run_analysis()
{
    int iteration=0;
    init_nodal_solution();



    while(current_time<=end_time)
    {
    //    cout<<"tempo: "<< current_time<<endl;

        if(current_time==0)
        {
            build_matriz_mna();
        }
        else
        {
            if(get_circuit()->hasEvent(current_time))
            {
    //            cout<<"evento"<<endl;
                clear_matrix(get_circuit()->get_num_vars(),matrix_mna);
                build_matriz_mna();
            }
            else
            {
                update_matriz_mna();
            }

        }
        copy_matrix(get_circuit()->get_num_vars(),matrix_mna,matrix_mna_aux);
        if(solve(get_circuit()->get_num_vars(),matrix_mna_aux))
        {
    //        cout<<"Converge"<<endl;
           // print_matrix(get_circuit()->get_num_vars(),matrix_mna_aux);
            write_in_file(output_file_name,calculate_plot_outputs());
            //printf("escreveu");
            //cout<<calculate_plot_outputs()<<endl;
            //plot 'SWITCH_AC.dat' using 1:2 with lines, 'SWITCH_AC.dat' using 1:3 with lines, 'SWITCH_AC.dat' using 1:4 with lines
                //plot 'SWITCH_AC.dat' using 1:2 title "Corrente em L1" with lines
            //set xrange [0:0.15]

            /*
             * set terminal png ;
             * set output 'SWITCH_AC.png';
             gnuplot> set multiplot layout 3,1
             * set encoding utf8
             * * set title "Tensão em C1"
             * set ylabel "Tensao (V)"

unset key
multiplot> plot [0:0.15] [-2.5:2.5] 'SWITCH_AC.dat' using 1:3 with lines
             * set title "Corrente em L1"
unset key
multiplot> plot [0:0.15] [-1:1] 'SWITCH_AC.dat' using 1:2 with lines
             * * set title "Corrente em L2"
             * set xlabel "Tempo (s)"
unset key
plot [0:0.15] [-1:1] 'SWITCH_AC.dat' using 1:4  with lines

             */
        }
        else
        {
            return false;
        }
        current_time = current_time + step_time;
        iteration++;
    }
    //cout<<"Resultado Final"<<endl;
    //print_matrix(get_circuit()->get_num_vars(),matrix_mna_aux);
    return true;
}

string Simulation::calculate_plot_outputs()
{
    Output* o;
    stringstream ss (stringstream::in | stringstream::out);
    ss << current_time;
    string temp="";
    temp=ss.str();
    for(int i=0;i<outputs.size();i++)
    {
        o=outputs[i];
        stringstream ss (stringstream::in | stringstream::out);
        double value;
        value = o->get_value(matrix_mna_aux, circuit.get_num_vars());
        ss << value;
        temp +=" "+ss.str();
    }
    temp = temp.substr(0, temp.size()-1);
    //cout<<"LINHA: " <<temp<<endl;
    return temp;
}

void Simulation::set_output_file_name(string name)
{
    output_file_name=name;
}
/**
 *
 * @return
 */
double** Simulation::get_matrix_mna()
{
  return matrix_mna;
}
/**
 *
 * @return
 */
double Simulation::get_initial_time()
{
  return initial_time;
}
/**
 *
 * @return
 */
vector<double> Simulation::get_nodal_solution()
{
    return nodal_solution;
}
/**
 *
 * @return
 */
double Simulation::get_current_time()
{
  return current_time;
}
/**
 *
 * @return
 */
double Simulation::get_end_time()
{
  return end_time;
}
/**
 *
 * @return
 */
double Simulation::get_step_time()
{
  return step_time;
}
/**
 *
 * @return
 */
Circuit* Simulation::get_circuit()
{
  return &circuit;
}
/**
 *
 * @return
 */
string Simulation::get_type_simulation()
{
  return type_simulation;
}
/**
 *
 * @return
 */
double Simulation::get_parsed_value(string s)
{
    istringstream os(s);
    double d;
    os >> d;
    return d;
}
/**
 *
 */
void Simulation::init_nodal_solution()
{
    for(int i=0;i<circuit.get_num_vars();i++)
    {
        nodal_solution.push_back(0);
    }
}

void Simulation::write_in_file(string file_path, string line)
{
    ofstream file;
    file.open(file_path.c_str(), ios::app);
    file << line << std::endl;
    file.close();
}