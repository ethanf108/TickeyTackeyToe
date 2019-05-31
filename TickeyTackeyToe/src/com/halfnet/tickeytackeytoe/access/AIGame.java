package com.halfnet.tickeytackeytoe.access;

import com.halfnet.tickeytackeytoe.game.GameRef;
import com.halfnet.tickeytackeytoe.game.Piece;
import com.halfnet.tickeytackeytoe.game.TilePosition;
import com.halfnet.tickeytackeytoe.graphical.GameButtonRelay;
import com.halfnet.tickeytackeytoe.graphical.MainWindow;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIGame implements GameButtonRelay {

    private final MainWindow mainWindow;
    private final GameRef game;
    private final CommandSupplier xSupplier;
    private final CommandSupplier oSupplier;

    private boolean gameEnd = false;

    private static final long MILLIS_TIMEOUT = 1;

    public AIGame(CommandSupplier x, CommandSupplier o) {
        this.game = new GameRef();
        this.mainWindow = new MainWindow(this.game.getGame(), this);
        this.xSupplier = x;
        this.oSupplier = o;
        this.setNextSubBoard();
    }

    private AIGame(CommandSupplier x, CommandSupplier o, GameRef g) {
        this.xSupplier = x;
        this.oSupplier = o;
        this.game = g;
        this.mainWindow = null;
        this.setNextSubBoard();
    }

    private CommandSupplier getCurrentSupplier() {
        if (this.game.getGame().getCurrentTurn() == Piece.X) {
            return this.xSupplier;
        } else {
            return this.oSupplier;
        }
    }

    @Override
    public String[] getInfoText() {
        return new String[]{
            xSupplier.getClass().getSimpleName() + " vs " + oSupplier.getClass().getSimpleName()
        };
    }

    private void evelateErrorToWindow(String g) {
        if (this.mainWindow != null) {
            this.mainWindow.throwError(g);
        }
    }

    private void setNextSubBoard() {
        FutureTask<TilePosition> ft = new FutureTask<>(() -> this.getCurrentSupplier().chooseNextSubBoard(this.game.getGame()));
        try {
            ft.run();
            TilePosition tp = ft.get(1, TimeUnit.MICROSECONDS);
            this.game.setNextPlayPosition(tp);
        } catch (InterruptedException | TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Error while evaluating next move. Abort");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.evelateErrorToWindow("Exception thrown in AI code");
        } catch (IllegalStateException ex) {
            this.evelateErrorToWindow("Cannot change next play tile");
        } catch (IllegalArgumentException ex) {
            this.evelateErrorToWindow("Invalid next play tile");
        } finally {
            ft.cancel(true);
        }
    }

    @Override
    public synchronized void pressedNextButton() {
        if (this.gameEnd) {
            return;
        }
        FutureTask<TilePosition> ft = new FutureTask<>(() -> this.getCurrentSupplier().chooseNextPlay(this.game.getGame()));
        try {
            ft.run();
            TilePosition tp = ft.get(1, TimeUnit.MICROSECONDS);
            this.game.playPiece(tp);
        } catch (InterruptedException | TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Error while evaluating next move. Abort");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.evelateErrorToWindow("Exception thrown in AI code");
        } catch (IllegalArgumentException ex) {
            this.evelateErrorToWindow("Piece move invalid");
        } finally {
            ft.cancel(true);
        }
        if (this.game.getGame().getBoard().getWinner().placed || this.game.getGame().getBoard().isCatsGame()) {
            gameEnd = true;
        } else if (!this.game.getGame().hasNextPlayPosition()) {
            this.setNextSubBoard();
        }
    }

    @Override
    public void pressedXY(int x, int y) {
    }

    @Override
    public void pressedFinishButton() {
        while (!gameEnd) {
            this.pressedNextButton();
        }
    }

    public static int quickRun(CommandSupplier x, CommandSupplier o) {
        GameRef g = new GameRef();
        AIGame ai = new AIGame(x, o, g);
        ai.pressedFinishButton();
        return ai.game.getGame().getBoard().isCatsGame() ? 1 : ai.game.getGame().getBoard().getWinner() == Piece.X ? 2 : 0;
    }
}
