package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game {
    
    private Player player;
    private Integer[] code;//What the players are trying to guess
    private ArrayList<Integer[]> allCodeGuesses;
    private ArrayList<String> allHitsAndBlows;
    private boolean isWinner;
    public Game(Player player)
    {
        this.player=player;
        this.isWinner=false;
        this.allCodeGuesses=new ArrayList<Integer[]>();
        this.allHitsAndBlows=new ArrayList<String>();
    }

    public void startGame()
    {   
        String quit="A";
        randomizeCode();
        while(!quit.equals("q"))
        {
            while(!areDistinct(this.code))
            {
                randomizeCode();
            }
            Scanner playerInput=new Scanner(System.in);
            System.out.println(this.player.getPlayerName()+ ", make your first guess");
            System.out.println("--------------------");
            Integer[] currentGuess=new Integer[4];
            while(true){}

    }
        
        
    }

    public static boolean areDistinct(Integer arr[])
    {
        // Put all array elements in a HashSet
        Set<Integer> s = 
           new HashSet<Integer>(Arrays.asList(arr));
  
        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    public void randomizeCode()
    {
        this.code=new Integer[4];
        for(int i=0;i<this.code.length;i++)
        {
            this.code[i]=(int)Math.random()*6;
        }
    }


}
