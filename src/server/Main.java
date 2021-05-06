package server;
public class Main
{
    public static void main(String args[])
    {
        System.out.println("Hi");
        GameState master=new GameState();
        master.addPlayer("James");
        master.createGames();
    }
}