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

/* The program starts here */
int main(int argc, char **argv)
{
  ifstream netlistFile;
  Simulation simulation;
  Simulator simulator;
  
  print();
  readNetlistFile(argc, argv, netlistFile);
  simulator = Simulator(netlistFile, &simulation);

  simulator.init_matrix_mna();
  simulator.run_mna_analysis();


  return 0;
}