package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game {

    private Player player;
    private Integer[] code;// What the players are trying to guess
    private ArrayList<Integer[]> allCodeGuesses;
    private ArrayList<String> allHitsAndBlows;
    private boolean isWinner;

    public Game(Player player, Integer code[]) {
        this.player = player;
        this.isWinner = false;
        this.allCodeGuesses = new ArrayList<Integer[]>();
        this.allHitsAndBlows = new ArrayList<String>();
        this.code = code;
    }

    public void startGame() {
        while (!isWinner) {
            String quit = "A";// Implement a quit function, but for now, game ends when you win the game
            // debugDisplayArray(this.code);
            Scanner playerInput = new Scanner(System.in);
            System.out.println(this.player.getPlayerName() + ", make your first guess");
            Integer[] currentGuess = new Integer[4];
            while (!Arrays.equals(currentGuess, code)) {
                currentGuess = new Integer[4];
                System.out.println("--------------------");
                if (allCodeGuesses.size() != 0) {
                    //displayGuessesAndMarks();
                }
                String guess = playerInput.nextLine();
                if (validateGuess(guess)) {
                    int hitCount = 0;
                    int blowCount = 0;
                    for (int i = 0; i < currentGuess.length; i++) {
                        currentGuess[i] = Integer.parseInt(guess.substring(i, i + 1));
                    }
                    for (int i = 0; i < currentGuess.length; i++) {
                        for (int j = 0; j < currentGuess.length; j++) {
                            if (currentGuess[i] == code[j] && i == j) {
                                hitCount++;
                            } else if (currentGuess[i] == code[j] && i != j) {
                                blowCount++;
                            }
                        }
                    }
                    // debugDisplayArray(currentGuess);

                    String hitAndBlows = hitCount + "H" + blowCount + "B";
                    player.addGuess(currentGuess);
                    //this.allCodeGuesses.add(currentGuess);
                    //player.setAllGuesses
                    player.setHitAndBlows(hitCount,blowCount);
                    //this.allHitsAndBlows.add(hitAndBlows);

                } else {
                    System.out.println("Invalid Guess, try again");
                }
            }
            //System.out.println("You Win!");
            this.isWinner = true;
            playerInput.close();

        }

    }

    public boolean validateGuess(String guess) {
        if (guess.length() == 4) {
            for (int i = 0; i < guess.length(); i++) {
                if (!(guess.charAt(i) >= 48 && guess.charAt(i) <= 53)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void displayGuessesAndMarks() {

        for (int j = 0; j < this.allCodeGuesses.size(); j++) {
            Integer[] guess = this.allCodeGuesses.get(j);
            // debugDisplayArray(guess);
            String guessAndMarks = "Guess " + (j+1) + " --- [";
            for (int i = 0; i < guess.length; i++) {
                if (i != guess.length - 1) {
                    guessAndMarks += guess[i] + "|";
                } else {
                    guessAndMarks += guess[i];
                }
            }
            guessAndMarks += "] " + allHitsAndBlows.get(j);
            System.out.println(guessAndMarks);
        }
    }

    private void debugDisplayArray(Integer[] arr) {
        for (Integer num : arr) {
            System.out.print(num);
        }
        System.out.println("");
    }

    public boolean playerHasWon()
    {
        return this.isWinner;
    }

}
