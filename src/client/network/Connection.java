package client.network;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection {
    private String hostname;
    private String name;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean socketExists;

    public static void main(String[] args) {
        new Connection("127.0.0.1",1234,"Message");
    }
    
    public Connection(String hostname, int port, String name){
        this.hostname = hostname;
        this.port = port;
        this.name = name;

        try {
            establishConnection();
        } catch(Exception e){
            System.out.println("Fuck");
        }
        
    }
    
    public void establishConnection() throws Exception {
        socket = new Socket(hostname, port);
        socketExists = true;
        //Used to check if connection exists by other functions
        System.out.println("Connected. Communicating from port " +
                socket.getLocalPort() + " to port " +
                socket.getPort() + ".");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        out.writeObject(new Message("Hello"));
        out.flush();
        
    }

}
