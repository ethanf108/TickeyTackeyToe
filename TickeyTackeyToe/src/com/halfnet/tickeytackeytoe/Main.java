package com.halfnet.tickeytackeytoe;

import com.halfnet.tickeytackeytoe.ai.EasyAI;
import com.halfnet.tickeytackeytoe.game.AIGame;

public class Main {

    public static void main(String[] args) {
        AIGame a = new AIGame(new EasyAI(), new EasyAI());
    }
}
