package client;

import client.gui.MainWindow;
import client.logic.TempSettings;
import client.logic.TempState;

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
        window.processSettings(TempSettings.getDefaultSettings());
        window.processState(TempState.getDefaultState());
    }

    public static void main(String[] args) {
        new Client();
    }
}
