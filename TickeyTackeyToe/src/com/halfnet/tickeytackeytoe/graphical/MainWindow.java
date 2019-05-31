package com.halfnet.tickeytackeytoe.graphical;

import com.halfnet.tickeytackeytoe.game.Game;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class MainWindow extends JFrame {

    private final DrawPanel drawPanel;
    private final Game mainGame;

    static{
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Unable to set look and feel");
            ex.printStackTrace(System.err);
        }
    }
    public MainWindow(Game g, GameButtonRelay gbr) {
        super("TickeyTackeyToe");
        this.mainGame = g;
        this.drawPanel = new DrawPanel(this.mainGame, gbr);
        this.getContentPane().add(drawPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void throwError(String message) {
        System.err.println("ERROR: " + message);
        this.getContentPane().remove(this.drawPanel);
        this.getContentPane().add(new JLabel(message));
        this.getContentPane().revalidate();
    }
}
