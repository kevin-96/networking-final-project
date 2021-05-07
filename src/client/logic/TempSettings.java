package client.logic;

/**
 * @author jrgermain
 *  A temporary class used to hold game settings that I created while developing the gui.
 *  Will eventually be replaced with a new class that is shared between the client and server.
 */


public class TempSettings implements Cloneable {

    // Client Settings
    public String playerName;
    public boolean isConnected;
    public String serverAddress;
    public int port;
    public boolean isSpectator;

    // Game Settings
    public int codeLength = 4;
    public int maxDigit = 5;

    public TempSettings(String playerName, boolean isConnected, String serverAddress, int port, boolean isSpectator, int codeLength, int maxDigit) {
        this.playerName = playerName;
        this.isConnected = isConnected;
        this.serverAddress = serverAddress;
        this.port = port;
        this.isSpectator = isSpectator;
        this.codeLength = codeLength;
        this.maxDigit = maxDigit;
    }

    public static TempSettings getDefaultSettings() {
        try {
            return DEFAULT_SETTINGS.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public TempSettings clone() throws CloneNotSupportedException {
        return (TempSettings) super.clone();
    }

    private static final TempSettings DEFAULT_SETTINGS = new TempSettings("Player", false, "localhost", 1234, false, 4, 9);
}
