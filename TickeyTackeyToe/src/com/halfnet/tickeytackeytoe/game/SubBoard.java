package com.halfnet.tickeytackeytoe.game;

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

    public Piece getWinner() {

        if (state[0][0] == state[0][1] && state[0][1] == state[0][2] && state[0][0].placed) {
            return state[0][0];
        }

        if (state[1][0] == state[1][1] && state[1][1] == state[1][2] && state[1][0].placed) {
            return state[1][0];
        }

        if (state[2][0] == state[2][1] && state[2][1] == state[2][2] && state[2][0].placed) {
            return state[2][0];
        }

        if (state[0][0] == state[1][0] && state[1][0] == state[2][0] && state[0][0].placed) {
            return state[0][0];
        }

        if (state[0][1] == state[1][0] && state[1][0] == state[2][0] && state[0][1].placed) {
            return state[0][1];
        }

        if (state[0][2] == state[1][2] && state[1][2] == state[2][2] && state[0][2].placed) {
            return state[0][2];
        }
        /*
        for (int i = 0; i < 3; i++) {
            if (state[i][0] == state[i][1] && state[i][1] == state[i][2] && state[i][0].placed) {
                return state[i][0];
            }
            if (state[0][i] == state[1][i] && state[1][i] == state[2][i] && state[0][i].placed) {
                return state[0][1];
            }
        }
         */
        if (state[0][0] == state[1][1] && state[1][1] == state[2][2] && state[0][0].placed) {
            return state[0][0];
        }
        if (state[0][2] == state[1][1] && state[1][1] == state[2][0] && state[0][2].placed) {
            return state[0][2];
        }
        return Piece.None;
    }
}
