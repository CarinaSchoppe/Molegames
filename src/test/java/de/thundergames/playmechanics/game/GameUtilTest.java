/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 08.01.22, 10:59 by Carina Latest changes made by Carina on 08.01.22, 10:35 All contents of "GameUtilTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameUtilTest {

  private final HashSet<Mole> moles = new HashSet<>();
  private final HashSet<Field> holes = new HashSet<>();
  private GameUtil gameUtil;
  private Game game;

  @Mock
  private Player playerMock = mock(Player.class);

  @BeforeEach
  void setUp() {
    game = new Game(1);
    game.setRadius(5);
    holes.add(new Field(1, 1));
    holes.add(new Field(1, 2));
    var map = new Map(game, holes, holes, 1);
    var gameState = new GameState();
    gameState.setRadius(5);
    gameState.setFloor(map);
    map.build(gameState);
    game.setMap(map);
    var field = new Field(0, 1);
    var mole = new Mole(playerMock, field);
    moles.add(mole);
    when(playerMock.getMoles()).thenReturn(moles);
    holes.add(new Field(1, 1));
    gameUtil = new GameUtil(game);
  }

  @Test
  void allHolesFilled() {
    assertFalse(gameUtil.allHolesFilled());
    var field = new Field(1, 1);
    var mole = new Mole(playerMock, field);
    var mole2 = new Mole(playerMock, field);
    moles.add(mole);
    game.getMap().getFieldMap().get(List.of(1, 1)).setOccupied(true);
    game.getMap().getFieldMap().get(List.of(1, 2)).setOccupied(true);
    game.getMap().getFieldMap().get(List.of(1, 1)).setMole(mole);
    game.getMap().getFieldMap().get(List.of(1, 2)).setMole(mole2);
    assertTrue(gameUtil.allHolesFilled());
  }

  @Test
  void allPlayerMolesInHoles() {
    var settings = new Settings(game);
    settings.setNumberOfMoles(2);
    game.setCurrentPlayer(playerMock);
    assertFalse(gameUtil.allPlayerMolesInHoles());
    var field = new Field(1, 1);
    var mole = new Mole(playerMock, field);
    moles.add(mole);
    playerMock.getMoles().clear();
    playerMock.getMoles().add(mole);
    game.getMap().getFieldMap().get(List.of(1, 1)).setOccupied(true);
    game.getMap().getFieldMap().get(List.of(1, 1)).setMole(mole);
    assertTrue(gameUtil.allPlayerMolesInHoles());
  }

  @Test
  void nextPlayer() {
    // TODO: Implement
  }

  @Test
  void nextFloor() {
    // TODO: Implement
  }

  @Test
  void givePoints() {
    // TODO: Implement
  }
}
