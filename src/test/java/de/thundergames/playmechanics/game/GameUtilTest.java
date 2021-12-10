package de.thundergames.playmechanics.game;

import de.thundergames.networking.util.interfaceItems.NetworkConfiguration;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkFloor;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameUtilTest {

    private final HashSet<Mole> moles = new HashSet<>();
    private final ArrayList<NetworkField> holes = new ArrayList<>();
    private GameUtil gameUtil;

    @Mock
    private Player playerMock = mock(Player.class);

    @Mock
    private Game gameMock = mock(Game.class);

    @BeforeEach
    void setUp() {
        Map map = new Map(gameMock);
        map.createMap(5);
        gameMock.setMap(map);
        
        Field field = new Field(Arrays.asList(0, 1));
        Mole mole = new Mole(playerMock, field);
        moles.add(mole);
        when(playerMock.getMoles()).thenReturn(moles);

        ArrayList<Player> players = new ArrayList<>();
        players.add(playerMock);

        when(gameMock.getMap()).thenReturn(map);
        when(gameMock.getPlayers()).thenReturn(players);

        holes.add(new NetworkField(1,1));
        NetworkFloor netFloor = new NetworkFloor();
        netFloor.setHoles(holes);

        GameState gameState = new GameState();
        gameState.setFloor(netFloor);

        gameUtil = new GameUtil(gameMock);

        map.setHoles(gameState.getFloor().getHoles());
    }

    @Test
    void allHolesFilled() {
        assertFalse(gameUtil.allHolesFilled());

        Field field = new Field(List.of(1, 1));
        Mole mole = new Mole(playerMock, field);
        moles.add(mole);

        assertTrue(gameUtil.allHolesFilled());
    }

    @Test
    void allPlayerMolesInHoles() {
        NetworkConfiguration netConf = new NetworkConfiguration();
        netConf.setNumberOfMoles(2);

        Settings settings = new Settings(gameMock);
        settings.updateConfiuration((netConf));

        when(gameMock.getSettings()).thenReturn(settings);
        when(gameMock.getCurrentPlayer()).thenReturn(playerMock);

        assertFalse(gameUtil.allPlayerMolesInHoles());

        holes.add(new NetworkField(0,1));

        Field field = new Field(List.of(1, 1));
        Mole mole = new Mole(playerMock, field);
        moles.add(mole);

        assertTrue(gameUtil.allPlayerMolesInHoles());
    }

    @Test
    void nextPlayer() {

    }

    @Test
    void nextFloor() {

    }

    @Test
    void givePoints() {

    }
}