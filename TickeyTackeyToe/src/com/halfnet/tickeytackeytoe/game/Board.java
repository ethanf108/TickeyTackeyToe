package com.halfnet.tickeytackeytoe.game;

import java.util.Arrays;

public class Board {

    private final SubBoard[][] boards = new SubBoard[3][3];

    public Board() {
        for (int i = 0; i < 9; i++) {
            boards[i / 3][i % 3] = new SubBoard(TilePosition.getByOrdinal(i));
        }
    }

    public SubBoard getByXY(int x, int y) {
        return boards[x][y];
    }

    public SubBoard getByPosition(int p) {
        return boards[p / 3][p % 3];
    }

    public SubBoard getByTilePosition(TilePosition tp) {
        return getByPosition(tp.ordinal());
    }

    public Piece getPieceByXY(int x, int y) {
        return this.getByXY(x / 3, y / 3).getPiece(x % 3, y % 3);
    }
    
    private Piece[][] genTempState(){
        Piece[][] state = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state[i][j] = getByXY(i, j).getWinner();
            }
        }
        return state;
    }
    
    public boolean isCatsGame(){
        return !getWinner().placed && Arrays.stream(this.genTempState()).flatMap(n->Arrays.stream(n)).allMatch(n->n.placed);
    }

    public Piece getWinner() {
        Piece[][] state = this.genTempState();
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
