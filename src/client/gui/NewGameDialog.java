package client.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner codeLength;
    private JSpinner maxDigit;

    public NewGameDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Game Settings");

        codeLength.setModel(new SpinnerNumberModel(4, 4, 8, 1));
        maxDigit.setModel(new SpinnerNumberModel(3, 3, 9, 1));

        ChangeListener enforceMinimums = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Make sure maxValue is high enough for each digit to be unique
                int maxDigitValue = (Integer) maxDigit.getValue();
                int codeLengthValue = (Integer) codeLength.getValue();
                if (maxDigitValue < (codeLengthValue - 1)) {
                    maxDigit.setValue(codeLengthValue - 1);
                }
            }
        };

        codeLength.addChangeListener(enforceMinimums);
        maxDigit.addChangeListener(enforceMinimums);

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
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        NewGameDialog d = new NewGameDialog();
        d.pack();
        d.setVisible(true);
    }
}
