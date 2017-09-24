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
    Simulator(ifstream &netlistFile, Simulation simulation);

private:
  Simulation simulation;
  void parser_simulator(ifstream &netlistFile);
};

#endif