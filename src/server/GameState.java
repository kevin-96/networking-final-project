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

    private ArrayList<Player> players;
    private ArrayList<String> playerNames; // Players in a game
    // private ArrayList<Game> games;
    private Integer[] code;// What the players are trying to guess
    private boolean gameStarted=true;

    public GameState() {
        this.players = new ArrayList<Player>();
        this.playerNames = new ArrayList<String>();
    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        this.playerNames.add(name);
        this.players.add(player);
    }

    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }

    public ArrayList<String> getAllPlayerNames(){
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
