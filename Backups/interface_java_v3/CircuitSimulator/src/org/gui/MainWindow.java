package org.gui;

import org.gui.canvas.PanelCircuitArea;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import org.gui.elements.Simulation;
import org.gui.plot.ManagePlot;
import org.jni.InterfaceJNI;

public class MainWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private  Container container;
    private  JMenuBar menuBar;
    private  JMenu menuFile, menuEdit, menuAbout;
    private  JMenuItem newFile, openFile, saveFile, close, cut, copy, paste, clearFile, aboutMe, aboutSoftware;
    private  JToolBar mainToolbar;
    private JTextField timeSimulationtxt, stepSizetxt;
    JButton newButton, openButton, saveButton, clearButton, aboutMeButton, aboutButton,runButton,timeButton, pointerButton;
    private JTabbedPane tabbedPane;
    private JPanel principalPanel;
    private SideBar sideBar;
    private PanelCircuitArea canvas_panel;
    private ManagePlot manage_plots;
    
    TrapezoidCircuitSimulator circuit_simulator;

    // setup icons - File Menu
    private ImageIcon newIcon,openIcon,saveIcon,closeIcon,clearIcon,cutIcon,copyIcon,pasteIcon,aboutMeIcon,aboutIcon,
            capacitorIcon,inductorIcon,ResistorIcon,sourceVoltageIcon,sourceCurrentIcon, sourceVoltageACIcon, groundIcon, wireIcon, switchIcon,
            ammeterIcon, voltmeterIcon,transientIcon,dcIcon, nodeNameIcon,timeIcon,goIcon,pointerIcon;

    public MainWindow(TrapezoidCircuitSimulator tcs)
    {
        circuit_simulator=tcs;
        initIcons();
        initComponents();
        initSideBar();
    }
   
    private void initIcons()
    {
        // setup icons - File Menu
        newIcon = new ImageIcon("icons/new.png");
        openIcon = new ImageIcon("icons/open.png");
        saveIcon = new ImageIcon("icons/save.png");
        closeIcon = new ImageIcon("icons/close.png");
        goIcon = new ImageIcon("icons/go.png");
        timeIcon = new ImageIcon("icons/time_icon.png");
        pointerIcon = new ImageIcon("icons/pointer.png");

        // setup icons - Edit Menu
        clearIcon = new ImageIcon("icons/clear.png");
        cutIcon = new ImageIcon("icons/cut.png");
        copyIcon = new ImageIcon("icons/copy.png");
        pasteIcon = new ImageIcon("icons/paste.png");
        // setup icons - Help Menu
        aboutMeIcon = new ImageIcon("icons/about_me.png");
        aboutIcon = new ImageIcon("icons/about.png");
        //componentes eletronicoss
        capacitorIcon = new ImageIcon("icons/capacitor.png");
        inductorIcon = new ImageIcon("icons/inductor.png");
        ResistorIcon = new ImageIcon("icons/resistor.png");
        sourceVoltageACIcon = new ImageIcon("icons/ACsource.png");
        //ammeterIcon = new ImageIcon("icons/ammeter.png");
        sourceCurrentIcon = new ImageIcon("icons/currentSource.png");
        groundIcon = new ImageIcon("icons/ground.png");
        switchIcon = new ImageIcon("icons/switch.png");
        sourceVoltageIcon = new ImageIcon("icons/voltageSource.png");
        //voltmeterIcon = new ImageIcon("icons/voltmeter.png");
        wireIcon = new ImageIcon("icons/wire.png");
        transientIcon = new ImageIcon("icons/tran.png");
        dcIcon = new ImageIcon("icons/dc.png");
        nodeNameIcon=new ImageIcon("icons/nodename.png");
    }
    
    private void initMenu()
    {
        // Set the Menus
        menuFile = new JMenu("Arquivo");
        menuEdit = new JMenu("Editar");
        menuAbout = new JMenu("Sobre");
        //Font Settings menu
        // Set the Items Menu
        newFile = new JMenuItem("Novo", newIcon);
        openFile = new JMenuItem("Abrir", openIcon);
        saveFile = new JMenuItem("Salvar", saveIcon);
        close = new JMenuItem("Fechar", closeIcon);
        clearFile = new JMenuItem("Limpar", clearIcon);
        aboutMe = new JMenuItem("Sobre mim", aboutMeIcon);
        aboutSoftware = new JMenuItem("Sobre o Programa", aboutIcon);

        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuAbout);
        this.setJMenuBar(menuBar);
        this.setJMenuBar(menuBar);
        // New File
        newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)); // Set a keyboard shortcut
        menuFile.add(newFile); // Adding the file menu
        // Open File
        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        menuFile.add(openFile);
        // Save File
        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(saveFile);

        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        menuFile.add(close);
        // Clear File (Code)
        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        menuEdit.add(clearFile);
        // Cut Text
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Recortar");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Recortar");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuEdit.add(cut);
        // Copy Text
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copiar");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copiar");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuEdit.add(copy);

        // Paste Text
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Colar");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Colar");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        menuEdit.add(paste);

        // About Me
        aboutMe.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuAbout.add(aboutMe);

        // About Software
        aboutSoftware.addActionListener(this);
        aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuAbout.add(aboutSoftware);

        mainToolbar = new JToolBar();
        this.add(mainToolbar, BorderLayout.NORTH);

        newButton = new JButton(newIcon);
        newButton.setToolTipText("Novo");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();

        openButton = new JButton(openIcon);
        openButton.setToolTipText("Abrir");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();

        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Salvar");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();

        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Limpar tudo");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();
        
        
        //JLabel timeLabel = new JLabel(timeIcon);
        //timeLabel.setToolTipText("Tempo de simulação");
        //mainToolbar.add(timeLabel);
        mainToolbar.add(new JLabel("Tempo"));
        mainToolbar.addSeparator();
        
        timeSimulationtxt = new JTextField(6);
        timeSimulationtxt.setText("0.0");
        timeSimulationtxt.setMaximumSize(timeSimulationtxt.getPreferredSize());
        timeSimulationtxt.setToolTipText("Tempo de simulação");
        
        timeSimulationtxt.getDocument().addDocumentListener(new DocumentListener()
        {
            private void updateTime()
            {
                canvas_panel.setChanged(true);
            }
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateTime();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateTime();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateTime();
            }
        });
        
        mainToolbar.add(timeSimulationtxt);
        mainToolbar.addSeparator();
        
        
        
        mainToolbar.add(new JLabel("Passo"));
        mainToolbar.addSeparator();
        
        stepSizetxt = new JTextField(6);
        stepSizetxt.setText("1E-5");
        stepSizetxt.setMaximumSize(stepSizetxt.getPreferredSize());
        stepSizetxt.setToolTipText("Passo de Integraçao");
        
        stepSizetxt.getDocument().addDocumentListener(new DocumentListener()
        {
            private void updateSize()
            {
                canvas_panel.setChanged(true);                
            }
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateSize();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateSize();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateSize();
            }
        });
        
        mainToolbar.add(stepSizetxt);
        mainToolbar.addSeparator();
        
        runButton = new JButton(goIcon);
        runButton.setToolTipText("Simular");
        runButton.addActionListener(this);
        mainToolbar.add(runButton);
        mainToolbar.addSeparator();
        
        pointerButton = new JButton(pointerIcon);
        pointerButton.setToolTipText("Ponteiro");
        pointerButton.addActionListener(this);
        mainToolbar.add(pointerButton);
        mainToolbar.addSeparator();
        

        aboutMeButton = new JButton(aboutMeIcon);
        aboutMeButton.setToolTipText("Sobre mim");
        aboutMeButton.addActionListener(this);
        mainToolbar.add(aboutMeButton);
        mainToolbar.addSeparator();

        aboutButton = new JButton(aboutIcon);
        aboutButton.setToolTipText("Sobre o SimAc");
        aboutButton.addActionListener(this);
        mainToolbar.add(aboutButton);
        mainToolbar.addSeparator();
    }
    
    /**
     * 
     */
    private void initComponents()
    {
        //String plafClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        //UIManager.setLookAndFeel(plafClassName);
        setDefaultLookAndFeelDecorated(true);
        container = getContentPane();
        // Seta o tamanho inicial da janela principal
        setBounds(0, 0, 1024, 708);
        // Seta titulo da janela
        setTitle(TrapezoidCircuitSimulator.NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Centraliza janela na tela
        setLocationRelativeTo(null);
        container.setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        
        initMenu();
        
        
    }

    public String get_time_simulation()
    {
        return timeSimulationtxt.getText();
    }
    
    public String get_step_simulation()
    {
        return stepSizetxt.getText();
    }
    
    private void addComponentsEletrics(JPanel panel, int i)
    {
        if(i==1)
        {
            addComponentsOnSideBar(panel, new JButton(nodeNameIcon) ,"Output","Rótulo nó");
            addComponentsOnSideBar(panel, new JButton(capacitorIcon) , "Capacitor","Capacitor");
            addComponentsOnSideBar(panel, new JButton(inductorIcon) , "Inductor","Indutor");
            addComponentsOnSideBar(panel, new JButton(ResistorIcon) ,"Resistor", "Resistor");
            addComponentsOnSideBar(panel, new JButton(sourceCurrentIcon) ,"CurrentSource","Fonte de Corrente");
            addComponentsOnSideBar(panel, new JButton(sourceVoltageACIcon) ,"ACVoltageSource","Fonte de Tensão Alternada");
            addComponentsOnSideBar(panel, new JButton(sourceVoltageIcon) ,"DCVoltageSource","Fonte de Tensão");
            addComponentsOnSideBar(panel, new JButton(switchIcon) ,"Switch","Chave");
            addComponentsOnSideBar(panel, new JButton(wireIcon) ,"Wire","Conexão");
            //addComponentsOnSideBar(panel, new JButton(voltmeterIcon) ,"Voltmeter","Voltimetro");
            //addComponentsOnSideBar(panel, new JButton(ammeterIcon) ,"Ammeter","Amperimentro");
            addComponentsOnSideBar(panel, new JButton(groundIcon) ,"Ground","Terra");
        }
        else
        {
            addComponentsOnSideBar(panel, new JButton(transientIcon) ,"tran","Simulação Transiente");
            addComponentsOnSideBar(panel, new JButton(dcIcon) ,"dc","Simulação DC");
        }
        
    }
    /**
    * Inicializa e configura o menu esquerdo de componentes e simulação
    */
    private void initSideBar()
    {
        principalPanel = new JPanel(new BorderLayout());

        sideBar = new SideBar(SideBar.SideBarMode.MINIMISE_CONTENT);
        
        JPanel panelComponents = new JPanel();
        panelComponents.setBackground(Color.WHITE);
        panelComponents.setLayout(new GridLayout(4,3));        
        addComponentsEletrics(panelComponents,1);            
        SidebarSectionModel m1 = new SidebarSectionModel("Componentes", panelComponents, "");
        SideBarSection ss1 = new SideBarSection(sideBar, m1);
        sideBar.addSection(ss1);
        
        JPanel panelSimulation = new JPanel();
        panelSimulation.setBackground(Color.WHITE);
        panelSimulation.setLayout(new GridLayout(1,2));
        addComponentsEletrics(panelSimulation,2);
        SidebarSectionModel m2 = new SidebarSectionModel("Simulação", panelSimulation, "");
        SideBarSection ss2 = new SideBarSection(sideBar, m2);
        sideBar.addSection(ss2);
        sideBar.setBackground(Color.DARK_GRAY);
        principalPanel.add(sideBar, BorderLayout.WEST);
        
        canvas_panel=new PanelCircuitArea(this);
        principalPanel.add(canvas_panel,BorderLayout.CENTER);
        principalPanel.setBackground(Color.red);
        tabbedPane.add("Menu de Ferramentas", principalPanel);
        container.add(tabbedPane);        
    }
    /**
     * Adiciona um botão no menu SideBar
     * @param panel
     * @param component
     * @param toolTip 
     */
    private void addComponentsOnSideBar(JPanel panel, JButton btn, String name,String toolTip)
    {
        btn.setToolTipText(toolTip);
        btn.setName(name);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.addActionListener(this);
        panel.add(btn);
    }
    /**
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e)
    {
        // Se botão de fechar
        if (e.getSource() == close)
        {
            this.dispose(); // dispose all resources and close the application
        }
        // Se botão de novo circuito
        else if (e.getSource() == newFile || e.getSource() == newButton)
        {
            
        }
         // Se botão de ponteiro
        else if (e.getSource() == pointerButton)
        {
            canvas_panel.setMousePointer();
        }
        // Se botão de rodar simulação
        else if (e.getSource() == runButton)
        {
         try
            {
                
                if (Double.parseDouble(timeSimulationtxt.getText())>0)
                {
                    if(Double.parseDouble(stepSizetxt.getText())>0)
                    {
                        if(Double.parseDouble(timeSimulationtxt.getText())>Double.parseDouble(stepSizetxt.getText()))
                        {
                            manage_plots =new ManagePlot();
                            String path_circuit=canvas_panel.analysis_circuit(get_time_simulation(), get_step_simulation(),manage_plots);
                            if(path_circuit!=null)
                            {
                                InterfaceJNI jni=new InterfaceJNI();
                                String path_out = jni.run_analysis(path_circuit);
                                manage_plots.open_chart(path_out);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this,
                            "Pro favor digite um tempo de simulaçao menor que o passo de integraçao", "Erro de passo de simulação",
                            JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this,
                            "Pro favor digite um passo de integraçao válido", "Erro de passo de simulação",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this,
                        "Pro favor digite um tempo válido", "Erro de tempo de simulação",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(this, "Verifique se o valor no tempo e passo de simulação sao válidos","Análise do circuito",JOptionPane.ERROR_MESSAGE);
            }
        }
        // If the source was the "open" option
        else if (e.getSource() == openFile || e.getSource() == openButton)
        {
            JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to  browse files to open)
            int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
            /*
             * NOTE: because we are OPENing a file, we call showOpenDialog~ if
             * the user clicked OK, we have "APPROVE_OPTION" so we want to open
             * the file
             */
            if (option == JFileChooser.APPROVE_OPTION)
            {
                
            }
        }
        // If the source of the event was the "save" option
        else if (e.getSource() == saveFile || e.getSource() == saveButton)
        {
            if(canvas_panel.getChanged() || !(canvas_panel.get_circuit().get_path_circuit_name().equals("")))
            {
                manage_plots =new ManagePlot();
                canvas_panel.create_circuit_description(manage_plots);
            }
            else
            {
                if(!canvas_panel.save_circuit())
                {
                    JOptionPane.showMessageDialog(this,"Para simulação a rede deve ser salva.");
                }
            }
        }
        // Limpar canvas
        else if (e.getSource() == clearFile || e.getSource() == clearButton)
        {
            
        }
        // Sobre mim
        else if (e.getSource() == aboutMe || e.getSource() == aboutMeButton)
        {
            new About(this).me();
        }
        // Sobre o programa
        else if (e.getSource() == aboutSoftware || e.getSource() == aboutButton)
        {
            new About(this).software();
        }
        else if((((JButton)e.getSource()).getName().equals("Output")) || ((JButton)e.getSource()).getName().equals("Wire") ||
                ((JButton)e.getSource()).getName().equals("Capacitor")|| ((JButton)e.getSource()).getName().equals("Inductor") ||
                ((JButton)e.getSource()).getName().equals("Resistor") || ((JButton)e.getSource()).getName().equals("Ground") ||
                ((JButton)e.getSource()).getName().equals("CurrentSource") || ((JButton)e.getSource()).getName().equals("DCVoltageSource") ||
                ((JButton)e.getSource()).getName().equals("ACVoltageSource")||((JButton)e.getSource()).getName().equals("Switch") ||
                ((JButton)e.getSource()).getName().equals("Ammeter")||((JButton)e.getSource()).getName().equals("Voltmeter"))
        {
            
            String el = ((JButton)e.getSource()).getName();
            canvas_panel.choice_circuit_element(el);
        }
    }
}
