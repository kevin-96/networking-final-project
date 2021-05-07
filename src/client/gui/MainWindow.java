package client.gui;

import client.logic.TempGuess;
// import client.logic.TempPlayer;
import client.logic.TempSettings;
import client.logic.TempState;
import client.network.Connection;
import common.Message;
import common.SendAllPlayersMessage;
import common.WinMessage;
import server.Guess;
import server.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
    private int maxDigit;
    private String playerName;

    private TempSettings settings;
    // private TempState state;
    private Connection connection;
    private List<Player> players;// State

    private boolean isDisplayingWinDialog;
    private Timer timer;

    public TempSettings getSettings() {
        try {
            return settings.clone();
        } catch (NullPointerException | CloneNotSupportedException e) {
            System.err.println("Error in getSettings: " + e.getLocalizedMessage());
            return TempSettings.getDefaultSettings();
        }
    }

    public void processSettings(TempSettings settings) {
        if (settings == null) {
            return;
        }
        this.settings = settings;
        this.playerName = settings.playerName;
        this.codeLength = settings.codeLength;
        this.maxDigit = settings.maxDigit;

        // Update GUI
        try {
            // Set connection status label
            String spectatorLabel = settings.isSpectator ? " as spectator" : "";
            this.connectionInfoText.setText(settings.isConnected
                    ? String.format("Connected to %s:%d", settings.serverAddress, settings.port) + spectatorLabel
                    : "Disconnected");

            // If inputs haven't been initialized yet, add them in
            if (playerControls.getComponents().length == 0) {
                // Number input fields
                for (int i = 0; i < codeLength; i++) {
                    SpinnerModel model = new SpinnerNumberModel(0, 0, maxDigit, 1);
                    JSpinner numberInput = new JSpinner(model);
                    if (this.playerControls != null) {
                        this.playerControls.add(numberInput);
                    }
                }

                // Submit button
                JButton submit = new JButton();
                submit.setText("Submit Guess");
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Combine the numbers the user entered into a string
                        StringBuilder guessDigits = new StringBuilder();
                        for (int i = 0; i < codeLength; i++) {
                            JSpinner input = (JSpinner) MainWindow.this.playerControls.getComponents()[i];
                            guessDigits.append(input.getValue());
                        }
                        String[] guessToString = guessDigits.toString().split("");
                        int[] guess = new int[4];
                        for (int i = 0; i < guess.length; i++) {
                            guess[i] = Integer.valueOf(guessToString[i]);
                        }
                        MainWindow.this.connection.guess(guess);
                    }
                });
                this.playerControls.add(submit);

                // Pack it up!
                parent.pack();
            }

            // Disable controls if disconnected
            for (Component control : playerControls.getComponents()) {
                control.setEnabled(settings.isConnected && !settings.isSpectator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processState(List<Player> players) {

        this.players = players;

        Player currentPlayer = null;
        for (Player p : players) {
            if (p.getPlayerName().equals(this.playerName)) {
                currentPlayer = p;
                break;
            }
        }

        // Only update guesses if the player is not a spectator (in which case they aren't in the players list)
        if (currentPlayer != null) {
            DefaultTableModel guessModel = new DefaultTableModel(0, 3);

            guessModel.addRow(new Object[] { "Guess", "Hits", "Blows" });

            for (Guess guess : currentPlayer.getAllGuesses()) {
                guessModel.addRow(new Object[] { Guess.convert(guess.digits), String.valueOf(guess.hitCount),
                        String.valueOf(guess.blowCount) });
            }

            this.currentPlayerGuesses.setModel(guessModel);
            this.currentPlayerGuesses.revalidate();
            this.currentPlayerGuesses.repaint();
        }

        // Player rows
        DefaultTableModel playersModel = new DefaultTableModel(0, 3);

        playersModel.addRow(new Object[] { "Player", "Hits", "Blows" });

        for (Player player : players) {
            playersModel.addRow(new Object[] { player.getPlayerName(), String.valueOf(player.getHitCount()),
                    String.valueOf(player.getBlowCount()) });
        }

        this.allPlayerHitCount.setModel(playersModel);


        parent.revalidate();
        parent.repaint();

        root.revalidate();
        root.repaint();


    }

    public void display() {
        if (parent == null || !parent.isDisplayable()) {
            parent = new JFrame("Mastermind Royale");
            parent.setContentPane(root);
            parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parent.pack();
        }
        parent.setVisible(true);
    }

    public MainWindow() {

        connectionSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnectionDialog(MainWindow.this).display();
            }
        });

        // Refresh game state every {REFRESH_RATE} milliseconds
        timer = new Timer(REFRESH_RATE, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MainWindow.this.updateState();
            }
        });

        this.playerControls.setLayout(new FlowLayout());

        String[] guessColumns = new String[] { "Guess", "Hits", "Blows" };
        DefaultTableModel guessModel = new DefaultTableModel(0, 3);
        guessModel.addRow(guessColumns);
        this.currentPlayerGuesses.setModel(guessModel);

        String[] playerColumns = new String[] { "Player", "Hits", "Blows" };
        DefaultTableModel playersModel = new DefaultTableModel(0, 3);
        playersModel.addRow(playerColumns);
        allPlayerHitCount.setModel(playersModel);
        // isDisplayingWinDialog = false;
        timer.start();
    }

    public void setConnection(String server, int port, String name, boolean isSpectator) throws Exception {
        this.connection = new Connection(server, port, name, isSpectator);
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void updateState() {
        if (this.connection != null) {
            Message state = this.connection.getState();

            if (state instanceof SendAllPlayersMessage) {
                processState(((SendAllPlayersMessage) state).getAllPlayers());
            } else if (state instanceof WinMessage) {

                // Clear out guesses by sending an empty state


                // isDisplayingWinDialog = true;
                
                WinMessage winner = (WinMessage) state;
                System.out.println(winner.getName() + " Has Won");
                String winText = winner.getName() + " broke the code! Code: " + intArrayToString(winner.getCode());
                JOptionPane.showMessageDialog(null, winText, "Game Over!", JOptionPane.PLAIN_MESSAGE);

                processState(((WinMessage) state).getAllPlayers());
            }

            // processState(this.players);
        }
    }

    public String intArrayToString(int arr[]) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += Integer.toString(arr[i]);
        }
        return str;
    }

}
