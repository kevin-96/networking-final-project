package client.network;

/*
  Connection handling

  @author Brian Carballo, Kevin Sangurima
 */

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import common.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection implements Serializable {
    private transient final String hostname;
    private transient final String name;
    private transient final int port;
    private ObjectInputStream in;
    private transient ObjectOutputStream out;
    private boolean isPlaying;

    public static void main(String[] args) throws Exception {
        new Connection("127.0.0.1", 1234, "Message", false);
    }

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

    public Message getState()
    {
        try {
            // Occurs when the user first joins a games
            this.out.writeObject(new RequestAllPlayersMessage());// Hard coded true
            this.out.reset();
            return (Message) this.in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
