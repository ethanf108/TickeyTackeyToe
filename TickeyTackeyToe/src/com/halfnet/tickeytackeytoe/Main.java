package com.halfnet.tickeytackeytoe;

import com.halfnet.tickeytackeytoe.ai.RandomAI;
import com.halfnet.tickeytackeytoe.game.AIGame;

public class Main {

    public static void main(String[] args) {
        AIGame a = new AIGame(new RandomAI(), new RandomAI());
    }
}
