package com.halfnet.tickeytackeytoe.ai;

import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import com.halfnet.tickeytackeytoe.game.Game;
import com.halfnet.tickeytackeytoe.game.TilePosition;

public class EasyAI implements CommandSupplier {

    private static final int[] order = new int[]{4, 0, 2, 6, 8, 1, 3, 5, 7};

    @Override
    public TilePosition chooseNextSubBoard(Game game) {
        for (int i : order) {
            if (game.canMoveToPosition(TilePosition.getByOrdinal(i))) {
                return TilePosition.getByOrdinal(i);
            }
        }
        throw new RuntimeException("Cannot choose next sub-board");
    }

    @Override
    public TilePosition chooseNextPlay(Game game) {
        for (int i : order) {
            if (!game.getCurrentSubBoard().getPiece(i).placed) {
                return TilePosition.getByOrdinal(i);
            }
        }
        throw new RuntimeException("Cannot choose next play");
    }
}
