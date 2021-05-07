package common;

//Might not actually have to do any of this class because the JoinMessage should already give the server a name for each connection
public class ErrorMessage extends Message {

    public ErrorMessage(String message){
        super(message);
    }


}
