package client.gui;

import javax.swing.*;

public class SimpleDialog {
    public static void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(String message) {
        SimpleDialog.showErrorDialog(message, "Error");
    }
}
