package com.halfnet.tickeytackeytoe;

import com.halfnet.tickeytackeytoe.ai.RandomAI;
import com.halfnet.tickeytackeytoe.game.AIGame;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final Random r = new Random();
        AIGame a = new AIGame(new RandomAI(), new RandomAI());
    }
}
