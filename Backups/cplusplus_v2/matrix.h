#ifndef MATRIX_H_
#define MATRIX_H_
#include "CONSTANTES.h"


//double** init_matrix_mna(int numVariables);//return a matriz 2D
void init_matrix_mna(int numVariables, double** Yn1,double** Yn2);
void print_matrix(int nv, double** Yn);
bool solve(int nv, double** Y_mna);
void delete_matrix(int nv, double** Yn);
void copy_matrix(int nv, double** Yn_source, double** Yn_destiny);
void clear_matrix(int nv, double** Yn);
#endif
