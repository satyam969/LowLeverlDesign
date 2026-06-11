package entities;

import strategies.bot.BotPlayingStrategy;

public class BotPlayer extends Player {
    private BotPlayingStrategy playingStrategy;

    public BotPlayer(String name, String symbol, BotPlayingStrategy playingStrategy) {
        super(name, symbol);
        this.playingStrategy = playingStrategy;
    }

    @Override
    public Move makeMove(Board board) {
        System.out.println("Bot " + name + " is thinking...");
        return playingStrategy.decideMove(this, board);
    }
}
