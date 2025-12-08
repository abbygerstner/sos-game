package org.sosgame.sprint5;

public class ComputerPlayer extends Player {

    public ComputerPlayer(String color) {
        super(color);
    }

    @Override
    public boolean isComputer() {
        return true;
    }

    @Override
    public SOSGame.Move getMove(SOSGame game) {
        return game.getComputerMove();
    }
}
