package client.gui;

import client.logic.TempSettings;

import javax.swing.*;
import java.awt.event.*;

public class ConnectionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField serverAddressField;
    private JTextField portField;
    private JCheckBox spectatorToggle;

    private MainWindow window;

    public ConnectionDialog(MainWindow window) {
        this.window = window;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Connection Settings");

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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


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
        TempSettings settings = window.getSettings();
        serverAddressField.setText(settings.serverAddress);
        portField.setText(String.valueOf(settings.port));
        spectatorToggle.setSelected(settings.isSpectator);
    }

    private void onOK() {
        String server = serverAddressField.getText();
        int port = Integer.parseInt(portField.getText());
        boolean spectator = spectatorToggle.isSelected();

        // TODO try to connect
        boolean isConnected = true;

        TempSettings settings = window.getSettings();
        settings.serverAddress = server;
        settings.port = port;
        settings.isSpectator = spectator;
        settings.isConnected = isConnected;
        window.processSettings(settings);

        if (isConnected) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Connection failed", "Error", JOptionPane.ERROR_MESSAGE);
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
