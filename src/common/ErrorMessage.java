package common;

import java.util.ArrayList;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class ErrorMessage extends Message {

    String message;
    ArrayList<String> allPlayers;

    public ErrorMessage(String message){
        super(message);
    }


    public ErrorMessage(ArrayList<String> allPlayers)
    {
        super(allPlayers.get(0));//
        this.allPlayers=allPlayers;
        
    }

    public ArrayList<String> getAllPlayers()
    {
        return this.allPlayers;
    }
}
