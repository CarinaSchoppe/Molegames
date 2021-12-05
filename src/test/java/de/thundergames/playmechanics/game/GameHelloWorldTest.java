package de.thundergames.playmechanics.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHelloWorldTest {

    @Test
    void helloWorldOnce() {
        GameHelloWorld ghw = new GameHelloWorld();
        assertEquals("Hello World", ghw.helloWorldOnce());
    }

    @Test
    void helloWorldTwice() {
    }
}