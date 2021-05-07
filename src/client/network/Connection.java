package client.network;


import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import common.*;
import server.GameServer;
import server.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection implements Serializable {
    private transient final String hostname;
    private transient final String name;
    private transient Socket socket;
    private transient final int port;
    public boolean socketExists=false;
    private ObjectInputStream in;
    private transient ObjectOutputStream out;

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
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.socketExists=true;

        // This thread will handle the client
        // separately
        joinGame();
        //Needs a parameter that see if it is a spectator or not
    }

    public void guess(int[] guess) {

        try {
            this.out.writeObject(new GuessMessage(this.name, guess));
            this.out.reset();
            //expectGuess();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    public Player expectGuess(){
        try {
            System.out.println(this.name);
            Object response = this.in.readObject();

            if (response instanceof Player) {
                Player p=(Player) response;
                return p;
            }

            else
            {
                System.out.println("Error Joining game");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public SettingsMessage createGame()
    {
        try {
            // Occurs when the user first joins a games
            this.out.writeObject(new CreateGameMessage(this.name));
            this.out.reset();
            Object response = this.in.readObject();
            if (response instanceof SettingsMessage) {
                SettingsMessage sm=(SettingsMessage) response;
                return sm;
            }
            else
            {
                System.out.println("Error Creating game");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JoinGameMessage joinGame() {

        try {
            // Occurs when the user first joins a games
            this.out.writeObject(new JoinMessage(this.name,true));// Hard coded true
            this.out.reset();
            Object response = this.in.readObject();
            if (response instanceof JoinGameMessage) {
                JoinGameMessage jgm=(JoinGameMessage) response;
                System.out.println(jgm.getMessage());
                return jgm;
            }
            else
            {
                System.out.println("Error Joining game");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Player> getAllPlayers()
    {
        try {
            // Occurs when the user first joins a games
            this.out.writeObject(new RequestAllPlayersMessage());// Hard coded true
            this.out.reset();
            Object response = this.in.readObject();
            if (response instanceof SendAllPlayersMessage) {
                return ((SendAllPlayersMessage) response).getAllPlayers();
            }
            else
            {
                System.out.println("Error getting all players");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
