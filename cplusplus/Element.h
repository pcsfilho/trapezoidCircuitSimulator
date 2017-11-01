#ifndef ELEMENT_H_
#define ELEMENT_H_
#include <vector>
#include <string>
#include <stdlib.h>
#include <iostream>
#include "CONSTANTES.h"

using namespace std;

class Element{
    public:
        Element();
        Element(vector<string> element, int num_var);
        Element(vector<string> element);
        string get_name();
        string get_type();
        double get_value();
        double get_resistance();
        int get_node_1();
        int get_node_2();
        int get_var();
        virtual void set_resistance(double r)=0;    
        //virtual void set_stamp(double** Yn_original, double** Yn_original,vector<double> nodal_solution,int num_vars)=0;
        virtual void set_stamp(double** Yn_original, double** Yn_solution,int num_vars)=0;
        void set_num_var(int num_var);
        double get_voltage(double** Yn_original,int num_vars);
    protected:
        string name;
        string type;
        double value;
        double resistance;
        int node_1;
        int node_2;
        int var;
        double get_parsed_value(string s);
    private:
        void set_nodes(string n1,string n2);
        void set_type(string t);
        void set_name(string n);
        void set_value(string value);
};

#endif