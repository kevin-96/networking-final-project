package server;

import java.util.ArrayList;

public class GameState {

    private GameState instance;

    public GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    private ArrayList<Player> players; // Players in a game
    // private ArrayList<Game> games;
    private Integer[] code;// What the players are trying to guess

    public GameState() {
        this.players = new ArrayList<Player>();
    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        this.players.add(player);
    }

    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }
}
