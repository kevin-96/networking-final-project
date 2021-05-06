package server;

public class Player implements Serializable {
    
    private String playerName;
    //Latest hit and blows
    private int hitCount;
    private int blowCount;

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

    public void setHitAndBlows(int hits, int blows)
    {
        this.hitCount=hits;
        this.blowCount=blows;
    }
}
