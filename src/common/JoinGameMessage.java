/*
JoinGameMessage
Author:James Jacobson
Sever sends a message that the client has joined a game, includes game data
 */

package common;

import java.util.List;

import server.Player;

// Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class JoinGameMessage extends Message {

    List<Player> allPlayers;
    boolean isStarted;


    public JoinGameMessage(List<Player> allPlayers, boolean isStarted)
    {
        super(isStarted + "");//
        this.allPlayers = allPlayers;
        this.isStarted = isStarted;
    }

}
