#include "CONSTANTES.h"
#include "matrix.h"
#include <iostream>
#include <iomanip>
#include <cmath>
#include <cstdio>
#include <stdio.h>

using namespace std;

/*
double** init_matrix_mna(int nv){
    
    double** Yn=new double*[nv];
    
    for (int i=0;i<nv;i++)
    {
        Yn[i]= new double[nv+1];
    }        
    
    for(int i = 0; i < nv; ++i)
        for(int j = 0; j < nv+1; ++j)
            Yn[i][j] = 0;
    
    return Yn;
}
*/

void init_matrix_mna(int nv,double** Yn1,double** Yn2){
    for (int i=0;i<nv;i++)
    {
        Yn1[i]= new double[nv+1];
        Yn2[i]= new double[nv+1];
    }        
    
    for(int i = 0; i < nv; ++i)
    {
        for(int j = 0; j < nv+1; ++j)
        {
            Yn1[i][j] = 0;
            Yn2[i][j] = 0;
        }
    }
}


void delete_matrix(int nv, double** Yn)
{
  for(int i = 0; i < nv; ++i)
    delete [] Yn[i];
    delete [] Yn;
}

void print_matrix(int nv, double** Yn){
    /* Opcional: Mostra o sistema apos a montagem da estampa*/
    for (int k=0; k<nv; k++) {
        for (int j=0; j<nv+1; j++)
            if (Yn[k][j]!=0)
            {
                if(Yn[k][j]<1)
                {
                    cout << setprecision(3) << fixed << setw(3) << showpos;
                }
                else
                {
                    cout << setprecision(1) << fixed << setw(3) << showpos;
                }
                cout <<Yn[k][j] << "  ";
            }
            else cout << " ..... ";
        cout << endl;
    }
    cout << endl;
}

void copy_matrix(int nv, double** Yn_source, double** Yn_destiny)
{
    for(int i=0;i<nv;i++)
    {
        for(int j=0;j<nv+1;j++)
        {
            Yn_destiny[i][j]=Yn_source[i][j];
        }
    }
}

bool solve(int nv, double** Yn){
    int i, j, l, a;
    double t, p;

    for( i = 0; i < nv; i++ )
    {
        t = 0.0;
        a = i;
        for( l = i; l < nv; l++ )
        {
            if( fabs( Yn[l][i] ) > fabs( t ) )
            {
                a = l;
                t = Yn[l][i];
            }
        }
        if( i != a)
        {
            for( l = 0; l <= nv; l++ )
            {
                p = Yn[i][l];
                Yn[i][l] = Yn[a][l];
                Yn[a][l] = p;
            }
        }
        if( fabs( t ) < TOLG )
        {
            printf("Sistema singular\n");
            return false;
        }
        for( j = nv; j >= 0; j--)
        {  // Basta j>i em vez de j>0 
            Yn[i][j] /= t;
            p = Yn[i][j];
            if(p!=0)
            {
                for( l = 0; l < nv; l++)
                {
                    if( l != i )
                        Yn[l][j] -= (Yn[l][i]*p);
                }
            }
            
        }
    }
    return true;
}