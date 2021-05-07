/*
Player class
Author:James Jacobson
 */

package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private String playerName;
    private List<Guess> allGuesses;

    public Player(String name) {
        this.playerName = name;
        this.allGuesses = new ArrayList<>();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public List<Guess> getAllGuesses() {
        return this.allGuesses;
    }

    // Returns the latest hit count. If there are no guesses, defaults to 0
    public int getHitCount() {
        if (allGuesses.isEmpty()) {
            return 0;
        } else {
            return allGuesses.get(allGuesses.size() - 1).getHitCount();
        }
    }

    // Returns the latest blow count. If there are no guesses, defaults to 0
    public int getBlowCount() {
        if (allGuesses.isEmpty()) {
            return 0;
        } else {
            return allGuesses.get(allGuesses.size() - 1).getBlowCount();
        }
    }

    public void addGuess(Guess guess) {
        this.allGuesses.add(guess);
    }

    // Occurs when a player has won the game. Deletes all guesses
    public void reset() {
        this.allGuesses.clear();
    }
}
