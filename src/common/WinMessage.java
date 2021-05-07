package common;

import server.Player;

public class WinMessage extends Message {
    String name;
    int[] code;

    public WinMessage(String player, int[] code){
        super(player);
        this.name = player;
        this.code=code;
    }

    public String getName()
    {
        return this.name;
    }

    public int[] getCode(){return this.code;}


}
