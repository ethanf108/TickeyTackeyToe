package com.halfnet.tickeytackeytoe.graphical;

import com.halfnet.tickeytackeytoe.game.Game;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow extends JFrame{
    
    private final DrawPanel drawPanel;
    private final Game mainGame;
    
    public MainWindow(Game g, GameButtonRelay gbr){
        super("TickeyTackeyToe");
        this.mainGame = g;
        this.drawPanel = new DrawPanel(this.mainGame, gbr);
        this.getContentPane().add(drawPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    public void throwError(String message){
        System.out.println("ERROR: "+message);
        this.getContentPane().remove(this.drawPanel);
        this.getContentPane().add(new JLabel(message));
        this.getContentPane().revalidate();
    }
}
