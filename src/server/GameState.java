package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameState {

    private GameState instance;

    public GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    private List<Player> players;
    private List<String> playerNames; // Players in a game
    // private ArrayList<Game> games;
    private boolean gameStarted=true;

    public GameState() {
        this.players = new Vector<Player>();
        this.playerNames = new Vector<String>();
    }

    public void addPlayer(String name) {
        synchronized (players) {
            Player player = new Player(name);
            this.playerNames.add(name);
            this.players.add(player);
        }
    }

    public void removePlayer(String name)
    {
        synchronized (players) {
            // Make a copy of players/playerNames to remove the player
            List<Player> filteredPlayers = new Vector(players);
            
            for (Player player : filteredPlayers) {
                if (player.getPlayerName().equals(name)) {
                    filteredPlayers.remove(player);
                }
            }

            this.players = filteredPlayers;
        }
    }

    public List<Player> getAllPlayers() {
        return this.players;
    }

    public List<String> getAllPlayerNames(){
        return this.playerNames;
    }

    public boolean didGameStart()
    {
        return this.gameStarted;
    }

    public void startGame()
    {
        this.gameStarted=true;
    }

    public void endGame()
    {
        this.gameStarted=false;
    }
}
