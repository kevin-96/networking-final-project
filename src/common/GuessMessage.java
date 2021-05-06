package common;

public class GuessMessage extends Message {

    String message;
    String name;
    int[] guess;
    
    public GuessMessage(String message){
        super(message);
    }

    public GuessMessage(String name, int[] guess)
    {
        super(name);
        this.name=name;
        this.guess=guess;
    }

    public String getName()
    {
        return this.name;
    }

    public int[] getGuess()
    {
        return this.guess;
    }
}
