#include "Node.h"

Node::Node() {
}

Node::Node(string v) {
    var=v;
}

void Node::add_solution(double s)
{
    solutions.push_back(s);
}

string Node::get_var_node()
{
    return var;
}

vector<double> Node::get_node_solutions()
{
    return solutions;
}