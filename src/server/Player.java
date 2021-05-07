package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    
    private String playerName;
    // Latest hit and blows
    private List<Guess> allGuesses;

    public Player(String name)
    {
        this.playerName=name;
        this.allGuesses=new ArrayList<Guess>();
    }

    public String getPlayerName(){
        return this.playerName;
    }


    public List<Guess> getAllGuesses(){return this.allGuesses;}

    public int getHitCount()
    {
        if(allGuesses.size()>0) {
            return allGuesses.get(allGuesses.size() - 1).hitCount;
        }
        else
        {
            return 0;
        }
    }

    public int getBlowCount()
    {
        if(allGuesses.size()>0) {
            return allGuesses.get(allGuesses.size() - 1).blowCount;
        }
        else
        {
            return 0;
        }
    }

    public void addGuess(Guess guess)
    {
        this.allGuesses.add(guess);
    }

    public void reset()
    {
        this.allGuesses = new ArrayList<Guess>() ;
    }
}
