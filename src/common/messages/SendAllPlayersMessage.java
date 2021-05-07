/*
SendAllPlayersMessage
Author:James Jacobson
 */

package common.messages;

import java.util.List;

import common.datatypes.Player;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class SendAllPlayersMessage extends Message {

    List<Player> allPlayers;


    public SendAllPlayersMessage(List<Player> allPlayers)
    {
        super("All Players");//
        this.allPlayers=allPlayers;
    }

    public List<Player> getAllPlayers()
    {
        return this.allPlayers;
    }
}
