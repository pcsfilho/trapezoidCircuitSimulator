#ifndef NODE_H
#define NODE_H
#include <string>
#include <vector>

using namespace std;

class Node {
public:
    Node();
    Node(string v);
    string get_var_node();
    vector<double> get_node_solutions();
    double get_last_node_solution();
    void add_solution(double s);
    
private:
    string var;
    vector<double> solutions;
};

#endif /* NODE_H */

