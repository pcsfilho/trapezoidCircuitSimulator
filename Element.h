#ifndef ELEMENT_H_
#define ELEMENT_H_
#include <vector>
#include <string>
#include <stdlib.h>
#include "CONSTANTES.h"

using namespace std;

class Element{
    public:
        Element();
        Element(vector<string> element);
        string get_name();
        string get_type();
        float get_value();
        int get_node_1();
        int get_node_2();
        int get_var();
        virtual void set_stamp(double** Yn)=0;
        void set_num_var(int num_var);
    protected:
        string name;
        string type;
        float value;
        int node_1;
        int node_2;
        int var;
    private:
        void set_nodes(string n1,string n2);
        void set_type(string t);
        void set_name(string n);
        void set_value(string value);
};

#endif