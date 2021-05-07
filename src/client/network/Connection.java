package client.network;

/*
  Connection
  @author Brian Carballo, Kevin Sangurima
  The connection that the player has to the server. Sends and retrieves messages to and from server
 */

import java.io.IOException;
import java.net.Socket;

import common.messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection {
    private final String hostname;
    private final String name;
    private final int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isPlaying;

    public Connection(String hostname, int port, String name, boolean isSpectator) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.name = name;
        this.isPlaying = !isSpectator;
        establishConnection();
    }

    public void establishConnection() throws Exception {
        Socket socket = new Socket(hostname, port);
        // Used to check if connection exists by other functions
        System.out.println(
                "Connected. Communicating from port " + socket.getLocalPort() + " to port " + socket.getPort() + ".");
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        //Once connected, join the game
        joinGame();
    }

    //Sends a guess message once the user clicks the submit guess button
    public void guess(int[] guess) {

        try {
            this.out.writeObject(new GuessMessage(this.name, guess));
            this.out.reset();
            //expectGuess();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    //Sends a join game message, gets a JoinGameMessage in response. Server add the player
    public void joinGame() throws Exception {
        // Occurs when the user first joins a games
        this.out.writeObject(new JoinMessage(this.name, isPlaying));
        this.out.reset();
        Object response = this.in.readObject();
        if (response instanceof JoinGameMessage) {
            JoinGameMessage jgm=(JoinGameMessage) response;
            System.out.println(jgm.getMessage());
        }
        else
        {
            String error;
            if (response instanceof ErrorMessage) {
                error = ((ErrorMessage) response).getMessage();
            } else {
                error = "Unknown error joining game";
            }
            System.out.println(error);
            throw new Exception(error);
        }
    }

    // Occurs when the user first joins a games
    public Message getState()
    {
        try {
            this.out.writeObject(new RequestAllPlayersMessage());// Hard coded true
            this.out.reset();
            return (Message) this.in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
