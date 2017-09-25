/*
 * File:   Circuit.h
 * Author: paulo
 *
 * Created on 21 de Setembro de 2017, 23:33
 */
#ifndef CIRCUIT_H_
#define CIRCUIT_H_
#include <vector>
#include <string>
#include "Element.h"

using namespace std;

class Circuit{
    public:
        Circuit();
        Circuit(string name, int numElements, int numNodes, vector<Element> elements);
        void add_element(vector<string> tokens);
        string get_name();
        int get_num_vars();
        int get_num_nodes();
        int get_num_elements();
        vector<Element> get_elements();
        vector<string> get_vars();
        void set_name(string n);
        void add_node(string var);
        
    private:
        //atributtes
        int numElements;
        int numVars;
        int numNodes;
        string name;
        vector<Element> elements;
        vector<string> vars;
        //methods
        void add_var(string var);
};
#endif