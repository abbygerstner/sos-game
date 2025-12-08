package org.sosgame.sprint5;

public class HumanPlayer extends Player {
    public HumanPlayer(String color) { super(color); }

    @Override
    public boolean isComputer() { return false; }

    @Override
    public SOSGame.Move getMove(SOSGame game) {
        return null; // Human players don’t auto-generate moves
    }
}

