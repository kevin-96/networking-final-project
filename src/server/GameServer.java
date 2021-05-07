/*
GameServer
Author:James Jacobson
GameServer hosts a game of Hit and Blow. Send messages between itself and the connection to the player
 */

package server;

import common.datatypes.Guess;
import common.datatypes.Player;
import common.messages.*;
import server.logic.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**************
 * GameServer class Authors: Phillip Nam, James Jacobson, Ryan Clark Spring 2021
 *
 * This class is the server for the Mastermind game. It handles the networking
 * of the game and is essentially responsible for updating the game state.
 *************/

public class GameServer {

    private GameState state;
    private List<ClientHandler> clients; // A list of each thread. Contains the Object Output and Input stream of the socket
    private int[] code; // Code everyone is trying to guess

    public GameServer() {
        state = new GameState();
        code = this.createCode(5, false); // Hard-code setting that all digits must be distinct
        clients = new Vector<>();
        ServerSocket server = null;

        // Input the port you wish to run the game on
        System.out.println("Enter port number");
        Scanner scan = new Scanner(System.in);
        int port = scan.nextInt();
        System.out.println("Server is active on port " + port);
        scan.close();

        try {
            //Creates the server
            server = new ServerSocket(port);
            server.setReuseAddress(true);

            // running infinite loop for getting new client request
            while (true) {
                //Create socket that accepts new client connections
                Socket client = server.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // create a new thread that will handle the client separately
                ClientHandler clientSock = new ClientHandler(client);
                clients.add(clientSock);
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processClientGuessMessage(GuessMessage msg) {
        // Find the sender's player object and update its hit and blow count
        try {
            for (Player player : state.getAllPlayers()) {
                if (player.getPlayerName().equals(msg.getName())) {
                    int[] hab = guess(msg.getGuess());
                    System.out.println("H" + hab[0] + "B" + hab[1]);
                    synchronized (state) {
                        Guess guess = new Guess(hab[0], hab[1], msg.getGuess());
                        player.addGuess(guess);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean playerExists(String name) {
        if (name == null) {
            return false;
        }
        for (Player player : state.getAllPlayers()) {
            if (player.getPlayerName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    //Send all of the players to a newly joined client
    private Message processClientJoinMessage() {
        return new JoinGameMessage(state.getAllPlayers(), state.didGameStart());
    }


    //Sends out a list of players to every player
    //If someone has 4 hit(win condition) every players guess is wiped out, and a new code is generated
    private Message processRequestAllPlayers() { // bool
        try {
            List<Player> players = state.getAllPlayers();
            for (Player player : players) {
                if (player.getAllGuesses().size() > 0 && player.getHitCount() == 4) {
                    for (Player losers : players) {
                        losers.reset();
                    }
                    int maxDigit = 5;
                    boolean allowDuplicates = false;
                    int[] oldCode = this.code;
                    this.code = this.createCode(maxDigit, false);
                    // Send everyone a win message
                    for (ClientHandler client : clients) {
                        client.getOutput().writeObject(new WinMessage(player.getPlayerName(), players, oldCode));
                    }
                    return null;
                }
            }
            return new SendAllPlayersMessage(state.getAllPlayers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Logic to determine hit and blow count
    private int[] guess(int[] currentGuess) {
        int hitCount = 0;
        int blowCount = 0;
        for (int i = 0; i < currentGuess.length; i++) {
            for (int j = 0; j < currentGuess.length; j++) {
                if (currentGuess[i] == code[j] && i == j) {
                    hitCount++;
                } else if (currentGuess[i] == code[j] && i != j) {
                    blowCount++;
                }
            }
        }
        return new int[]{hitCount, blowCount};
    }

    //Creates an array of unique ints
    private static boolean areDistinct(int[] arr) {
        Set<Integer> s = new HashSet<Integer>();
        for (int i : arr) {
            s.add(i);
        }
        return (s.size() == arr.length);
    }

    //Randomizes an int[]'s values
    private static int[] randomizeCode(int maxDigit) {
        int[] code = new int[4];
        for (int i = 0; i < code.length; i++) {
            code[i] = (int) (Math.random() * (maxDigit + 1));
        }
        return code;

    }

    //Creates a new code based on the highest digit and if duplicates are allowed
    private int[] createCode(int maxDigit, boolean dups) {
        int[] code = GameServer.randomizeCode(maxDigit);
        while (!areDistinct(code) && !dups) {
            code = randomizeCode(maxDigit);
        }
        return code;
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
    }

    //A thread that handles a client connection
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private ObjectOutputStream output;
        private String playerName;

        public ObjectOutputStream getOutput() {
            return output;
        }

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(this.clientSocket.getInputStream());
                //runs indefinitely
                while (true) {
                    //Retrieves a message from the client. Determines what the message means and the next steps to take
                    //WriteObject sends a message to the client side
                    Message msg = (Message) input.readObject();
                    if (msg instanceof JoinMessage) {
                        JoinMessage jm = (JoinMessage) msg;
                        boolean _isPlaying = jm.getIsPlaying();
                        boolean _playerExists = _isPlaying && playerExists(jm.getName());
                        //Does appropriate action if a client is a spectator or not
                        if (_isPlaying && !_playerExists) {
                            state.addPlayer(jm.getName());
                            this.playerName = jm.getName();
                        } else {
                            this.playerName = null;
                        }
                        Message toSend = _playerExists ? new ErrorMessage("Player already exists") : processClientJoinMessage();
                        this.output.writeObject(toSend);
                        this.output.flush();
                    } else if (msg instanceof GuessMessage) {
                        processClientGuessMessage((GuessMessage) msg);
                        this.output.reset();
                    } else if (msg instanceof RequestAllPlayersMessage) {
                        RequestAllPlayersMessage req = (RequestAllPlayersMessage) msg;
                        Message all = processRequestAllPlayers();
                        if (all != null) {
                            this.output.writeObject(all);
                            this.output.reset();
                        }
                    }
                    // This hangs here until a message is sent
                }
            } catch (SocketException e) {
                //If a player leaves, remove them from the player list
                System.out.println(this.playerName + " has left the game");
                state.removePlayer(this.playerName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}