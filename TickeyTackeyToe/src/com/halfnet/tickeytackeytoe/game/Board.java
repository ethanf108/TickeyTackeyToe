package com.halfnet.tickeytackeytoe.game;

public class Board {

    private final SubBoard[][] boards = new SubBoard[3][3];
    
    public Board(){
        for(int i = 0; i < 9; i++){
            boards[i/3][i%3] = new SubBoard(TilePosition.getByOrdinal(i));
        }
    }
    
    public SubBoard getByXY(int x, int y){
        return boards[x][y];
    }
    
    public SubBoard getByPosition(int p){
        return boards[p/3][p%3];
    }
    
    public SubBoard getByTilePosition(TilePosition tp){
        return getByPosition(tp.ordinal());
    }
    
    public Piece getPieceByXY(int x, int y){
        return this.getByXY(x/3, y/3).getPiece(x%3, y%3);
    }
}
