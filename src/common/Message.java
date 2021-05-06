package common;

import java.io.Serializable;

public class Message implements Serializable {
    String message;
    
    public Message(String message){
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }
}
