package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import client.network.Connection;
import common.CreateGameMessage;
import common.GuessMessage;
import common.JoinGameMessage;
import common.JoinMessage;
import common.Message;
import common.SettingsMessage;

/**************
 * GameServer class Authors: Phillip Nam, James Jacobson, Ryan Clark Spring 2021
 *
 * This class is the server for the Mastermind game. It handles the networking
 * of the game and is essentially responsible for updating the game state.
 *************/

public class GameServer {

    // // Server Methods

    private GameState state;
    // private Map<
    private ArrayList<Socket> sockets;
    private ArrayList<ObjectOutputStream> outputs;
    private ArrayList<ObjectInputStream> inputs;
    private int[] code;

    public GameServer() {
        state = new GameState();
        sockets = new ArrayList<Socket>();
        outputs = new ArrayList<ObjectOutputStream>();
        inputs = new ArrayList<ObjectInputStream>();
        code=new int[]{1,2,3,4};//Hard coded for now
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

                sockets.add(client);

                // Might have to implement this here: once the socket is added, make 2 more
                // arraylists of the sockets ObjectOutputStream and ObjectInputStream
                // Since you cannot make 2 ObjectStreams of the same socket, it might be best to
                // save thoise in arraylist.

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

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
            ArrayList<Player> players = state.getAllPlayers();
            Player currPlayer = null;
            for (Player player : players) {
                if (player.getPlayerName().equals(msg.getName())) {
                    int[] hab = guess(msg.getGuess());
                    System.out.println("H"+hab[0]+"B"+hab[1]);
                    player.setHitAndBlows(hab[0], hab[1]);
                    currPlayer = player;
                }
            }
            // Print again
            System.out.printf("Hits: %d; Blows: %d\n", currPlayer.getHitCount(), currPlayer.getBlowCount());

            for (ObjectOutputStream output : outputs) {
                output.writeObject(currPlayer);
                output.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message processClientJoinMessage(JoinMessage msg) {
        try {
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

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() { // A player joins the game(I think)
            // Creates inputs and outputs
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                output = new ObjectOutputStream(this.clientSocket.getOutputStream());// Only one of these should be made
                                                                                     // // per player
                input = new ObjectInputStream(this.clientSocket.getInputStream());// Only one of these should be made //
                                                                                  // per player
                outputs.add(output);
                inputs.add(input);
        
                while (true) {
                    Message msg = (Message) input.readObject();
                    System.out.println(msg.getMessage());
                    if (msg instanceof JoinMessage) {
                        System.out.println("JoinMessage Received");
                        JoinMessage jm = (JoinMessage) msg;
                        state.addPlayer(jm.getName());
                        Message toSend = processClientJoinMessage(jm);
                        output.writeObject(toSend);
                        output.flush();
                        System.out.println("Message Sent");
                    } else if (msg instanceof GuessMessage) {
                        System.out.println("GuessMessage Received");
                        processClientGuessMessage((GuessMessage) msg);
                    } else if (msg instanceof CreateGameMessage) {
                        System.out.println("CreateGameMessage Received");
                        CreateGameMessage cm = (CreateGameMessage) msg;
                        SettingsMessage toSend = processClientCreateGameMessage(cm);
                        output.writeObject(toSend);
                        output.reset();
                        GameServer.this.processClientCreateGameMessage((CreateGameMessage) msg);
                    }
                    // This hangs here until a message is sent
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (Exception e) {
                System.out.println("Connection reset error");
                e.printStackTrace();
            }
            // Creates an ObjectInputStream

            /*
             * finally { try { if (output != null) { //output.close();
             * System.out.println(""); } if (input != null) { //input.close();
             * //clientSocket.close(); } } catch (IOException e) { //e.printStackTrace(); //
             * }
             * 
             * }
             */
        }

        public void sendMessage(Message m)// Should send any message
        {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {

                // Creates an ObjectInputStream
                // output = new ObjectOutputStream(this.clientSocket.getOutputStream());
                // input = new ObjectInputStream(this.clientSocket.getInputStream());

                // Reads the objects

                Message msg = (Message) input.readObject();
                if (msg != null) {
                    System.out.println("Message:" + msg.getMessage());
                }
                /*
                 * This will not work the first time you try it. You get a connection reset
                 * error on the server. Client side will stop working but you can join again.
                 * You will be reset again but with no error on the server side, and you will
                 * see a message This is our only problem right now. When this is figured out, i
                 * think the rest should be easy. -Love, James /* if (msg instanceof
                 * JoinMessage) { processClientJoinMessage((JoinMessage) msg); } else if (msg
                 * instanceof GuessMessage) { processClientGuessMessage((GuessMessage) msg); }
                 * else if (msg instanceof CreateGameMessage) {
                 * processClientCreateGameMessage((CreateGameMessage) msg); }
                 */

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (Exception e) {
                System.out.println("Connection reset error");
            } /*
               * finally { try { if (output != null) { //output.close();
               * System.out.println(""); } if (input != null) { //input.close();
               * //clientSocket.close(); } } catch (IOException e) { //e.printStackTrace(); //
               * }
               * 
               * }
               */
        }
    }

}