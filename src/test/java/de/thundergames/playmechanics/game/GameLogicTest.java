/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 20.12.21, 16:51 by Carina Latest changes made by Carina on 20.12.21, 16:50 All contents of "GameLogicTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GameLogicTest {

  private Map map;

  @Mock
  private Player playerMock = mock(Player.class);

  @BeforeEach
  void setUp() {
    var game = new Game(1);
    game.setRadius(2);
    map = new Map(game, new HashSet<>(), new HashSet<>(), 1);
    map.build(game);
    game.setMap(map);
  }

  @Test
  void wasLegalMove() {
    assertFalse(GameLogic.wasLegalMove(new int[]{5, 5}, new int[]{6, 5}, 1, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{3, 3}, new int[]{6, 6}, 1, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{3, 3}, new int[]{3, 3}, 1, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{5, 5}, new int[]{3, 3}, 2, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{3, 4}, new int[]{3, 1}, 4, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{4, 3}, new int[]{1, 3}, 4, map));
    var fieldMole = map.getFieldMap().get(List.of(1, 1));
    var fieldHole = map.getFieldMap().get(List.of(1, 2));
    var fieldDrawAgain = map.getFieldMap().get(List.of(0, 0));
    var mole = new Mole(playerMock, fieldMole);
    fieldMole.setMole(mole);
    var moles = new ArrayList<Mole>();
    var holes = new HashSet<Field>();
    var drawAgainFields = new HashSet<Field>();
    moles.add(mole);
    holes.add(fieldHole);
    drawAgainFields.add(fieldDrawAgain);
    var floor = new Map(holes, drawAgainFields, 1);
    var gameState = new GameState();
    gameState.setPlacedMoles(moles);
    gameState.setFloor(floor);
    map.changeFieldParams(gameState);
    assertFalse(GameLogic.wasLegalMove(new int[]{0, 0}, new int[]{1, 1}, 1, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{1, 0}, new int[]{1, 2}, 2, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{2, 0}, new int[]{2, 2}, 2, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{1, 2}, new int[]{1, 0}, 2, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{2, 2}, new int[]{2, 0}, 2, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{0, 1}, new int[]{3, 1}, 3, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{0, 2}, new int[]{3, 2}, 3, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{3, 1}, new int[]{0, 1}, 3, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{0, 0}, new int[]{3, 3}, 3, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{1, 1}, new int[]{4, 4}, 3, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{2, 0}, new int[]{0, 2}, 2, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{3, 1}, new int[]{1, 3}, 2, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{0, 2}, new int[]{2, 0}, 2, map));
    assertTrue(GameLogic.wasLegalMove(new int[]{1, 3}, new int[]{3, 1}, 2, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{3, 3}, new int[]{0, 0}, 3, map));
    assertFalse(GameLogic.wasLegalMove(new int[]{1, 3}, new int[]{2, 4}, 2, map));
  }

  @Test
  void checkWinning() {
  }
}
