package com.halfnet.tickeytackeytoe.access;

import com.halfnet.tickeytackeytoe.game.Game;
import com.halfnet.tickeytackeytoe.game.TilePosition;

public interface CommandSupplier {

    /**
     * Only called if the player can pick the next sub-board to place in, for
     * example if they were chosen to play in the Top Left sub-board, but that
     * sub-board was already won.
     *
     * @param gameBoard the current state of the game
     * @return the Tile Position to move to
     */
    public TilePosition chooseNextSubBoard(Game gameBoard);

    public TilePosition chooseNextPlay(Game gameBoard);
}
