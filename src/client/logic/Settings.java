package client.logic;

/**
 * @author jrgermain
 *  Game settings
 */


public class Settings implements Cloneable {
    public String playerName;
    public boolean isConnected;
    public String serverAddress;
    public int port;
    public boolean isSpectator;
    public int codeLength;
    public int maxDigit;

    public Settings(String playerName, boolean isConnected, String serverAddress, int port, boolean isSpectator, int codeLength, int maxDigit) {
        this.playerName = playerName;
        this.isConnected = isConnected;
        this.serverAddress = serverAddress;
        this.port = port;
        this.isSpectator = isSpectator;
        this.codeLength = codeLength;
        this.maxDigit = maxDigit;
    }

    public static Settings getDefaultSettings() {
        try {
            return DEFAULT_SETTINGS.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public Settings clone() throws CloneNotSupportedException {
        return (Settings) super.clone();
    }

    private static final Settings DEFAULT_SETTINGS = new Settings("Player", false, "localhost", 1234, false, 4, 5);
}
