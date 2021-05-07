/*
GameState
Author:James Jacobson
Holds all of the players, and their guesses. Knows if the game has started
 */

package server.logic;

import common.datatypes.Player;

import java.util.List;
import java.util.Vector;

public class GameState {

    private List<Player> players;
    private boolean gameStarted=true;

    public GameState() {
        this.players = new Vector<Player>();
    }

    public void addPlayer(String name) {
        synchronized (players) {
            Player player = new Player(name);
            this.players.add(player);
        }
    }

    public void removePlayer(String name)
    {
        synchronized (players) {
            this.players.removeIf(player -> player.getPlayerName().equals(name));
        }
    }

    public List<Player> getAllPlayers() {
        return this.players;
    }

    public boolean didGameStart()
    {
        return this.gameStarted;
    }

}
