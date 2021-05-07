package server;

import java.io.Serializable;

public class Player implements Serializable {
    
    private String playerName;
    // Latest hit and blows
    private int hitCount;
    private int blowCount;
    private int[] latestGuess;

    public Player(String name)
    {
        this.playerName=name;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public int getHitCount()
    {
        return this.hitCount;
    }

    public int getBlowCount()
    {
        return this.blowCount;
    }

    public int[] getLatestGuess(){return this.latestGuess;}



    public void setHitAndBlows(int hits, int blows, int[] guess)
    {
        this.hitCount = hits;
        this.blowCount = blows;
        this.latestGuess= guess;
    }
}
