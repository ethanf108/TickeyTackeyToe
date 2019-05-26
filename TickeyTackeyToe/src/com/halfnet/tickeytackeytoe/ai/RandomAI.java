package com.halfnet.tickeytackeytoe.ai;

import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import com.halfnet.tickeytackeytoe.game.Game;
import com.halfnet.tickeytackeytoe.game.TilePosition;
import java.util.Random;

public class RandomAI implements CommandSupplier {

    private final Random rand = new Random();

    @Override
    public TilePosition chooseNextSubBoard(Game game) {
        TilePosition tp = TilePosition.getByOrdinal(rand.nextInt(9));
        while (game.getBoard().getByTilePosition(tp).getWinner().placed) {
            tp = TilePosition.getByOrdinal(rand.nextInt(9));
        }
        return tp;
    }

    @Override
    public TilePosition chooseNextPlay(Game game) {
        TilePosition tp = TilePosition.getByOrdinal(rand.nextInt(9));
        while (game.getCurrentSubBoard().getPiece(tp).placed) {
            tp = TilePosition.getByOrdinal(rand.nextInt(9));
        }
        return tp;
    }

}
