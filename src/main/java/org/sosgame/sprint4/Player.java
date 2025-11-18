package org.sosgame.sprint4;

public abstract class Player {
    protected String color; // "Blue" or "Red"

    public Player(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    // Humans don't autoplay but computers autoplay
    public abstract boolean isComputer();

    // For computers: generates next move.
    public abstract SOSGame.Move getMove(SOSGame game);
}

