package server;

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
            // Make a copy of players/playerNames to remove the player


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
