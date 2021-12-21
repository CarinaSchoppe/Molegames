/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 15:22 by Carina Latest changes made by Carina on 21.12.21, 15:21 All contents of "GameUtilTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game; /*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "GameUtilTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameUtilTest {

  private final HashSet<Mole> moles = new HashSet<>();
  private final HashSet<Field> holes = new HashSet<>();
  private GameUtil gameUtil;

  @Mock
  private Player playerMock = mock(Player.class);

  @Mock
  private Game gameMock = mock(Game.class);

  @BeforeEach
  void setUp() {
    var map = new Map(gameMock, holes, holes, 1);
    map.createMap(5);
    gameMock.setMap(map);
    var field = new Field(0, 1);
    var mole = new Mole(playerMock, field);
    moles.add(mole);
    when(playerMock.getMoles()).thenReturn(moles);
    var players = new ArrayList<Player>();
    players.add(playerMock);
    when(gameMock.getMap()).thenReturn(map);
    when(gameMock.getPlayers()).thenReturn(players);
    holes.add(new Field(1, 1));
    var nextFloor = new Map(holes, holes, 1);
    var gameState = new GameState();
    gameState.setFloor(nextFloor);
    gameUtil = new GameUtil(gameMock);
  }

  @Test
  void allHolesFilled() {
    assertFalse(gameUtil.allHolesFilled());
    var field = new Field(1, 1);
    var mole = new Mole(playerMock, field);
    moles.add(mole);
    assertTrue(gameUtil.allHolesFilled());
  }

  @Test
  void allPlayerMolesInHoles() {
    var settings = new Settings(gameMock);
    settings.setNumberOfMoles(2);
    when(gameMock.getSettings()).thenReturn(settings);
    when(gameMock.getCurrentPlayer()).thenReturn(playerMock);
    assertFalse(gameUtil.allPlayerMolesInHoles());
    holes.add(new Field(0, 1));
    var field = new Field(1, 1);
    var mole = new Mole(playerMock, field);
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
