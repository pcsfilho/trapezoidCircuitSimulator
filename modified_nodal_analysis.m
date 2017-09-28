    clc;
    clear;

    display('----------Análise Transiente RLC--------');
    [fileName,pathName] = uigetfile('*.net','Selecione o arquivo .net');
    %%%%%%%%%%%%%%%Inicializa variaveis%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    REFERENCIA=0;
    num_elements =0;%Numero de Elementos no circuito
    num_vars =0;%Numero de Variï¿½veis para a anï¿½lise nodal modificada
    num_voltage_sources=0;%Numero de fontes de tensï¿½o
    num_current_sources=0;%Numero de fontes de corrente
    num_nodes=0;
    current_t=0;
    RS=1e9;
    aux_list = zeros(1,1);
    tstart=tic;
    netlist_file = fopen(fileName,'r'); %abre arquivo para leitura
    while ~feof(netlist_file)
        line = fgets(netlist_file); %lê linha a linha
        line = strsplit(line);
        if(line{1}=='R' || line{1}=='L' || line{1}=='C' || line{1}=='S' || line{1}=='V' || line{1}=='I')
            num_elements=num_elements+1;
            NETLIST(num_elements).NUM_VAR=0;
            NETLIST(num_elements).TYPE=line{1};
            NETLIST(num_elements).NAME=line{2};
            switch(line{1})
                case {'R','L','C'}
                    NETLIST(num_elements).VALUE=str2double(line{5});
                    NETLIST(num_elements).VOLTAGE=zeros(1,tam_vect);
                    NETLIST(num_elements).CURRENT=zeros(1,tam_vect);
                    NETLIST(num_elements).NODE1=str2double(line{3});
                    NETLIST(num_elements).NODE2=str2double(line{4});
                    if NETLIST(num_elements).TYPE=='C' || NETLIST(num_elements).TYPE=='L'
                        NETLIST(num_elements).NUM_VAR=num_vars;
                        NETLIST(num_elements).HISTORIC_CURRENT=zeros(1,tam_vect);
                    end
                case {'S'}
                    NETLIST(num_elements).VALUE=RS;
                    NETLIST(num_elements).NODE1=str2double(line{3});
                    NETLIST(num_elements).NODE2=str2double(line{4});
                    n=str2double(line{6});
                    NETLIST(num_elements).TIME_CHANGES = zeros(1,n);
                    NETLIST(num_elements).STATE=str2double(line{5});
                    for i=1:1:n
                        NETLIST(num_elements).TIME_CHANGES(i)= str2double(line{6+i});
                    end
                    NETLIST(num_elements).CHANGE=1;
                case {'V'}
                    NETLIST(num_elements).DC=0;
                    NETLIST(num_elements).NODE1=str2double(line{4});
                    NETLIST(num_elements).NODE2=str2double(line{5});
                    num_voltage_sources = num_voltage_sources+1;
                    num_vars =num_vars+1;
                    list_vars(num_vars)=strcat('I_',line(2));
                    NETLIST(num_elements).NUM_VAR=num_vars;
                    NETLIST(num_elements).VALUE=str2double(line{6});
                    if ismember(line{3},'AC')
                       NETLIST(num_elements).DC=1;
                       NETLIST(num_elements).FREQ=str2double(line{7});
                       NETLIST(num_elements).VALUES =  NETLIST(num_elements).VALUE*(sin(2*pi*NETLIST(num_elements).FREQ*time));
                    else
                       NETLIST(num_elements).VALUES(1:tam_vect) =  NETLIST(num_elements).VALUE;
                    end
                case {'I'}
                    NETLIST(num_elements).DC=0;
                    num_current_sources = num_current_sources+1;
                    NETLIST(num_elements).NODE1=str2double(line{4});
                    NETLIST(num_elements).NODE2=str2double(line{5});
                    NETLIST(num_elements).VALUE=str2double(line{6});
                    if ismember(line{3},'AC')
                       NETLIST(num_elements).DC=1;
                       NETLIST(num_elements).FREQ=str2double(line{7});
                       NETLIST(num_elements).VALUES =  NETLIST(num_elements).VALUE*(sin(2*pi*NETLIST(num_elements).FREQ*time));
                    else
                       NETLIST(num_elements).VALUES(1:tam_vect) =  NETLIST(num_elements).VALUE;
                    end
            end
                        %Testa se o nï¿½ de partida ja foi adicionado a lista de variaveis
            if((ismember(NETLIST(num_elements).NODE1,aux_list)==0) && (NETLIST(num_elements).NODE1~=REFERENCIA))
                num_vars=num_vars+1;
                num_nodes = num_nodes+1;
                aux_list(num_nodes)=NETLIST(num_elements).NODE1;
                list_vars(num_vars)={num2str(NETLIST(num_elements).NODE1)};
            end
            %Testa se o nï¿½ de destino ja foi adicionado a lista de variaveis
            if((ismember(NETLIST(num_elements).NODE2,aux_list)==0) && (NETLIST(num_elements).NODE2~=REFERENCIA))
                num_vars=num_vars+1;
                num_nodes = num_nodes+1;
                aux_list(num_nodes)=NETLIST(num_elements).NODE2;
                list_vars(num_vars)={num2str(NETLIST(num_elements).NODE2)};
            end

        elseif(line{1}=='.')
            step_t=str2double(line{3});
            initial_time=str2double(line{4});
            end_time=str2double(line{5});
            time=initial_time:step_t:end_time;
            tam_vect = size(time,2);
        end
    end
    fclose(netlist_file);
    modified_nodal_solutions=zeros(1,tam_vect);

    disp('******************NETLIST*********************');
    for i=1:1:num_elements
        disp(['nome: ',NETLIST(i).NAME]);
        disp(['No 1: ',num2str(NETLIST(i).NODE1)]);
        disp(['No 2: ',num2str(NETLIST(i).NODE2)]);
        disp(['Valor: ',num2str(NETLIST(i).VALUE)]);
        disp(['Variavel: ',num2str(NETLIST(i).NUM_VAR)]);
    end
    disp('Variaveis da analise nodal modificada');
    for i=1:1:num_vars
        disp(['Variavel: ',list_vars{i}]);
    end
    disp(['Numero de nos: ',num2str(num_nodes)]);
    disp(['Numero de variaveis: ',num2str(num_vars)]);
    disp(['Numero de Elementos: ',num2str(num_elements)]);


    %Inicializa matriz nodal modificada
    modified_nodal=zeros(num_vars,num_vars+1);
    it=1;
    t=0;
    Y=zeros(num_vars,num_vars+1);
    while current_t <= end_time
            %///////////////////////////////////////////////////////////////////////////
            %/* Monta sistema nodal */
            %///////////////////////////////////////////////////////////////////////////
           if current_t==0
               [Y,netlist] = build_modified_nodal_system(Y,num_elements,num_vars,NETLIST,modified_nodal_solutions,step_t,current_t,it,0);
           else
               [Y,netlist] = update_modified_nodal_system(Y,num_elements,num_vars,NETLIST,modified_nodal_solutions,current_t,it,0);
           end
            NETLIST=netlist;
%             A=Y(1:3,1:3);
%             A=inv(A);
%             B=Y(1:3,4);
            solucao_atual = solve_system_equations(Y,num_vars);
%             solucao_atual=A*B;

            %Atualizando ultima solucao
            for i=1:1:num_vars
                modified_nodal_solutions(i) = solucao_atual(i,num_vars+1);
            end
            %Atualiza tensão sobre cada dispositivo
            for i=1:1:num_elements
                if NETLIST(i).TYPE~='V' && NETLIST(i).TYPE~='I'
                    NETLIST(i).VOLTAGE(it) = update_voltage_on_device(modified_nodal_solutions,NETLIST(i));
                end
            end
            current_t = current_t + step_t;
            it=it+1;
    end
    toc(tstart);
    %Atualiza ultima posição do vetor de tensões sobre cada dispositivo
%     for i=1:1:num_elements
%         if NETLIST(i).TYPE~='V' && NETLIST(i).TYPE~='I'
%             NETLIST(i).VOLTAGE(tam_vect) = update_voltage_on_device(modified_nodal_solutions,NETLIST(i));
%         end
%     end
%
%
%     row= num_elements-(num_voltage_sources+num_current_sources);
%     position=0;
%     time=time*1000;
%     end_time = end_time*1000;
%     for i=1:1:num_elements
%         if NETLIST(i).TYPE=='L' || NETLIST(i).TYPE=='C' || NETLIST(i).TYPE=='R'
%             figure();
%             subplot(1,2,1)  % Top figure
%             plot(time,NETLIST(i).VOLTAGE);
%             %axis([initial_time end_time min(NETLIST(i).VOLTAGE) max(NETLIST(i).VOLTAGE)]);
%             xlim([initial_time end_time])
%             xlabel('Tempo (ms)');  ylabel('Tensão (V)');
%             legend('Trapezoidal');
%             title(['Tensão sobre ', NETLIST(i).NAME]);
%
%             subplot(1,2,2)  % Top figure
%             plot(time,NETLIST(i).CURRENT);
%
%             %axis([initial_time end_time min(NETLIST(i).CURRENT) max(NETLIST(i).CURRENT)]);
%             xlim([initial_time end_time])
%             xlabel('Tempo (ms)');  ylabel('Corrente (A)');
%             legend('Trapezoidal');
%             title(['Corrente em ', NETLIST(i).NAME]);
%         end
%     end