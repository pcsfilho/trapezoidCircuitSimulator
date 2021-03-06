#include <iostream>
#include <fstream>
#include <cstdlib>
#include <string>
#include <string.h>
#include <vector>
using namespace std;


int readNetlistFile(int argc, char** argv, ifstream& netlist)
{
    string file_path;

    switch(argc) {
        case 1: {
            cout << "Entre com o caminho do arquivo netlist .net: ";
            cin >> file_path;
            break;
        }

        case 2: 
        {
            file_path = argv[1];
            break;
        }

        default:
            cerr << "ERRO!" << endl;
            return -1;
    }

    netlist.open(file_path.c_str(), ifstream::in);
    //create_file(file_path.substr(0,file_path.length()-4).c_str());
    if(!netlist.is_open()){
        cerr << "ERRO: Este arquivo não pode ser aberto " << file_path << endl;
    return -1;
    }

    return 0;
}

void print(){
    cout << endl << "Análise de Circuitos"
         << endl << "Autor: Paulo Filho (paulo.ecomp@gmail.com)" << endl << endl;

    cout << "............o......o............................................." << endl <<
            ".........o............o.........................................." << endl <<
            ".......o................o........................................" << endl <<
            ".....o....................o......................................" << endl <<
            "....o......................o....................................." << endl <<
            "..o..........................o..................................." << endl <<
            ".o............................o.................................." << endl <<
            "o...............................o................................" << endl <<
            "..........---/\\/\\/\\--- ..........o.............-|(-.............o" << endl <<
            "..................................o............................o." << endl <<
            "...................................o..........................o.." << endl <<
            ".....................................o......................o...." << endl <<
            "......................................o....................o....." << endl <<
            "........................................o................o......." << endl <<
            "..........................................o............o........." << endl <<
            ".............................................o......o............" << endl << endl;
}

bool endsWith(const string& s, const string& suffix)
{
    return (s.size() >= suffix.size() &&
           s.substr(s.size() - suffix.size()) == suffix);
}

vector<string> split(const string& s, const string& delimiter, const bool& removeEmptyEntries=false)
{
    vector<string> tokens;
    for (size_t start = 0,end; start < s.length(); start = end+delimiter.length())
    {
        size_t pos= s.find(delimiter,start);
        end=pos!= string::npos ? pos: s.length();
        string token = s.substr(start, end-start);
        if(!removeEmptyEntries || !token.empty())
        {
            tokens.push_back(token);
        }

        if(!removeEmptyEntries && (s.empty() || endsWith(s, delimiter)))
        {
            tokens.push_back(token);
        }
        cout << token<<endl;
    }
    return tokens;
}

string create_file(string name)
{
    //cout<<name <<endl;
    string ext=".dat";
    name = name.substr(0, name.size()-1);
    name+=ext;
    //cout<<"Nome: "<<name<<endl;
    ofstream file;
    file.open(name.c_str());
    file.close();
    return name;
}
/*void write_in_file(string file_path, string line)
{
    ofstream file;
    file.open(file_path.c_str(), ios::app);
    file << line << std::endl;
    file.close();
}*/