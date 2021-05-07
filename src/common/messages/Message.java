/*
Message
Author:James Jacobson
Contains a message that can be sent to and from the server
 */
package common.messages;

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
