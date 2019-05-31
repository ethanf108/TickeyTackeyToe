package com.halfnet.tickeytackeytoe.game;

public class GameRef {

    private final Game game;

    public GameRef() {
        this.game = new Game();
    }

    public Game getGame() {
        return this.game;
    }

    public void setNextPlayPosition(TilePosition tp) {
        this.game.setNextPlayPosition(tp);
    }

    public void playPiece(TilePosition tp) {
        this.game.playPiece(tp);
    }
}
