package com.shinkson47.ScalaGame.Client;

import javax.swing.*;

/**
 * Java mid end for the client window form.
 * Only acts as an intermediate between the client backend and the frontend form.
 */
public class ClientWindow {

    /*
     * publicly available references to objects on the form.
     */
    /**
     * Text box used to render the game field.
     */
    public JTextArea txtTerminal;

    /**
     * Main content parent pane. This pane is for placing on a displayable window.
     */
    public JPanel clientPanelMain;

    /**
     * label to display the output of manual junit tests at runtime.
     */
    public JLabel txtJUnit;

    /**
     * Create a new clientwindow instance.
     */
    public ClientWindow() {
        clientPanelMain.addKeyListener(GameClient.keyspec());                                                           //Using the game client's key specification, add a key listener.
        txtTerminal.addKeyListener(GameClient.keyspec());                                                               //For good measure, the listener is added to both focusable objects on the form.
    }

}
