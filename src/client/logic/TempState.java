package client.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jrgermain
 *  A temporary class used to hold game state that I created while developing the gui.
 *  Will eventually be replaced with a new class that is shared between the client and server.
 */

@Deprecated
public class TempState {
    // Client State
    public List<TempGuess> guesses;

    // Game State
    public List<TempPlayer> gameState;

    public static final TempState getDefaultState() {
        List<TempGuess> guesses = new ArrayList<>();
        List<TempPlayer> gameState = new ArrayList<>();

        TempPlayer defaultPlayer = new TempPlayer();
        defaultPlayer.name = "Fred";
        defaultPlayer.hits = 2;
        defaultPlayer.blows = 1;

        gameState.add(defaultPlayer);

        TempState ts = new TempState();
        ts.guesses = guesses;
        ts.gameState = gameState;
        return ts;
    }
}
