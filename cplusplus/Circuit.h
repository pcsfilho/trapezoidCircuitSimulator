/*
 * File:   Circuit.h
 * Author: paulo
 * Created on 21 de Setembro de 2017, 23:33
 */
#ifndef CIRCUIT_H_
#define CIRCUIT_H_
#include <vector>
#include <string>
#include "Capacitor.h"
#include "Inductor.h"
#include "Source.h"
#include "Resistor.h"
#include "Switch.h"
#include "Node.h"

using namespace std;

class Circuit{
    public:
        Circuit();
        Circuit(string name, int numElements, int numNodes, vector<Element*> elements);
        void add_element(vector<string> tokens);
        string get_name();
        int get_num_vars();
        int get_num_nodes();
        int get_num_elements();
        vector<Node*> get_nodes();
        Element* get_element_by_index(int index);
        vector<Element*> get_elements();
        vector<string> get_vars();
        void set_name(string n);
        void add_node(string var);
        string set_node_values(double** matrix_solution, double time);        
        void print_nodes_solutions();
        //bool get_event();
        //void set_event();
        //void add_time_event(double time);
        
        bool hasEvent(double time);
    private:
        //atributtes
        int numElements;
        int numVars;
        int numNodes;
        string name;
        vector<Element*> elements;
        vector<Switch*> switches;
        vector<string> vars;
        vector<Node*> nodes;
        //bool event;
        //methods
        void add_var(string var);
};
#endif