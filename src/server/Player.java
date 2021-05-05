package server;

import java.util.ArrayList;

public class Player {
    
    private String playerName;
    private int hitCount;
    private int blowCount;
    private ArrayList<Integer[]> guesses;

    public Player(String name)
    {
        this.playerName=name;
        this.guesses=new ArrayList<Integer[]>();
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

    public ArrayList<Integer[]> getAllGuesses()
    {
        return this.guesses;
    }

    public void setHitAndBlows(int hits, int blows)
    {
        this.hitCount=hits;
        this.blowCount=blows;
    }

    public void setGuesses(ArrayList<Integer[]> guesses)
    {
        this.guesses=guesses;
    }

    public void addGuess(Integer[] guess)
    {
        this.guesses.add(guess);
    }




}
