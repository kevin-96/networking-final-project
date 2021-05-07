package client.gui;

// import client.logic.TempPlayer;
import client.logic.Settings;
import client.network.Connection;
import common.messages.Message;
import common.messages.SendAllPlayersMessage;
import common.messages.WinMessage;
import common.datatypes.Guess;
import common.datatypes.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow {
    private static final int REFRESH_RATE = 1000;
    private JPanel root;
    private JLabel connectionInfoText;
    private JButton connectionSettingsButton;
    private JTable allPlayerHitCount;
    private JTable currentPlayerGuesses;
    private JToolBar toolbar;
    private JPanel playerControls;

    private JFrame parent;
    private int codeLength;
    private String playerName;

    private Settings settings;
    private Connection connection;

    public Settings getSettings() {
        try {
            return settings.clone();
        } catch (NullPointerException | CloneNotSupportedException e) {
            System.err.println("Error in getSettings: " + e.getLocalizedMessage());
            return Settings.getDefaultSettings();
        }
    }

    public void processSettings(Settings settings) {
        if (settings == null) {
            return;
        }
        this.settings = settings;
        this.playerName = settings.playerName;
        this.codeLength = settings.codeLength;

        // Update GUI
        try {
            // Set connection status label
            String spectatorLabel = settings.isSpectator ? " as spectator" : "";
            this.connectionInfoText.setText(settings.isConnected
                    ? String.format("Connected to %s:%d", settings.serverAddress, settings.port) + spectatorLabel
                    : "Disconnected");

            // If player controls (number inputs and submit button) haven't been initialized yet, add them in
            if (playerControls.getComponents().length == 0) {
                // Number input fields (one for each digit in the code)
                for (int i = 0; i < codeLength; i++) {
                    SpinnerModel model = new SpinnerNumberModel(0, 0, settings.maxDigit, 1);
                    JSpinner numberInput = new JSpinner(model);
                    this.playerControls.add(numberInput);
                }

                // Submit button
                JButton submit = new JButton();
                submit.setText("Submit Guess");
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the guess based on the input fields
                        int[] guess = new int[codeLength];
                        for (int i = 0; i < codeLength; i++) {
                            JSpinner input = (JSpinner) MainWindow.this.playerControls.getComponents()[i];
                            guess[i] = (Integer) input.getValue();
                        }

                        if (Guess.digitsAreDistinct(guess)) {
                            System.out.println("Guessed " + Guess.convertToString(guess));
                            MainWindow.this.connection.guess(guess);
                        } else {
                            SimpleDialog.showErrorDialog("Guess digits must be distinct");
                        }

                    }
                });
                this.playerControls.add(submit);

                // Pack it up!
                parent.pack();
            }

            // Disable controls if disconnected or spectating
            for (Component control : playerControls.getComponents()) {
                control.setEnabled(settings.isConnected && !settings.isSpectator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processState(List<Player> players) {
        // If the player is part of the game, get them from the player list
        Player currentPlayer = null;
        for (Player p : players) {
            if (p.getPlayerName().equals(this.playerName)) {
                currentPlayer = p;
                break;
            }
        }

        // Only update guesses if the player is not a spectator (in which case they aren't in the players list)
        if (currentPlayer != null) {
            // Build table data set
            DefaultTableModel guessModel = new DefaultTableModel(0, 3);
            guessModel.addRow(new Object[] { "Guess", "Hits", "Blows" }); // Header
            for (Guess guess : currentPlayer.getAllGuesses()) {
                guessModel.addRow(new Object[] { Guess.convertToString(guess.getDigits()), guess.getHitCount(), guess.getBlowCount()});
            }

            // Apply new data set to table
            this.currentPlayerGuesses.setModel(guessModel);
            this.currentPlayerGuesses.revalidate();
            this.currentPlayerGuesses.repaint();
        }

        // Update the player list
        DefaultTableModel playersModel = new DefaultTableModel(0, 3);
        playersModel.addRow(new Object[] { "Player", "Hits", "Blows" }); // Header
        for (Player player : players) {
            playersModel.addRow(new Object[] { player.getPlayerName(), player.getHitCount(), player.getBlowCount() });
        }

        // Apply new data set to table
        this.allPlayerHitCount.setModel(playersModel);

        // Repaint app
        parent.revalidate();
        parent.repaint();
        root.revalidate();
        root.repaint();
    }

    public void display() {
        // Make sure we have a frame to hold the gui
        if (parent == null || !parent.isDisplayable()) {
            parent = new JFrame("Mastermind Royale");
            parent.setContentPane(root);
            parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parent.pack();
        }
        parent.setVisible(true);
    }

    public MainWindow() {
        // Trigger connection settings popup when button is pressed
        connectionSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnectionDialog(MainWindow.this).display();
            }
        });

        // Refresh game state every {REFRESH_RATE} milliseconds
        Timer timer = new Timer(REFRESH_RATE, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.refreshState();
            }
        });

        this.playerControls.setLayout(new FlowLayout());

        // Start polling for state updates
        timer.start();
    }

    // Make a connection. Throws an exception if this process fails.
    public void setConnection(String server, int port, String name, boolean isSpectator) throws Exception {
        this.connection = new Connection(server, port, name, isSpectator);
    }

    // Ask server for a new state and process the response
    private void refreshState() {
        if (this.connection != null) {
            Message stateMessage = this.connection.getState();

            if (stateMessage instanceof SendAllPlayersMessage) {
                // We're getting a player list
                processState(((SendAllPlayersMessage) stateMessage).getAllPlayers());
            } else if (stateMessage instanceof WinMessage) {
                // Someone won, so we received a win message instead
                WinMessage winner = (WinMessage) stateMessage;
                System.out.println(winner.getName() + " Has Won");
                String winText = winner.getName() + " broke the code! Code: " + Guess.convertToString(winner.getCode());
                SimpleDialog.showInfoDialog(winText, "Game Over!");

                processState(((WinMessage) stateMessage).getAllPlayers());
            }
        }
    }

}
