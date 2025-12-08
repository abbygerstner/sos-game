package org.sosgame.sprint5;

import java.util.List;

public interface GameEventListener {
    void onSOSFormed(List<SOSGame.SOSSequence> sequences);

    void onGameOver(String winner);

    void onMoveMade(int row, int col, char letter, String playerColor);

}
