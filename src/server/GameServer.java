package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import common.*;

/**************
 * GameServer class Authors: Phillip Nam, James Jacobson, Ryan Clark Spring 2021
 *
 * This class is the server for the Mastermind game. It handles the networking
 * of the game and is essentially responsible for updating the game state.
 *************/

public class GameServer {

    // // Server Methods

    private GameState state;
    private List<ClientHandler> clients;
    // private Map<

    private int[] code;

    public GameServer() {
        state = new GameState();
        //code = this.createCode(5,false);
        code = new int[]{1,2,3,4};//DEBUG
        clients = new Vector<ClientHandler>();
        ServerSocket server = null;
        System.out.println("Enter port number");
        Scanner scan= new Scanner(System.in);
        int port=scan.nextInt();
        System.out.println("Server is active on port " + port);
        scan.close();
        try {

            // GuessMessage
            //
            // server is listening on port 1234
            server = new ServerSocket(port);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // Might have to implement this here: once the socket is added, make 2 more
                // arraylists of the sockets ObjectOutputStream and ObjectInputStream
                // Since you cannot make 2 ObjectStreams of the same socket, it might be best to
                // save thoise in arraylist.

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);
                clients.add(clientSock);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
                // System.out.println("Yuh");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processClientGuessMessage(GuessMessage msg) {
        // Find the sender's player object and update its hit and blow count
        try {
            for (Player player : state.getAllPlayers()) {
                if (player.getPlayerName().equals(msg.getName())) {
                    int[] hab = guess(msg.getGuess());
                    System.out.println("H" + hab[0] + "B" + hab[1]);
                    synchronized (state) {
                        Guess guess=new Guess();
                        guess.hitCount=hab[0];
                        guess.blowCount=hab[1];
                        guess.digits=msg.getGuess();
                        player.addGuess(guess);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean playerExists(String name) {
        if (name == null) {
            return false;
        }
        for (Player player : state.getAllPlayers())  {
            if (player.getPlayerName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Message processClientJoinMessage() {
        return new JoinGameMessage(state.getAllPlayers(), state.didGameStart());
    }



    private Message processRequestAllPlayers() { // bool
        try {
            List<Player> players = state.getAllPlayers();
            for (Player player : players) {
                if (player.getAllGuesses().size()>0 && player.getHitCount() == 4) {
                    System.out.println("Reached1");
                    for (Player losers : players) {
                        losers.reset();
                    }
                    System.out.println("Reached2");
                    int maxDigit = 5;
                    boolean allowDuplicates = false;
                    int[] oldCode=this.code;
                    this.code=this.createCode(maxDigit, false);
                    System.out.println("Reached3");

                    // Send everyone a win message
                    for (ClientHandler client : clients) {
                        client.getOutput().writeObject(new WinMessage(player.getPlayerName(), players, oldCode));
                    }
                    return null;
                }
            }
            return new SendAllPlayersMessage(state.getAllPlayers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int[] guess(int[] currentGuess) {
        int hitCount = 0;
        int blowCount = 0;
        for (int i = 0; i < currentGuess.length; i++) {
            for (int j = 0; j < currentGuess.length; j++) {
                if (currentGuess[i] == code[j] && i == j) {
                    hitCount++;
                } else if (currentGuess[i] == code[j] && i != j) {
                    blowCount++;
                }
            }
        }
        return new int[] { hitCount, blowCount };
    }

    private static boolean areDistinct(int[] arr) {
        // Put all array elements in a HashSet
        Set<Integer> s = new HashSet<Integer>();
        for (int i : arr) {
            s.add(i);
        }

        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    private static int[] randomizeCode(int maxDigit) {
        int[] code = new int[4];
        for (int i = 0; i < code.length; i++) {
            code[i] = (int) (Math.random() * (maxDigit + 1));
        }
        return code;

    }

    private int[] createCode(int maxDigit, boolean dups)
    {
        int [] code = GameServer.randomizeCode(maxDigit);
        while (!areDistinct(code) && !dups) {
            code = randomizeCode(maxDigit);
        }
        return code;
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private ObjectOutputStream output;
        private String playerName;
        
        public ObjectOutputStream getOutput() {
            return output;
        }

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() { // A player joins the game(I think)
            // Creates inputs and outputs
            try {
                this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());// Only one of these should be
                                                                                          // made
                // // per player
                ObjectInputStream input = new ObjectInputStream(this.clientSocket.getInputStream());// Only one of these should be
                // made //
                // per player

                while (true) {
                    Message msg = (Message) input.readObject();
                    if (msg instanceof JoinMessage) {
                        JoinMessage jm = (JoinMessage) msg;
                        boolean _isPlaying = jm.getIsPlaying();
                        boolean _playerExists = _isPlaying && playerExists(jm.getName());
                        if (_isPlaying && !_playerExists) {
                            state.addPlayer(jm.getName());
                            this.playerName = jm.getName();
                        } else {
                            this.playerName = null;
                        }
                        Message toSend = _playerExists ? new ErrorMessage("Player already exists") : processClientJoinMessage();
                        this.output.writeObject(toSend);
                        this.output.flush();
                    } else if (msg instanceof GuessMessage) {
                        processClientGuessMessage((GuessMessage) msg);
                        this.output.reset();
                    } else if (msg instanceof RequestAllPlayersMessage) {
                        RequestAllPlayersMessage req = (RequestAllPlayersMessage) msg;
                        Message all = processRequestAllPlayers();
                        if (all != null) {
                            this.output.writeObject(all);
                            this.output.reset();
                        }
                    }
                    // This hangs here until a message is sent
                }
            } catch (SocketException e) {
                System.out.println(this.playerName + " has left the game");
                state.removePlayer(this.playerName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Connection reset error");
                e.printStackTrace();
            }
        }

    }

}