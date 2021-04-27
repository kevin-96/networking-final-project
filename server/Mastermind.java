package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Mastermind {

    private ArrayList<Player> players;
    private ArrayList<Game> games;
    private Integer[] code;// What the players are trying to guess

    public Mastermind() {
        this.players = new ArrayList<Player>();
        this.games = new ArrayList<Game>();
    }

    public void createGames() {
        randomizeCode();
        while (!areDistinct(this.code)) {
            randomizeCode();
        }
        for (Player player : this.players) {
            Game game=new Game(player, this.code);
            game.startGame();
        }

    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        this.players.add(player);
    }

    public static boolean areDistinct(Integer arr[]) {
        // Put all array elements in a HashSet
        Set<Integer> s = new HashSet<Integer>(Arrays.asList(arr));

        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    public void randomizeCode() {
        this.code = new Integer[4];
        for (int i = 0; i < this.code.length; i++) {
            this.code[i] = (int) (Math.random() * 6);
        }

    }
}
