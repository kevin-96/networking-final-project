package server;
public class Main
{
    public static void main(String args[])
    {
        System.out.println("Hi");
        Mastermind master=new Mastermind();
        master.addPlayer("James");
        master.createGames();
    }
}