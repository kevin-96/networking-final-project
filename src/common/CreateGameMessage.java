package common;

import java.util.List;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class CreateGameMessage extends Message {

    String message;
    List<String> allPlayers;
    
    public CreateGameMessage(String message){
        super(message);
    }

    
    public CreateGameMessage(List<String> allPlayers)
    {
        super(allPlayers.get(0));//
        this.allPlayers=allPlayers;
        
    }

    public List<String> getAllPlayers()
    {
        return this.allPlayers;
    }
}
