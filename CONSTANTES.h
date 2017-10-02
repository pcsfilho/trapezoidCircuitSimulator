#ifndef CONSTANTES_H
#define CONSTANTES_H

#define DEBUG

static const int MAX_NAME = 11;
static const int MAX_NODES = 50;
static const int MAX_ELEMENTS = 50;
static const int REFERENCIA =0;
static const int RS=1e9;
static const double TOLG = 1e-9;

enum TYPE_SIMULATION {DC_SIMULATION, TRANS_SIMULATION};
enum TYPE_SOURCE_CURRENT {DC, AC};
enum TYPE_SOURCE {VOLTAGE, CURRENT};
enum TYPE_ELEMENT {R,L,C,S,V,I};

#endif