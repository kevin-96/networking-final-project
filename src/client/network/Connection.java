package client.network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import common.GuessMessage;
import common.JoinMessage;
import common.Message;
import server.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection {
    private String hostname;
    private String name;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean socketExists;

    public static void main(String[] args) {
        new Connection("127.0.0.1", 1234, "Message");
    }

    public Connection(String hostname, int port, String name) {
        this.hostname = hostname;
        this.port = port;
        this.name = name;

        try {
            establishConnection();
        } catch (Exception e) {
            System.out.println("Connection could not be established");
        }

    }

    public void establishConnection() throws Exception {
        socket = new Socket(hostname, port);
        socketExists = true;
        // Used to check if connection exists by other functions
        System.out.println(
                "Connected. Communicating from port " + socket.getLocalPort() + " to port " + socket.getPort() + ".");

        inGame();

    }

    public void inGame() {
        Scanner s = new Scanner(System.in);
        String tempName = s.nextLine();

        try {
            // Occurs when the user first joins a game
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            out.writeObject(new JoinMessage(tempName));
            out.reset();
            while (true) {// I think this needs a while loop as well cause it needs to wait for a server
                          // response but I could be wrong
                // Removing this line causes the server to crash
                Object response = input.readObject();// DEBUG
                if(response instanceof Message)
                {
                    Message m = (Message) response;
                    System.out.println(m.getMessage());
                }
                if(response instanceof Player)
                {
                    Player p = (Player) response;
                    System.out.println("Player: " + p.getPlayerName() + " Hits = " + p.getHitCount() + " Blows = " + p.getBlowCount());
                }
                
                String line = s.nextLine();
                int[] guess = new int[4];
                for(int i = 0; i < guess.length; i++) {
                    guess[i] = line.charAt(i) - '0';
                }
                out.writeObject(new GuessMessage(tempName, guess));
                out.reset();
                
                // System.out.println(response.getMessage());//DEBUG

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
