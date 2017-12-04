#ifndef OUTPUT_H
#define OUTPUT_H

#include <vector>
#include <string>
#include <stdlib.h>
#include <iostream>
#include "CONSTANTES.h"

using namespace std;

class Output{
    public:
        Output();
        Output(string node_1,string node_2 ,string type);
        string get_type();
        double get_value(double** Yn_original, int num_vars);
        int get_node_1();
        int get_node_2();
    protected:
        string type;
        int node_1;
        int node_2;
    private:
        void set_nodes(string n1,string n2);
        void set_type(string t);
};



#endif /* OUTPUT_H */

