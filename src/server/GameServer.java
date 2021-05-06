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
import client.network.Message;

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
    private ArrayList<Connection> connections;
    private int[] code;

    public GameServer() {
        state = new GameState();
        connections = new ArrayList<Connection>();
    }

    private void processClientGuessMessage(Message msg) {
        // Find the sender's player object and update its hit and blow count
        /*
         * ArrayList<Player> players = state.getAllPlayers(); Player currentPlayer; for
         * (Player player : players) { if (player.getPlayerName().equals(msg.name)) {
         * int[] hab = this.guess(mes.guess); player.setHitAndBlows(hab[0], hab[1]);
         * currentPlayer = player; break; } }
         * 
         * 
         * // Update other players for (Connection c : this.connections) { // Send
         * player object on object output stream FileOutputStream file = new
         * FileOutputStream("file.txt"); ObjectOutputStream output = new
         * ObjectOutputStream(file); // Create the ObjectOutputStream
         * output.writeObject(currentPlayer); // Add the currentPlayer object to the
         * ObjectOutputSteam System.out.println("Player: " + currentPlayer.getName +
         * "Hit Count: " + currentPlayer.getHitCount + "Blow Count: " +
         * currentPlayer.getBlowCount); // Send Player, specifically the name, the hit
         * // count, the blow count, to each connection // connection.write("Player: " +
         * player.getName + "Hit Count: " + // player.getHitCount + "Blow Count: " +
         * player.getBlowCount) // Send Player, // specifically the name, the hit count,
         * the blow count, to each connection output.close(); // Close the stream and
         * sends out whatever was written to it (In our case, the // player)
         * 
         * }
         */
        // //for connection c in connections
        // // send c gameState
    }

    /*
     * private void processClientJoinMessage(Message msg) {
     * 
     * FileOutputStream file = new FileOutputStream("file.txt"); ObjectOutputStream
     * output = new ObjectOutputStream(file); // Create the ObjectOutputStream
     * output.writeObject(currentPlayer);
     * 
     * // // if some player p in gameState.players has p.name = msg.name // // send
     * error response (duplicate player name) // //else
     * 
     * // // send success response that includes gameSettings(Games Setting for now
     * are // default) // }
     * 
     * // Process a guess to see if it matches with the code }
     * 
     * private void processClientCreateGameMessage(Message msg) { // bool
     * allowDuplicates=msg.allowDublicates // int maxDigit=msg.maxDigit int maxDigit
     * = 5; boolean allowDuplicates = false; randomizeCode(maxDigit); while
     * (!areDistinct(this.code) && !allowDuplicates) { randomizeCode(maxDigit); } //
     * // if some player p in gameState.players has p.name = msg.name // // send
     * error response (duplicate player name) // //else
     * 
     * // // send success response that includes gameSettings(Games Setting for now
     * are // default) // }
     * 
     * // Process a guess to see if it matches with the code }
     */

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

    private static boolean areDistinct(Integer arr[]) {
        // Put all array elements in a HashSet
        Set<Integer> s = new HashSet<Integer>(Arrays.asList(arr));

        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    private void randomizeCode(int maxDigit) {
        this.code = new int[4];
        for (int i = 0; i < this.code.length; i++) {
            this.code[i] = (int) (Math.random() * (maxDigit + 1));
        }

    }

    public static void main(String args[]) {
        ServerSocket server = null;
        System.out.println("Server is active");
        try {

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
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
                System.out.println("Yuh");
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

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            //Creates inputs and outputs
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {

                // Creates an ObjectInputStream
                output = new ObjectOutputStream(this.clientSocket.getOutputStream());
                input = new ObjectInputStream(this.clientSocket.getInputStream());
                
                
                // Reads the objects

                Message msg = (Message) input.readObject();
                if (msg != null) {
                    System.out.println("Message:" + msg.getMessage());
                }
                    /*
                    This will not work the first time you try it. You get a connection reset error on the server.
                    Client side will stop working but you can join again.
                    You will be reset again but with no error on the server side, and you will see a message
                    This is our only problem right now. When this is figured out,
                    i think the rest should be easy.
                    -Love,
                        James
                /*
                 * if (msg instanceof JoinMessage) { processClientJoinMessage((JoinMessage)
                 * msg); } else if (msg instanceof GuessMessage) {
                 * processClientGuessMessage((GuessMessage) msg); } else if (msg instanceof
                 * CreateGameMessage) { processClientCreateGameMessage((CreateGameMessage) msg);
                 * }
                 */

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}