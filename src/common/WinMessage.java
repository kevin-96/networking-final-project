/*
WinMessage
Author:James Jacobson
Message containing all of the players, the player who won, and the previous code
 */

package common;

import server.Player;

import java.util.List;

public class WinMessage extends Message {
    String name;
    int[] code;
    List<Player> allPlayers;

    public WinMessage(String player, List<Player> players, int[] code){
        super(player);
        this.name = player;
        this.code=code;
        this.allPlayers=players;
    }

    public String getName()
    {
        return this.name;
    }

    public int[] getCode(){return this.code;}

    public List<Player> getAllPlayers()
    {
        return this.allPlayers;
    }


}
