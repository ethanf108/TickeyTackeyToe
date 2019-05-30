package com.halfnet.tickeytackeytoe.game;

public class Game {

    private final Board gameBoard;
    private Piece currentTurn = Piece.X;
    private TilePosition nextPlayPosition = null;

    public Game() {
        this.gameBoard = new Board();
    }

    public Piece getCurrentTurn() {
        return this.currentTurn;
    }

    public Board getBoard() {
        return this.gameBoard;
    }

    public SubBoard getCurrentSubBoard() {
        if(this.nextPlayPosition == null){
            throw new IllegalStateException("There is no current sub-board avaliable");
        }
        return this.gameBoard.getByTilePosition(nextPlayPosition);
    }

    public TilePosition getNextPlayPosition() {
        return this.nextPlayPosition;
    }

    public boolean hasNextPlayPosition() {
        return this.nextPlayPosition != null;
    }
    
    public boolean canMoveToPosition(TilePosition tp){
        return !(this.getBoard().getByTilePosition(tp).getWinner().placed
                || this.getBoard().getByTilePosition(tp).isCatsGame());
    }

    void playPiece(TilePosition tp) {
        SubBoard sb = this.gameBoard.getByTilePosition(this.nextPlayPosition);
        if (sb.getPiece(tp).placed) {
            throw new IllegalArgumentException("Illegal Piece Place");
        }
        sb.setPiece(tp, currentTurn);
        this.currentTurn = this.currentTurn.next();
        this.nextPlayPosition = this.getBoard().getByTilePosition(tp).getWinner().placed
                || this.getBoard().getByTilePosition(tp).isCatsGame() ? null : tp;
    }

    void setNextPlayPosition(TilePosition tp) {
        if (this.nextPlayPosition != null) {
            throw new IllegalStateException("Cannot change next play position");
        }
        if (this.getBoard().getByTilePosition(tp).getWinner().placed
                || this.getBoard().getByTilePosition(tp).isCatsGame()) {
            throw new IllegalArgumentException("invalid next play position");
        }
        this.nextPlayPosition = tp;
    }
}
