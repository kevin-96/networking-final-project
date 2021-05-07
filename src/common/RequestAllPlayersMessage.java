package common;

import java.util.List;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class RequestAllPlayersMessage extends Message {

    String message;
    List<String> allPlayers;

    public RequestAllPlayersMessage(){
        super("request");
    }

}
