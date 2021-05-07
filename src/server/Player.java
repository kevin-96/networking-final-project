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
        return allGuesses.get(allGuesses.size()-1).hitCount;
    }

    public int getBlowCount()
    {
        return allGuesses.get(allGuesses.size()-1).blowCount;
    }

    public void addGuess(Guess guess)
    {
        this.allGuesses.add(guess);
    }
}
