/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 */

package org.gui;

import javax.swing.*;
import java.awt.FlowLayout;

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JLabel text;

    public About(MainWindow ui) {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(500,300);
        frame.setLocationRelativeTo(ui);
        text = new JLabel();
    }

    public void me() {
        frame.setTitle("Sobre mim - " + TrapezoidCircuitSimulator.NAME);

        contentText =
        "<html><body><p>" +
        "Author: Paulo Cezar dos Santos Filho<br />" +
        "Contato: " +
        "<a href='mailto:" + TrapezoidCircuitSimulator.AUTHOR_EMAIL + "?subject=About the NotePad PH Software'>" + TrapezoidCircuitSimulator.AUTHOR_EMAIL + "</a>" +
                "<br /><br />" +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

    public void software() {
        frame.setTitle("Sobre mim - " + TrapezoidCircuitSimulator.NAME);

        contentText =
        "<html><body><p>" +
        "Nome: " + TrapezoidCircuitSimulator.NAME + "<br />" +
        "Versão: " + TrapezoidCircuitSimulator.VERSION +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

}