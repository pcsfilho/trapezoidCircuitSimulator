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
        void add_element(vector<string> element);
        string get_name();
        vector<Element> get_elements();
        void set_name(string n);
        void set_num_nodes(int n);
    private:
      int numElements;
        int numNodes;
        string name;
        vector<Element> elements;
};
#endif