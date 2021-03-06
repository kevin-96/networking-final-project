package common.messages;
/*
GuessMessage
Author:James Jacobson
 */
public class GuessMessage extends Message {

    String name;
    int[] guess;

    public GuessMessage(String name, int[] guess)
    {
        super(name);
        this.name = name;
        this.guess = guess;
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
