/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "MapTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.map;/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "MapTest" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
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

class MapTest {

  private Game game;

  @Mock
  private Player playerMock = mock(Player.class);

  @BeforeEach
  void setUp() {
    game = new Game(1);
  }

  @Test
  void createMapRadiusTwo() {
    game.setRadius(2);
    var map = new Map(game, new HashSet<>(), new HashSet<>(), 1);
    map.build(game);
    assertTrue(map.existField(0, 0));
    assertTrue(map.existField(1, 0));
    assertTrue(map.existField(2, 0));
    assertFalse(map.existField(3, 0));
    assertFalse(map.existField(4, 0));
    assertTrue(map.existField(0, 1));
    assertTrue(map.existField(1, 1));
    assertTrue(map.existField(2, 1));
    assertTrue(map.existField(3, 1));
    assertFalse(map.existField(4, 1));
    assertTrue(map.existField(0, 2));
    assertTrue(map.existField(1, 2));
    assertTrue(map.existField(2, 2));
    assertTrue(map.existField(3, 2));
    assertTrue(map.existField(4, 2));
    assertFalse(map.existField(5, 2));
    assertFalse(map.existField(0, 3));
    assertTrue(map.existField(1, 3));
    assertTrue(map.existField(2, 3));
    assertTrue(map.existField(3, 3));
    assertTrue(map.existField(4, 3));
    assertFalse(map.existField(0, 3));
    assertFalse(map.existField(0, 4));
    assertFalse(map.existField(1, 4));
    assertTrue(map.existField(2, 4));
    assertTrue(map.existField(3, 4));
    assertTrue(map.existField(4, 4));
    assertFalse(map.existField(1, 4));
    assertFalse(map.existField(5, 4));
    assertFalse(map.existField(0, 5));
    assertFalse(map.existField(1, 5));
    assertFalse(map.existField(2, 5));
    assertFalse(map.existField(3, 5));
    assertFalse(map.existField(4, 5));
    assertFalse(map.existField(5, 5));
  }

  @Test
  void createMapRadiusFive() {
    game.setRadius(5);
    var map = new Map(game, new HashSet<>(), new HashSet<>(), 1);
    map.build(game);
    assertTrue(map.existField(0, 0));
    assertTrue(map.existField(1, 0));
    assertTrue(map.existField(2, 0));
    assertTrue(map.existField(3, 0));
    assertTrue(map.existField(4, 0));
    assertTrue(map.existField(5, 0));
    assertFalse(map.existField(6, 0));
    assertFalse(map.existField(7, 0));
    assertFalse(map.existField(8, 0));
    assertFalse(map.existField(9, 0));
    assertFalse(map.existField(10, 0));
    assertTrue(map.existField(0, 1));
    assertTrue(map.existField(1, 1));
    assertTrue(map.existField(2, 1));
    assertTrue(map.existField(3, 1));
    assertTrue(map.existField(4, 1));
    assertTrue(map.existField(5, 1));
    assertTrue(map.existField(6, 1));
    assertFalse(map.existField(7, 1));
    assertFalse(map.existField(8, 1));
    assertFalse(map.existField(9, 1));
    assertFalse(map.existField(10, 1));
    assertTrue(map.existField(0, 2));
    assertTrue(map.existField(1, 2));
    assertTrue(map.existField(2, 2));
    assertTrue(map.existField(3, 2));
    assertTrue(map.existField(4, 2));
    assertTrue(map.existField(5, 2));
    assertTrue(map.existField(6, 2));
    assertTrue(map.existField(7, 2));
    assertFalse(map.existField(8, 2));
    assertFalse(map.existField(9, 2));
    assertFalse(map.existField(10, 2));
    assertTrue(map.existField(0, 3));
    assertTrue(map.existField(1, 3));
    assertTrue(map.existField(2, 3));
    assertTrue(map.existField(3, 3));
    assertTrue(map.existField(4, 3));
    assertTrue(map.existField(5, 3));
    assertTrue(map.existField(6, 3));
    assertTrue(map.existField(7, 3));
    assertTrue(map.existField(8, 3));
    assertFalse(map.existField(9, 3));
    assertFalse(map.existField(10, 3));
    assertTrue(map.existField(0, 4));
    assertTrue(map.existField(1, 4));
    assertTrue(map.existField(2, 4));
    assertTrue(map.existField(3, 4));
    assertTrue(map.existField(4, 4));
    assertTrue(map.existField(5, 4));
    assertTrue(map.existField(6, 4));
    assertTrue(map.existField(7, 4));
    assertTrue(map.existField(8, 4));
    assertTrue(map.existField(9, 4));
    assertFalse(map.existField(10, 4));
    assertFalse(map.existField(11, 4));
    assertTrue(map.existField(0, 5));
    assertTrue(map.existField(1, 5));
    assertTrue(map.existField(2, 5));
    assertTrue(map.existField(3, 5));
    assertTrue(map.existField(4, 5));
    assertTrue(map.existField(5, 5));
    assertTrue(map.existField(6, 5));
    assertTrue(map.existField(7, 5));
    assertTrue(map.existField(8, 5));
    assertTrue(map.existField(9, 5));
    assertTrue(map.existField(10, 5));
    assertFalse(map.existField(11, 5));
    assertFalse(map.existField(0, 6));
    assertTrue(map.existField(1, 6));
    assertTrue(map.existField(2, 6));
    assertTrue(map.existField(3, 6));
    assertTrue(map.existField(4, 6));
    assertTrue(map.existField(5, 6));
    assertTrue(map.existField(6, 6));
    assertTrue(map.existField(7, 6));
    assertTrue(map.existField(8, 6));
    assertTrue(map.existField(9, 6));
    assertTrue(map.existField(10, 6));
    assertFalse(map.existField(11, 6));
    assertFalse(map.existField(0, 7));
    assertFalse(map.existField(1, 7));
    assertTrue(map.existField(2, 7));
    assertTrue(map.existField(3, 7));
    assertTrue(map.existField(4, 7));
    assertTrue(map.existField(5, 7));
    assertTrue(map.existField(6, 7));
    assertTrue(map.existField(7, 7));
    assertTrue(map.existField(8, 7));
    assertTrue(map.existField(9, 7));
    assertTrue(map.existField(10, 7));
    assertFalse(map.existField(11, 7));
    assertFalse(map.existField(0, 8));
    assertFalse(map.existField(1, 8));
    assertFalse(map.existField(2, 8));
    assertTrue(map.existField(3, 8));
    assertTrue(map.existField(4, 8));
    assertTrue(map.existField(5, 8));
    assertTrue(map.existField(6, 8));
    assertTrue(map.existField(7, 8));
    assertTrue(map.existField(8, 8));
    assertTrue(map.existField(9, 8));
    assertTrue(map.existField(10, 8));
    assertFalse(map.existField(11, 8));
    assertFalse(map.existField(0, 9));
    assertFalse(map.existField(1, 9));
    assertFalse(map.existField(2, 9));
    assertFalse(map.existField(3, 9));
    assertTrue(map.existField(4, 9));
    assertTrue(map.existField(5, 9));
    assertTrue(map.existField(6, 9));
    assertTrue(map.existField(7, 9));
    assertTrue(map.existField(8, 9));
    assertTrue(map.existField(9, 9));
    assertTrue(map.existField(10, 9));
    assertFalse(map.existField(11, 9));
    assertFalse(map.existField(0, 10));
    assertFalse(map.existField(1, 10));
    assertFalse(map.existField(2, 10));
    assertFalse(map.existField(3, 10));
    assertFalse(map.existField(4, 10));
    assertTrue(map.existField(5, 10));
    assertTrue(map.existField(6, 10));
    assertTrue(map.existField(7, 10));
    assertTrue(map.existField(8, 10));
    assertTrue(map.existField(9, 10));
    assertTrue(map.existField(10, 10));
    assertFalse(map.existField(11, 10));
    assertFalse(map.existField(11, 11));
  }

  @Test
  void changeFieldParams() {
    game.setRadius(5);
    var map = new Map(game, new HashSet<>(), new HashSet<>(), 1);
    map.build(game);
    assertFalse(map.getFieldMap().get(List.of(1, 3)).isOccupied());
    assertFalse(map.getFieldMap().get(List.of(1, 3)).isHole());
    assertFalse(map.getFieldMap().get(List.of(1, 3)).isDrawAgainField());
    var fieldMole = map.getFieldMap().get(List.of(1, 3));
    var fieldHole = map.getFieldMap().get(List.of(1, 1));
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
    assertTrue(map.getFieldMap().get(List.of(1, 3)).isOccupied());
    assertTrue(map.getFieldMap().get(List.of(1, 1)).isHole());
    assertTrue(map.getFieldMap().get(List.of(0, 0)).isDrawAgainField());
  }
}