package common;

import java.util.ArrayList;

import server.Player;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class SendAllPlayersMessage extends Message {

    String message;
    ArrayList<Player> allPlayers;

    public SendAllPlayersMessage(String message){
        super(message);
    }


    public SendAllPlayersMessage(ArrayList<Player> allPlayers)
    {
        super("All Players");//
        this.allPlayers=allPlayers;
    }

    public ArrayList<Player> getAllPlayers()
    {
        return this.allPlayers;
    }
}
