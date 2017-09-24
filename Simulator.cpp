#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <algorithm>
#include <set>
#include "Simulation.h"
#include "Simulator.h"



using namespace std;

Simulator::Simulator()
{

}


Simulator::Simulator(ifstream &netlistFile, Simulation simulation)
{
    parser_simulator(netlistFile);
}

void Simulator::parser_simulator(ifstream &netlistFile)
{
    // Ground node!
    //variablesList[0] = "0";
    int numElements=0;
    int numNodes=0;
    string netlistLine;
    bool isValidElement;
    char netlistLinePrefix;
    getline(netlistFile, netlistLine);
    Circuit circuit = simulation.get_circuit();
    circuit.set_name(netlistLine);
    string delimiter = " ";
    vector<string> nodes;
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
                numElements++;
                circuit.add_element(tokens);

                if(!(std::find(nodes.begin(), nodes.end(), tokens[2]) !=nodes.end()) && tokens[2]!="0")
                {
                    numNodes++;
                    nodes.push_back(tokens[2]);
                }

                if(!(std::find(nodes.begin(), nodes.end(), tokens[3]) !=nodes.end()) && tokens[3]!="0")
                {
                    numNodes++;
                    nodes.push_back(tokens[3]);
                }
            }
            else if(tokens[0]==".")
            {
            }
        }
    }
    netlistFile.close();

    cout<< "Nome do circuito: "<< circuit.get_name() <<endl;
    cout<< "Número de Elementos:"<< numElements <<endl;
    cout<< "Número de Nos: "<< numNodes <<endl;
    circuit.set_num_nodes(numNodes);
    for (int i = 0; i < circuit.get_elements().size(); ++i)
    {
        cout<< circuit.get_elements()[i].get_type()<<endl;
        cout<<circuit.get_elements()[i].get_name()<<endl;
        cout<<circuit.get_elements()[i].get_node_1()<<endl;
        cout<<circuit.get_elements()[i].get_node_2()<<endl;
        cout<<circuit.get_elements()[i].get_value()<<endl;
    }
}
