package common;

public class JoinMessage extends Message {
    String name;
    
    public JoinMessage(String name){
        super(name);
        this.name=name;
    }

    public String getName()
    {
        return this.name;
    }
}
