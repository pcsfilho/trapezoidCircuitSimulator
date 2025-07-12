# Trapezoid Circuit Simulator
Simulador de circuitos elétricos usando o método do trapézio para integração numérica, desenvolvido em C++.

# 📋 Visão Geral
O trapezoidCircuitSimulator é uma ferramenta interativa para modelagem e simulação de circuitos elétricos usando o método do trapézio na análise temporal (análise de transientes). Permite simular circuitos contendo resistores, capacitores e indutores, gerando gráficos de tensão e corrente ao longo do tempo.

# 🔧Funcionalidades
- Análise temporal (transiente) usando o método de integração do trapézio — balanceia precisão e estabilidade.
- Suporte a componentes básicos: resistor (R), capacitor (C) e indutor (L).
- Entrada por arquivo de descrição de rede (netlist), especificando componentes e conexões.
- Geração de gráficos dos resultados (tensão, corrente) ao longo da simulação.
- Interface GUI leve (se existir), ou interface por terminal.
- Código modular em C++, fácil de estender.

# 🚀 Instalação
## Pré-requisitos
- C++17 ou superior
- CMake (se o projeto usar CMake)
## Bibliotecas:
- [olcPixelGameEngine (opcional)] para GUI
- Bibliotecas de plotagem (e.g. matplotlib-cpp, gnuplot)

## Compilação
``
sh
git clone https://github.com/pcsfilho/trapezoidCircuitSimulator.git
cd trapezoidCircuitSimulator
mkdir build && cd build
cmake ..
make
``

# 🧩 Uso
1. Preparando o netlist
   - Crie um arquivo circuit.net com formato exemplo:
     ``
      R1 1 2 1000       # resistor de 1kΩ entre nó 1 e 2
      C1 2 0 1e-6       # capacitor de 1µF entre nó 2 e terra
      L1 2 3 1e-3       # indutor de 1mH entre nó 2 e 3
      V1 1 0 DC 5       # fonte DC de 5 V entre nó 1 e terra
      .tran 0 0.01 0 1e-5  # simulação de 0 a 10 ms com passo de 10µs
      .end
     ``
2. Executando
   ``./trapezoidSimulator circuit.net``
3. Visualizando resultados
 - A saída será gráfica (se GUI presente) ou via arquivos (CSV, gnuplot).
 - Use:
   `` plot_results.sh results.csv
``  
ou abra results.csv no Excel / LibreOffice / Python / MATLAB.


# 🧠 Como Funciona
O método do trapézio é uma técnica de integração implícita de segunda ordem. Ele utiliza valores nos instantes t e t+Δt para obter maior precisão. Isso evita amortecimento numérico excessivo, comum em métodos como Euler, sendo ideal para circuitos com capacitância e indutância


# 📈 Exemplos
O repositório contém exemplos nas pastas:

- examples/rc_circuit.net — carga RC com passo de 1 µs.

- examples/rl_circuit.net — carga RL com passo de 10 µs.

Obs: Veja a pasta examples/ para mais detalhes.

# 🛠️ Extensões Futuras
- Adição de novos componentes (fontes senoidais, diodos, transistores)
- Suporte a esquemas estáticos: DC OP, AC small signal
- Melhorias na interface gráfica
- Exportação de dados para JSON ou XML

Contribuições são bem-vindas! Abra uma issue ou pull request no repositório.
