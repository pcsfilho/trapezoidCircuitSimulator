//g++ -o libsimulation_jni.so  -shared -fPIC -I/usr/lib/jvm/java-8-oracle/include -I/usr/lib/jvm/java-8-oracle/include/linux  *.cpp -lc
//sudo cp libsimulation_jni.so /usr/lib

#include <iostream>
#include <string>
#include "org_jni_InterfaceJNI.h"
#include <fstream>
#include <sstream>
#include <iomanip>
#include <vector>
#include <cstdlib>
#include "Simulator.h"
#include "utils.h"


using namespace std;

JNIEXPORT jstring JNICALL Java_org_jni_InterfaceJNI_run_1analysis
  (JNIEnv *env, jobject o, jstring str)
{
    ifstream netlistFile;
    const char *org;
    org = env->GetStringUTFChars(str,NULL);

    int r=readNetlistFile(org,  netlistFile);
    env->ReleaseStringUTFChars(str, org);
    if(r==0)
  {
      Simulation* simulation = new Simulation();
      Simulator simulator;
      simulator = Simulator(netlistFile, simulation);
      string path=create_file(simulation->get_circuit()->get_name());
      simulation->set_output_file_name(path);
      simulator.init_matrix_mna();
      if(!simulator.run_mna_analysis())
      {
          return env->NewStringUTF(path.c_str());
      }
  }
  else
  {
      return env->NewStringUTF("Erro");
  }
}
