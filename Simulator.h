#ifndef SIMULATOR_H
#define SIMULATOR_H
#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include "Simulation.h"





using namespace std;

class Simulator
{
public:
    Simulator();
    Simulator(ifstream &netlistFile, Simulation *sim);
    void init_matrix_mna();
    void create_matrix_mna();
    bool run_mna_analysis();
    void print_matrix_mna();

private:
  Simulation* simulation;
  void parser_simulator(ifstream &netlistFile);
};

#endif