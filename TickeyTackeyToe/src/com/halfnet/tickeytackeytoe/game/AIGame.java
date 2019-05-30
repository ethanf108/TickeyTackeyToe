package com.halfnet.tickeytackeytoe.game;

import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import com.halfnet.tickeytackeytoe.graphical.GameButtonRelay;
import com.halfnet.tickeytackeytoe.graphical.MainWindow;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIGame implements GameButtonRelay {

    private final MainWindow mainWindow;
    private final Game game;
    private final CommandSupplier xSupplier;
    private final CommandSupplier oSupplier;
    
    private boolean gameEnd = false;

    private static final long MILLIS_TIMEOUT = 1000;

    public AIGame(CommandSupplier x, CommandSupplier o) {
        this.game = new Game();
        this.mainWindow = new MainWindow(this.game, this);
        this.xSupplier = x;
        this.oSupplier = o;
        this.setNextSubBoard();
    }

    private CommandSupplier getCurrentSupplier() {
        if (this.game.getCurrentTurn() == Piece.X) {
            return this.xSupplier;
        } else {
            return this.oSupplier;
        }
    }

    private void setNextSubBoard() {
        FutureTask<TilePosition> ft = new FutureTask<>(() -> this.getCurrentSupplier().chooseNextSubBoard(this.game));
        try {
            ft.run();
            TilePosition tp = ft.get(MILLIS_TIMEOUT, TimeUnit.MILLISECONDS);
            this.game.setNextPlayPosition(tp);
        } catch (InterruptedException | TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.mainWindow.throwError("Error while evaluating next move. Abort");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.mainWindow.throwError("Exception code thrown by AI");
        } catch (IllegalStateException ex) {
            this.mainWindow.throwError("Cannot change next play tile");
        } catch (IllegalArgumentException ex) {
            this.mainWindow.throwError("Invalid next play tile");
        } finally {
            ft.cancel(true);
        }
    }

    @Override
    public synchronized void pressedNextButton() {
        if (this.gameEnd) {
            return;
        }
        FutureTask<TilePosition> ft = new FutureTask<>(() -> this.getCurrentSupplier().chooseNextPlay(this.game));
        try {
            ft.run();
            TilePosition tp = ft.get(MILLIS_TIMEOUT, TimeUnit.MILLISECONDS);
            this.game.playPiece(tp);
        } catch (InterruptedException | TimeoutException ex) {
            ex.printStackTrace(System.err);
            this.mainWindow.throwError("Error while evaluating next move. Abort");
        } catch (ExecutionException ex) {
            ex.getCause().printStackTrace(System.err);
            this.mainWindow.throwError("Exception code thrown by AI");
        } catch (IllegalArgumentException ex) {
            this.mainWindow.throwError("Piece move invalid");
        } finally {
            ft.cancel(true);
        }
        if(this.game.getBoard().getWinner().placed || this.game.getBoard().isCatsGame()){
            gameEnd = true;
        }else if (!this.game.hasNextPlayPosition()) {
            this.setNextSubBoard();
        }
    }

    @Override
    public void pressedXY(int x, int y) {
    }
}
