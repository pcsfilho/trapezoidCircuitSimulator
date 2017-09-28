#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <set>
#include "Simulation.h"
#include "Simulator.h"
#include "matrix.h"



using namespace std;

Simulator::Simulator()
{

}


Simulator::Simulator(ifstream &netlistFile, Simulation* sim)
{
    simulation = sim;
    parser_simulator(netlistFile);
}

void Simulator::init_matrix_mna()
{
    simulation->create_matrix_mna();   
}

void Simulator::print_matrix_mna()
{
    print_matrix(simulation->get_circuit()->get_num_vars(),simulation->get_matrix_mna());
}


void Simulator::parser_simulator(ifstream &netlistFile)
{
    string netlistLine;
    getline(netlistFile, netlistLine);
    Circuit* circuit = simulation->get_circuit();
    circuit->set_name(netlistLine);
    string delimiter = " ";
    vector<string> nodes;
    vector<string> vars;
    while (getline(netlistFile, netlistLine))
    {
        if(netlistLine[0]!='*')
        {
            size_t pos=0;
            string token;
            string delimiter=" ";
            vector<string> tokens;

            while((pos=netlistLine.find(delimiter))!=string::npos)
            {
                token=netlistLine.substr(0,pos);
                netlistLine.erase(0, pos +delimiter.length());
                tokens.push_back(token);
                if((pos=netlistLine.find(delimiter))==string::npos)
                {
                    token = netlistLine;
                    tokens.push_back(token);
                }
            }
            if(tokens[0]=="R" || tokens[0]=="L" || tokens[0]=="C" || tokens[0]=="I" || tokens[0]=="V")
            {
                circuit->add_element(tokens);
                string node_1;
                string node_2;
                if(tokens[0]=="V" || tokens[0]=="I")
                {
                    node_1 = tokens[3];
                    node_2 = tokens[4];
                }
                else
                {
                    node_1 = tokens[2];
                    node_2 = tokens[3];
                }
                
                if(!(std::find(nodes.begin(), nodes.end(), node_1) !=nodes.end()) && node_1!="0")
                {
                    circuit->add_node(node_1);
                    nodes.push_back(node_1);
                }

                if(!(std::find(nodes.begin(), nodes.end(), node_2) !=nodes.end()) && node_2!="0")
                {
                    circuit->add_node(node_2);
                    nodes.push_back(node_2);
                }
            }
            else if(tokens[0]==".")
            {
                simulation->set_config_simulation(tokens);
            }
        }
    }
    netlistFile.close();

    cout<< "Nome do circuito: "<< circuit->get_name() <<endl;
    cout<< "Número de Elementos:"<< circuit->get_num_elements() <<endl;
    cout<< "Numero de Nos: "<< circuit->get_num_nodes() <<endl;
    cout<< "Número de Variaveis: "<< circuit->get_num_vars() <<endl;
    cout<<"Simulaçao: "<<endl;
    cout<<"Tipo: "<<simulation->get_type_simulation()<<endl;
    cout<<"Passo: "<<simulation->get_step_time()<<endl;
    cout<<"Tempo inicial: "<<simulation->get_initial_time() <<endl;
    cout<<"Tempo final: "<<simulation->get_end_time() <<endl;
    
    cout<<"Variaveis: "<<endl;
    for (int i = 0; i < circuit->get_num_vars(); ++i)
    {
        cout<<circuit->get_vars()[i]<<endl;
    }
    
    cout<<"Elementos: "<<endl;
    for (int i = 0; i < circuit->get_elements().size(); ++i)
    {
        cout<<"Tipo: "<<circuit->get_elements()[i]->get_type()<<endl;
        cout<<"Nome: "<<circuit->get_elements()[i]->get_name()<<endl;
        cout<<"No 1: "<<circuit->get_elements()[i]->get_node_1()<<"No 2: "<<circuit->get_elements()[i]->get_node_2()<<endl;
        cout<<"Valor: "<<circuit->get_elements()[i]->get_value()<<endl;
        cout<<"Var: "<<circuit->get_elements()[i]->get_var()<<endl;
    }
}


void Simulator::run_mna_analysis()
{
    simulation->run_analysis();
}
