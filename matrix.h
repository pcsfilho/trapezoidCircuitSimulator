#ifndef MATRIX_H_
#define MATRIX_H_
#include "CONSTANTES.h"


double** init_matrix_mna(int numVariables);//return a matriz 2D
void print_matrix(int nv, double** Yn);
bool solve(int nv, double** Yn);
void delete_matrix(int nv, double** Yn);


#endif
