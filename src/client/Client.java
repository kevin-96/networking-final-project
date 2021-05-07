package client;

import java.util.ArrayList;
import server.Player;

import client.gui.MainWindow;
import client.logic.Settings;

/**
 * The top-level gui component of the application
 *
 * @author Joey Germain
 */
public class Client {
    MainWindow window;

    public Client() {
        // Set up main window
        window = new MainWindow();
        window.display();
        window.processSettings(Settings.getDefaultSettings());
        window.processState(new ArrayList<Player>());

    }

    public static void main(String[] args) {
        new Client();
    }
}
