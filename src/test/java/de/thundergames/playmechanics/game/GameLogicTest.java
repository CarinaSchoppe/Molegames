package de.thundergames.playmechanics.game;

import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkFloor;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameLogicTest {

    private Map map;

    @Mock
    private Player playerMock = mock(Player.class);

    @BeforeEach
    void setUp() {
        Game game = new Game(1);
        map = new Map(game);
        map.createMap(2);
        game.setMap(map);
    }

    @Test
    void wasLegalMove() {
        assertFalse(GameLogic.wasLegalMove(new int[]{5,5}, new int[]{6,5},1, map));
        assertFalse(GameLogic.wasLegalMove(new int[]{3,3}, new int[]{6,6},1, map));
        assertFalse(GameLogic.wasLegalMove(new int[]{3,3}, new int[]{3,3},1, map));
        assertFalse(GameLogic.wasLegalMove(new int[]{5,5}, new int[]{3,3},2, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{3,4}, new int[]{3,1},4, map));
        assertFalse(GameLogic.wasLegalMove(new int[]{4,3}, new int[]{1,3},4, map));

        Field fieldMole = map.getFieldMap().get(List.of(1, 1));
        Field fieldHole = map.getFieldMap().get(List.of(1, 2));
        Field fieldDrawAgain = map.getFieldMap().get(List.of(0, 0));

        Mole mole = new Mole(playerMock, fieldMole);
        fieldMole.setMole(mole);

        ArrayList<NetworkMole> moles = new ArrayList<>();
        ArrayList<NetworkField> holes = new ArrayList<>();
        ArrayList<NetworkField> drawAgainFields = new ArrayList<>();

        moles.add(mole);
        holes.add(fieldHole);
        drawAgainFields.add(fieldDrawAgain);

        NetworkFloor floor = new NetworkFloor();
        floor.setHoles(holes);
        floor.setDrawAgainFields(drawAgainFields);

        GameState gameState = new GameState();
        gameState.setPlacedMoles(moles);
        gameState.setFloor(floor);

        map.changeFieldParams(gameState);

        assertFalse(GameLogic.wasLegalMove(new int[]{0,0}, new int[]{1,1},1, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{1,0}, new int[]{1,2},2, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{2,0}, new int[]{2,2},2, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{1,2}, new int[]{1,0},2, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{2,2}, new int[]{2,0},2, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{0,1}, new int[]{3,1},3, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{0,2}, new int[]{3,2},3, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{3,1}, new int[]{0,1},3, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{0,0}, new int[]{3,3},3, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{1,1}, new int[]{4,4},3, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{2,0}, new int[]{0,2},2, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{3,1}, new int[]{1,3},2, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{0,2}, new int[]{2,0},2, map));
        assertTrue(GameLogic.wasLegalMove(new int[]{1,3}, new int[]{3,1},2, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{3,3}, new int[]{0,0},3, map));

        assertFalse(GameLogic.wasLegalMove(new int[]{1,3}, new int[]{2,4},2, map));

    }

    @Test
    void checkWinning() {

    }
}