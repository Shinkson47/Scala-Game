package com.shinkson47.ScalaGame.Client;

import javax.swing.*;

/**
 * Java form for the client window. Defines content, lay out, and listeners.
 */
public class ClientWindow {
    public JTextArea txtTerminal;
    public JPanel clientPanelMain;
    public JLabel txtJUnit;

    public ClientWindow() {
        clientPanelMain.addKeyListener(scalaGame.keyspec());
        txtTerminal.addKeyListener(scalaGame.keyspec());
    }

}
