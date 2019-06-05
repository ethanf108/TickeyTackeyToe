package com.halfnet.tickeytackeytoe.access;

import com.halfnet.tickeytackeytoe.game.GameRef;
import com.halfnet.tickeytackeytoe.game.TilePosition;
import com.halfnet.tickeytackeytoe.graphical.GameButtonRelay;
import com.halfnet.tickeytackeytoe.graphical.MainWindow;

public class PVPGame implements GameButtonRelay {

    private final MainWindow mainWindow;
    private final GameRef game;

    private boolean gameEnd = false;

    public PVPGame() {
        this.game = new GameRef();
        this.mainWindow = new MainWindow(this.game.getGame(), this);
    }

    private void evelateErrorToWindow(String g) {
        if (this.mainWindow != null) {
            this.mainWindow.throwError(g);
        }
    }

    @Override
    public void pressedNextButton() {
    }

    @Override
    public void pressedXY(int x, int y) {
        if (this.gameEnd) {
            return;
        }
        if (this.game.getGame().hasNextPlayPosition()) {
            TilePosition play = TilePosition.getByOrdinal((x % 3) + 3 * (y % 3));
            this.game.playPiece(play);
        } else {
            TilePosition play = TilePosition.getByOrdinal((x / 3) + 3 * (y / 3));
            this.game.setNextPlayPosition(play);
        }
        if (this.game.getGame().getBoard().getWinner().placed || this.game.getGame().getBoard().isCatsGame()) {
            gameEnd = true;
        }
    }

    @Override
    public String[] getInfoText() {
        return new String[]{"Player vs Player"};
    }

    @Override
    public void pressedFinishButton() {
    }
    
    public static void main(String [] args){
        new PVPGame();
    }
}
