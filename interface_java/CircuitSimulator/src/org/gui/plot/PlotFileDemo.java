/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gui.plot;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author paulo
 */
public class PlotFileDemo extends JFrame
{
    public PlotFileDemo()
    {
        super("Dados de Arquivo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Arquivo");
        JMenu helpMenu = new JMenu("Ajuda");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        final JMenuItem openAction = new JMenuItem("Abrir", new ImageIcon("images/Open-icon.png"));
        final JMenuItem saveAction = new JMenuItem("Salvar", new ImageIcon("images/save-file.png"));
        final JMenuItem exitAction = new JMenuItem("Sair", new ImageIcon("images/exit-icon.png"));
        final JMenuItem aboutAction = new JMenuItem("Sobre", new ImageIcon("images/about-us.png"));

        final JTextArea textArea = new JTextArea(8, 16);
        textArea.setFont(new Font("Serif", Font.BOLD, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Texto"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        ChartPanel graphicsArea = new ChartPanel(null);
        graphicsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Grafico"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, graphicsArea);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.LINE_END);

        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(openAction);
        openAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getSource().equals(openAction)) {
                    File file = new File("/home/paulo/Documentos/UEFS/TCC/Código/Cplusplus/circuit.dat");
                    graphicsArea.setChart(creatChart(file));
                    /*JFileChooser fileChooser = new JFileChooser(new File("."));
                    if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        System.out.println(file.getAbsolutePath());
                        graphicsArea.setChart(creatChart(file));
                    }*/
                }
            }

            private JFreeChart creatChart(File file) {
                String title = null;
                String xAxisLabel = null;
                String yAxisLabel = null;
                BufferedReader in = null;
                int start = 0;
                int interval = 0;
                String data = null;
                String line = null;
                XYSeries dataset;
                int count=0;
                try {
                    in = new BufferedReader(new FileReader(file));
                    line = in.readLine();
                    dataset = new XYSeries(line.split(" ")[1]);
                    while ((line = in.readLine()) != null)
                    {
                        String[] data_line;
                        data_line = line.split(" ");
                        //System.out.println(count+" "+data_line[1]);                      
                        dataset.add(Double.valueOf(data_line[0]).doubleValue(), Double.valueOf(data_line[1]).doubleValue());
                        count++;
                        textArea.append(line + "\n");                        
                    }
                    return ChartFactory.createXYLineChart(title,xAxisLabel, yAxisLabel, new XYSeriesCollection(dataset),PlotOrientation.VERTICAL,true,true,false);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
                return null;
            }
        });
        fileMenu.add(saveAction);
        fileMenu.add(exitAction);
        exitAction.setMnemonic(KeyEvent.VK_X);
        exitAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getSource().equals(exitAction)) {
                    System.exit(0);
                }
            }
        });
        fileMenu.addSeparator();
        helpMenu.addSeparator();
        helpMenu.add(aboutAction);
        aboutAction.setMnemonic(KeyEvent.VK_A);
        aboutAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null,
                    "Visualization tool.",
                    "About Us", JOptionPane.PLAIN_MESSAGE);
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new PlotFileDemo();
        });
    }
}
