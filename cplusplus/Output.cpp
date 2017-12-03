using namespace std;

Element::Element()
{
    this->name="";
    this->type="";
    this->value=0;
    this->node_1=0;
    this->node_2=0;
    this->var=0;
    this->resistance=0;
    this->plot_current=false;
    this->plot_voltage=false;
}

Element::Element(vector<string> element,int num_var)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
  var=num_var;
  plot_current=false;
  plot_voltage=false;
}

Element::Element(vector<string> element)
{
  set_type(element[0]);
  set_name(element[1]);
  set_nodes(element[2],element[3]);
  set_value(element[4]);
  var=0;
  resistance=get_value();
  plot_current=false;
  plot_voltage=false;
}
double Element:: get_voltage(double** Yn_original, int num_vars)
{
    double voltage_node_starting=0;
    double voltage_node_ending=0;
    if(node_1 != 0)
    {
        voltage_node_starting= Yn_original[node_1-1][num_vars];
    }
    if(node_2 != 0)
    {
        voltage_node_ending= Yn_original[node_2-1][num_vars];
    }
    //cout<<get_name()<<": "<<voltage_node_starting<<"-"<<voltage_node_ending<<"="<<(voltage_node_starting-voltage_node_ending)<<endl;
    return (voltage_node_starting-voltage_node_ending);
}

void Element::set_type(string t)
{
  type=t;
}

void Element::set_nodes(string n1,string n2)
{
  node_1=atoi(n1.c_str());
  node_2=atoi(n2.c_str());
}

string Element::get_type()
{
  return type;
}

double Element::get_value(double** Yn_original, int num_vars)
{
    double voltage_node_starting=0;
    double voltage_node_ending=0;
    if(node_1 != 0)
    {
        voltage_node_starting= Yn_original[node_1-1][num_vars];
    }
    if(node_2 != 0)
    {
        voltage_node_ending= Yn_original[node_2-1][num_vars];
    }
    //cout<<get_name()<<": "<<voltage_node_starting<<"-"<<voltage_node_ending<<"="<<(voltage_node_starting-voltage_node_ending)<<endl;
    return (voltage_node_starting-voltage_node_ending);
}
int Element::get_node_1()
{
  return node_1;
}
int Element::get_node_2()
{
  return node_2;
}

double Element::get_parsed_value(string s)
{
    istringstream os(s);
    
    double d;
    os >> d;
    return d;
}