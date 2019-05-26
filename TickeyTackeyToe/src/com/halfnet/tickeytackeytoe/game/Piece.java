package com.halfnet.tickeytackeytoe.game;

public enum Piece {
    X, O, None(false);

    public final boolean placed;

    private Piece() {
        this.placed = true;
    }

    private Piece(boolean placed) {
        this.placed = placed;
    }
    
    public Piece next(){
        switch(this){
            case X:
                return O;
            case O:
                return X;
            default:
                throw new IllegalArgumentException("`None` does not have a next piece");
        }
    }
}
