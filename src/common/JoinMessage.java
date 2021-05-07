/*
JoinMessage
Author:James Jacobson
Client joins server
 */

package common;

public class JoinMessage extends Message {
    String name;
    boolean isPlaying;//Not spectator
    
    public JoinMessage(String name, boolean isPlaying){
        super(name);
        this.name = name;
        this.isPlaying = isPlaying;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean getIsPlaying(){return this.isPlaying;}

}
