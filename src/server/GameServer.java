package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import client.network.Connection;
import common.*;

/**************
 * GameServer class Authors: Phillip Nam, James Jacobson, Ryan Clark Spring 2021
 *
 * This class is the server for the Mastermind game. It handles the networking
 * of the game and is essentially responsible for updating the game state.
 *************/

public class GameServer {

    // // Server Methods

    private GameState state;
    private List<ClientHandler> clients;
    // private Map<

    private int[] code;

    public GameServer() {
        state = new GameState();
        code = new int[] { 1, 2, 3, 4 };// Hard coded for now
        clients = new Vector<ClientHandler>();
        ServerSocket server = null;
        System.out.println("Server is active");
        try {
            // GuessMessage
            //
            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // Might have to implement this here: once the socket is added, make 2 more
                // arraylists of the sockets ObjectOutputStream and ObjectInputStream
                // Since you cannot make 2 ObjectStreams of the same socket, it might be best to
                // save thoise in arraylist.

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);
                clients.add(clientSock);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
                // System.out.println("Yuh");
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
                        Guess guess=new Guess();
                        guess.hitCount=hab[0];
                        guess.blowCount=hab[1];
                        guess.digits=msg.getGuess();
                        player.addGuess(guess);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message processClientJoinMessage(JoinMessage msg) {
        try {
            // this.connections.add(msg.getConnection());
            return new JoinGameMessage(state.getAllPlayers(), state.didGameStart());
        } catch (Exception e) {
            e.printStackTrace();
            return new Message("Error");
        }

    }

    private SettingsMessage processClientCreateGameMessage(CreateGameMessage msg) { // bool
        // allowDuplicates=msg.allowDublicates // int maxDigit=msg.maxDigit
        int maxDigit = 5;
        boolean allowDuplicates = false;
        this.code = GameServer.randomizeCode(maxDigit);
        while (!areDistinct(code) && !allowDuplicates) {
            randomizeCode(maxDigit);
        }
        this.state.startGame();
        return new SettingsMessage(maxDigit, allowDuplicates);
        // TODO: Create error of duplicate

    }

    private Message processRequestAllPlayers(RequestAllPlayersMessage msg) { // bool
        try {
            List<Player> players = state.getAllPlayers();
            for (Player player : players) {
                if (player.getHitCount() == 4) {
                    for (Player losers : players) {
                        Guess guess=new Guess();
                        guess.hitCount=0;
                        guess.blowCount=0;
                        guess.digits=new int[4];
                        losers.addGuess(guess);
                    }
                    int maxDigit = 5;
                    boolean allowDuplicates = false;
                    this.code = GameServer.randomizeCode(maxDigit);
                    while (!areDistinct(code) && !allowDuplicates) {
                        randomizeCode(maxDigit);
                    }
                    return new WinMessage(player.getPlayerName(), this.code);
                }
            }
            return new SendAllPlayersMessage(state.getAllPlayers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
        return new int[] { hitCount, blowCount };
    }

    private boolean validateGuess(String guess) {
        if (guess.length() == 4) {
            for (int i = 0; i < guess.length(); i++) {
                if (!(guess.charAt(i) >= 48 && guess.charAt(i) <= 53)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean areDistinct(int[] arr) {
        // Put all array elements in a HashSet
        Set<Integer> s = new HashSet<Integer>();
        for (int i : arr) {
            s.add(i);
        }

        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    private static int[] randomizeCode(int maxDigit) {
        int[] code = new int[4];
        for (int i = 0; i < code.length; i++) {
            code[i] = (int) (Math.random() * (maxDigit + 1));
        }
        return code;

    }

    public static void main(String args[]) {
        GameServer gs = new GameServer();
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String playerName;

        public ObjectInputStream getInput() {
            return input;
        }

        public ObjectOutputStream getOutput() {
            return output;
        }

        public boolean getIsConnected() {
            return clientSocket.isConnected();
        }

        public String getPlayerName() {
            return this.playerName;
        }

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() { // A player joins the game(I think)
            // Creates inputs and outputs
            try {
                this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());// Only one of these should be
                                                                                          // made
                // // per player
                this.input = new ObjectInputStream(this.clientSocket.getInputStream());// Only one of these should be
                                                                                       // made //
                // per player

                while (true) {
                    Message msg = (Message) input.readObject();
                    if (msg instanceof JoinMessage) {
                        JoinMessage jm = (JoinMessage) msg;
                        if(jm.getIsPlaying()) {
                            state.addPlayer(jm.getName());
                        }
                        this.playerName = jm.getName();
                        Message toSend = processClientJoinMessage(jm);
                        this.output.writeObject(toSend);
                        this.output.flush();

                    } else if (msg instanceof GuessMessage) {
                        processClientGuessMessage((GuessMessage) msg);
                    } else if (msg instanceof CreateGameMessage) {
                        CreateGameMessage cm = (CreateGameMessage) msg;
                        SettingsMessage toSend = processClientCreateGameMessage(cm);
                        this.output.writeObject(toSend);
                        this.output.reset();
                        GameServer.this.processClientCreateGameMessage((CreateGameMessage) msg);
                    } else if (msg instanceof RequestAllPlayersMessage) {
                        RequestAllPlayersMessage req = (RequestAllPlayersMessage) msg;
                        Message all = processRequestAllPlayers(req);
                        this.output.writeObject(all);
                        this.output.reset();
                    }
                    // This hangs here until a message is sent
                }
            } catch (SocketException e) {
                state.removePlayer(this.playerName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Connection reset error");
                e.printStackTrace();
            }
        }

    }

}