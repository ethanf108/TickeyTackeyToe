package com.halfnet.tickeytackeytoe.graphical;

import com.halfnet.tickeytackeytoe.game.Game;
import com.halfnet.tickeytackeytoe.game.Piece;
import com.halfnet.tickeytackeytoe.game.TilePosition;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;

final class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final Game game;
    private int mouseX, mouseY;
    private boolean mouseDown;
    private final GameButtonRelay gameButtonRelay;
    
    private int tileX = -1, tileY = -1;

    private final JButton nextMove = new JButton("Next Move");
    private final JButton finishGame = new JButton("Finish Game");

    private static final BufferedImage BOARD_IMG_MAIN;
    private static final BufferedImage BOARD_IMG_SUB;
    private static final Font SMALL = new Font("times new roman", 0, 25);

    static {
        BOARD_IMG_MAIN = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) BOARD_IMG_MAIN.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(200, 0, 5, 600);
        g.fillRect(400, 0, 5, 600);
        g.fillRect(0, 200, 600, 5);
        g.fillRect(0, 400, 600, 5);
        BOARD_IMG_SUB = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) BOARD_IMG_SUB.getGraphics();
        g.setColor(Color.RED.brighter());
        g.fillRect(200 / 3, 0, 2, 200);
        g.fillRect((200 / 3) * 2, 0, 2, 200);
        g.fillRect(0, 200 / 3, 200, 2);
        g.fillRect(0, (200 / 3) * 2, 200, 2);
    }

    public DrawPanel(Game g, GameButtonRelay gbr) {
        super();
        this.game = g;
        this.gameButtonRelay = gbr;
        this.setPreferredSize(new Dimension(1000, 800));
        this.add(this.nextMove);
        this.add(this.finishGame);
        this.setLayout(null);
        this.nextMove.setBounds(10, 630, 100, 30);
        this.finishGame.setBounds(120, 630, 100, 30);
        this.revalidate();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.nextMove.addActionListener(this::nextMoveClick);
        this.finishGame.addActionListener(this::finishClick);
    }

    public void nextMoveClick(ActionEvent e) {
        this.gameButtonRelay.pressedNextButton();
        this.repaint();
    }

    public void finishClick(ActionEvent e) {
        this.gameButtonRelay.pressedFinishButton();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1000, 800);
        g.setStroke(new BasicStroke(7));
        g.setColor(Color.BLUE);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Piece p = this.game.getBoard().getByXY(i, j).getWinner();
                switch (p) {
                    case X:
                        g.drawLine(i * 200, j * 200, (i + 1) * 200, (j + 1) * 200);
                        g.drawLine((i + 1) * 200, j * 200, i * 200, (j + 1) * 200);
                        break;
                    case O:
                        g.drawOval(7 + i * 200, 7 + j * 200, 190, 190);
                        break;
                    case None:
                        if (TilePosition.getByOrdinal(j * 3 + i) == this.game.getNextPlayPosition()) {
                            g.setColor(Color.GREEN.brighter().brighter());
                            g.fillRect(i * 200, j * 200, 200, 200);
                            g.setColor(Color.BLUE);
                        }
                        g.drawImage(BOARD_IMG_SUB, null, i * 200, j * 200);
                }
            }
        }
        g.drawImage(BOARD_IMG_MAIN, null, 0, 0);
        g.setColor(Color.BLUE);
        g.setFont(SMALL);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                    if (mouseX <= 600 && mouseY <= 600
                            && mouseX > (600.0/9) * i && mouseX < (600.0/9) * (i + 1)
                            && mouseY > (600.0/9) * j && mouseY < (600.0/9) * (j + 1)) {
                        this.tileX = i;
                        this.tileY = j;
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect((600 / 9) * i + i, (600 / 9) * j + j,
                                   (600 / 9) , (600 / 9));
                        g.setColor(Color.BLUE);
                    }
                Piece p = this.game.getBoard().getPieceByXY(i, j);
                if (p.placed && !this.game.getBoard().getByXY(i / 3, j / 3).getWinner().placed) {
                    g.drawString(p.toString(), 25 + (600 / 9) * i, 40 + (600 / 9) * j);
                }
            }
        }
        g.setColor(Color.BLACK);
        Piece win = this.game.getBoard().getWinner();
        if (win.placed) {
            this.nextMove.setEnabled(false);
            this.finishGame.setEnabled(false);
            g.drawString(win.toString() + " Won!", 650, 50);
        } else if (game.getBoard().isCatsGame()) {
            this.finishGame.setEnabled(false);
            this.nextMove.setEnabled(false);
            g.drawString("Cat's game.", 650, 50);
        } else if(this.game.hasNextPlayPosition()){
            g.drawString(this.game.getCurrentTurn() + "'s turn", 650, 50);
        } else {
            g.drawString(this.game.getCurrentTurn() + ", select next play position", 650, 50);
        }
        String[] gbrt = this.gameButtonRelay.getInfoText();
        for (int i = 0; i < gbrt.length; i++) {
            g.drawString(gbrt[i], 650, 100 + 50 * i);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseDown = true;
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseDown = false;
        if(tileX > -1 && tileY > -1){
            this.gameButtonRelay.pressedXY(tileX, tileY);
        }
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        repaint();
    }
}
