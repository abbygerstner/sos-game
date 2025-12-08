package org.sosgame.sprint5;

public class GameFactory {

    public static SOSGame createGame(Console.GameMode mode,
                                     int size,
                                     boolean blueIsComputer,
                                     boolean redIsComputer) {
        return switch (mode) {
            case SIMPLE -> new SimpleSOSGame(size, blueIsComputer, redIsComputer);
            case GENERAL -> new GeneralSOSGame(size, blueIsComputer, redIsComputer);
        };
    }
}
