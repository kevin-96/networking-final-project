package client.gui;

import client.logic.Settings;

import javax.swing.*;
import java.awt.event.*;

public class ConnectionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField serverAddressField;
    private JTextField portField;
    private JCheckBox spectatorToggle;
    private JTextField nameField;

    private MainWindow window;

    public ConnectionDialog(MainWindow window) {
        // Basic setup
        this.window = window;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Connection Settings");

        // Setup button click behavior
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // Call onCancel() when close button is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Call onCancel() when escape key is pressed
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // For the "port" field, only let the user type in numbers, and set a max length of 5
        portField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                char c = e.getKeyChar();
                if ((portField.getText().length() < 5 && Character.isDigit(c))
                        || key == KeyEvent.VK_BACK_SPACE
                        || key == KeyEvent.VK_LEFT
                        || key == KeyEvent.VK_RIGHT) {
                    // Setting editable to true allows the user to type in the field
                    portField.setEditable(true);
                } else {
                    // Setting editable to false prevents the character from being inserted into the field
                    portField.setEditable(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                portField.setEditable(true);
            }
        });

        // Fill in settings with current settings
        Settings settings = window.getSettings();
        serverAddressField.setText(settings.serverAddress);
        portField.setText(String.valueOf(settings.port));
        nameField.setText(settings.playerName);
        spectatorToggle.setSelected(settings.isSpectator);
    }

    private void onOK() {
        // Get settings from fields
        String server = serverAddressField.getText();
        int port = Integer.parseInt(portField.getText());
        boolean spectator = spectatorToggle.isSelected();
        String playerName= nameField.getText();

        // Try to connect to server
        boolean isConnected;
        try {
            window.setConnection(server, port, playerName, spectator);
            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
            SimpleDialog.showErrorDialog("Connection failed: " + e.getLocalizedMessage());
        }

        // Update settings for the client
        Settings settings = window.getSettings();
        settings.serverAddress = server;
        settings.port = port;
        settings.isSpectator = spectator;
        settings.isConnected = isConnected;
        settings.playerName = playerName;
        window.processSettings(settings);

        // If connection succeeded, close the dialog
        if (isConnected) {
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    public void display() {
        this.pack();
        this.setVisible(true);
    }

}
