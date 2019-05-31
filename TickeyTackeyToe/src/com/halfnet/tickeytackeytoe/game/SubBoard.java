package com.halfnet.tickeytackeytoe.game;

import java.util.ArrayList;

public class SubBoard {

    private final TilePosition position;
    private final Piece[][] state = new Piece[3][3];

    public SubBoard(TilePosition tp) {
        this.position = tp;
        for (int i = 0; i < 9; i++) {
            this.state[i / 3][i % 3] = Piece.None;
        }
    }

    void setPiece(int x, int y, Piece p) {
        state[x][y] = p;
    }

    void setPiece(int ord, Piece p) {
        this.state[ord / 3][ord % 3] = p;
    }

    void setPiece(TilePosition tp, Piece p) {
        this.setPiece(tp.ordinal(), p);
    }

    public TilePosition getPosition() {
        return this.position;
    }

    public Piece getPiece(int x, int y) {
        return this.state[x][y];
    }

    public Piece getPiece(int x) {
        return this.state[x / 3][x % 3];
    }

    public Piece getPiece(TilePosition tp) {
        return this.getPiece(tp.ordinal());
    }
    
    /**
     * Helper method, used to find all tiles that can be winners for a given piece
     * @param p the piece to calculate winners for
     * @return an array of TilePositions, may be empty
     */
    public TilePosition[] getPossibleWinners(Piece p){
        ArrayList<TilePosition> ret = new ArrayList<>(9);
        for(TilePosition tp : TilePosition.values()){
            if(this.getPiece(tp).placed)continue;
            state[tp.ordinal()/3][tp.ordinal()%3] = p;
            if(getWinner() == p)ret.add(tp);
        }
        return ret.toArray(new TilePosition[ret.size()]);
    }

    /**
     * useful to iterate through all pieces of a sub-board
     *
     * @return a 2-D array representing the pieces of this sub-board
     */
    public Piece[][] getPieces() {
        Piece[][] ret = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ret[i][j] = this.state[i][j];
            }
        }
        return ret;
    }
    
    public boolean isCatsGame(){
        for(Piece[] tp : state){
            for(Piece p : tp){
                if(!p.placed)return false;
            }
        }
        return !getWinner().placed;
    }

    public Piece getWinner() {
        for (int i = 0; i < 3; i++) {
            if (state[i][0] == state[i][1] && state[i][1] == state[i][2] && state[i][0].placed) {
                return state[i][0];
            }
            if (state[0][i] == state[1][i] && state[1][i] == state[2][i] && state[0][i].placed) {
                return state[0][i];
            }
        }
        if (state[0][0] == state[1][1] && state[1][1] == state[2][2] && state[0][0].placed) {
            return state[0][0];
        }
        if (state[0][2] == state[1][1] && state[1][1] == state[2][0] && state[0][2].placed) {
            return state[0][2];
        }
        return Piece.None;
    }
}
