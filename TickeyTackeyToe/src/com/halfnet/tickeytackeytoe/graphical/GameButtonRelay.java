package com.halfnet.tickeytackeytoe.graphical;

public interface GameButtonRelay {

    public void pressedNextButton();

    public void pressedXY(int x, int y);
    
    public String[] getInfoText();
    
    public void pressedFinishButton();
}
