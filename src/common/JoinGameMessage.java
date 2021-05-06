package common;

import java.util.ArrayList;

import server.Player;

// Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class JoinGameMessage extends Message {

    String message;
    ArrayList<Player> allPlayers;
    boolean isStarted;
    
    public JoinGameMessage(String message){
        super(message);
    }

    
    public JoinGameMessage(ArrayList<Player> allPlayers, boolean isStarted)
    {
        super(isStarted + "");//
        this.allPlayers = allPlayers;
        this.isStarted = isStarted;
    }

    public ArrayList<Player> getAllPlayers()
    {
        return this.allPlayers;
    }

    public boolean getIsStarted()
    {
        return this.isStarted;
    }
}
