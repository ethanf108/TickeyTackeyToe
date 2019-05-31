package com.halfnet.tickeytackeytoe.access;

import com.halfnet.tickeytackeytoe.game.GameRef;
import com.halfnet.tickeytackeytoe.game.Piece;
import com.halfnet.tickeytackeytoe.game.TilePosition;
import com.halfnet.tickeytackeytoe.graphical.GameButtonRelay;
import com.halfnet.tickeytackeytoe.graphical.MainWindow;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIGame implements GameButtonRelay {

    private final MainWindow mainWindow;
    private final GameRef game;
    private final CommandSupplier xSupplier;
    private final CommandSupplier oSupplier;

    private final ExecutorService commandRunner;

    private boolean gameEnd = false;

    private static final long MILLIS_TIMEOUT = 250;

    public AIGame(CommandSupplier x, CommandSupplier o) {
        this.game = new GameRef();
        this.mainWindow = new MainWindow(this.game.getGame(), this);
        this.xSupplier = x;
        this.oSupplier = o;
        this.commandRunner = Executors.newSingleThreadExecutor();
        this.setNextSubBoard();
    }

    private AIGame(CommandSupplier x, CommandSupplier o, GameRef g) {
        this.xSupplier = x;
        this.oSupplier = o;
        this.game = g;
        this.mainWindow = null;
        this.commandRunner = Executors.newSingleThreadExecutor();
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
        Future<TilePosition> fut = this.commandRunner.submit(() -> this.getCurrentSupplier().chooseNextSubBoard(this.game.getGame()));
        try {
            TilePosition tp = fut.get(MILLIS_TIMEOUT, TimeUnit.MILLISECONDS);
            this.game.setNextPlayPosition(tp);
        } catch (TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("AI Used too much time. "
                    + this.game.getGame().getCurrentTurn().next() + " wins.");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.evelateErrorToWindow("Exception thrown in AI code");
        } catch (IllegalStateException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Cannot change next play tile");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Invalid next play tile");
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Interrupted while executing code.");
        } finally {
            fut.cancel(true);
        }
    }

    @Override
    public synchronized void pressedNextButton() {
        if (this.gameEnd) {
            return;
        }
        Future<TilePosition> fut = this.commandRunner.submit(() -> this.getCurrentSupplier().chooseNextPlay(this.game.getGame()));
        try {
            TilePosition tp = fut.get(MILLIS_TIMEOUT, TimeUnit.MILLISECONDS);
            this.game.playPiece(tp);
        } catch (TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("AI Used too much time. "
                    + this.game.getGame().getCurrentTurn().next() + " wins.");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.evelateErrorToWindow("Exception thrown in AI code");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Piece move invalid");
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            this.evelateErrorToWindow("Interrupted while executing code.");
        } finally {
            fut.cancel(true);
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
