/*
* File:   main.cpp
* Author: Paulo Cezar dos Santos Filho
* Created on 21 de Setembro de 2017, 23:24
*/
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <string>
#include <vector>
#include <cstdlib>
#include "utils.h"
#include "Simulator.h"

using namespace std;

int main(int argc, char **argv)
{
  ifstream netlistFile;

  print();
  int r=readNetlistFile(argc, argv, netlistFile);

  if(r==0)
  {
      Simulation* simulation = new Simulation();
      Simulator simulator;
      simulator = Simulator(netlistFile, simulation);
      simulation->set_output_file_name(create_file(simulation->get_circuit()->get_name()));
      simulator.init_matrix_mna();
      if(!simulator.run_mna_analysis())
      {
          cout<<"Nao converge"<<endl;
      }
  }
  else
  {
      cout<<"Arquivo nao encontrado"<<endl;
  }


  return 0;
}