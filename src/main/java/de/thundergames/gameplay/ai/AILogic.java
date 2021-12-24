/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "AILogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.map.Directions;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static de.thundergames.playmechanics.map.Directions.DOWN_LEFT;
import static de.thundergames.playmechanics.map.Directions.RIGHT;

@Data
public class AILogic {

  private final AI ai;

  /**
   * @author Carina
   * @use is called to make a move!
   */
  public boolean makeMove(@NotNull Mole mole, @NotNull final Directions direction) {
    var x = 0;
    var y = 0;
    if (isHoleCloseToMole(ai) != null) {
      mole = (Mole) isHoleCloseToMole(ai).get(0);
      x = (int) isHoleCloseToMole(ai).get(1);
      y = (int) isHoleCloseToMole(ai).get(2);
      if (ai.isDebug())
        System.out.println(
            "AI: there is a hole close to a mole within the drawcard. Hole: " + x + "," + y);
      if (GameLogic.wasLegalMove(
          new int[] {mole.getField().getX(), mole.getField().getY()},
          new int[] {x, y},
          ai.getCard(),
          ai.getMap())) {
        ai.getAIPacketHandler()
            .makeMovePacket(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                new int[] {x, y},
                ai.getCard());
        return true;
      } else {
        if (ai.isDebug()) {
          System.out.println("AI: the close hole at: [" + x + "," + y + "] is not a legal move!");
        }
      }
    }
    var endField = endField(ai, mole, direction);
    if (GameLogic.wasLegalMove(
        new int[] {mole.getField().getX(), mole.getField().getY()},
        endField,
        ai.getCard(),
        ai.getMap())) {
      ai.getAIPacketHandler()
          .makeMovePacket(
              new int[] {mole.getField().getX(), mole.getField().getY()}, endField, ai.getCard());
      if (ai.isDebug()) {
        System.out.println(
            "AI: moving from: ["
                + mole.getField().getX()
                + ","
                + mole.getField().getY()
                + "] to ["
                + x
                + ","
                + y
                + "]");
      }
      return true;
    }
    return false;
  }

  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public Directions isMoveable(
      @NotNull final AI ai,
      final int cardValue,
      @NotNull final List<Integer> field,
      @NotNull final Mole mole)
      throws NullPointerException {
    for (var directions : Directions.values()) {
      Directions direction =
          (Directions)
              Arrays.stream(Directions.values())
                  .toArray()[
                  new Random().nextInt(Arrays.stream(Directions.values()).toArray().length)];
      var endfield = endField(ai, mole, direction);
      if (direction == Directions.DOWN) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0), field.get(1) - cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.DOWN;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == Directions.UP) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0), field.get(1) + cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.UP;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) - cardValue, field.get(1)))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.LEFT;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == RIGHT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) + cardValue, field.get(1)))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.RIGHT;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == DOWN_LEFT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) - cardValue, field.get(1) - cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.DOWN_LEFT;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) + cardValue, field.get(1) - cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.DOWN_RIGHT;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) - cardValue, field.get(1) + cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.UP_LEFT;
            }
          }
        } catch (@NotNull final Exception e) {
        }
      } else if (direction == Directions.UP_RIGHT) {
        try {
          if (!(ai.getMap()
              .getFieldMap()
              .get(List.of(field.get(0) + cardValue, field.get(1) + cardValue))
              .isOccupied())) {
            if (GameLogic.wasLegalMove(
                new int[] {mole.getField().getX(), mole.getField().getY()},
                endfield,
                ai.getCard(),
                ai.getMap())) {
              return Directions.UP_RIGHT;
            }
          }
        } catch (Exception e) {
        }
      }
    }
    return null;
  }

  public int[] endField(
      @NotNull final AI ai, @NotNull final Mole mole, @NotNull final Directions direction) {
    var x = 0;
    var y = 0;
    if (direction == Directions.UP) {
      y = mole.getField().getY() + ai.getCard();
      x = mole.getField().getX();
    } else if (direction == Directions.DOWN) {
      y = mole.getField().getY() - ai.getCard();
      x = mole.getField().getX();
    } else if (direction == Directions.LEFT) {
      y = mole.getField().getY();
      x = mole.getField().getX() - ai.getCard();
    } else if (direction == Directions.RIGHT) {
      y = mole.getField().getY();
      x = mole.getField().getX() + ai.getCard();
    } else if (direction == Directions.UP_LEFT) {
      y = mole.getField().getY() + ai.getCard();
      x = mole.getField().getX() - ai.getCard();
    } else if (direction == Directions.UP_RIGHT) {
      y = mole.getField().getY() + ai.getCard();
      x = mole.getField().getX() + ai.getCard();
    } else if (direction == Directions.DOWN_RIGHT) {
      y = mole.getField().getY() - ai.getCard();
      x = mole.getField().getX() + ai.getCard();
    } else if (direction == Directions.DOWN_LEFT) {
      y = mole.getField().getY() - ai.getCard();
      x = mole.getField().getX() - ai.getCard();
    }
    return new int[] {x, y};
  }

  /**
   * @param ai
   * @author Carina
   * @use handles the placement of a mole if the mole needs to be placed or if all moles has been
   *     placed
   */
  public void handleAction(@NotNull final AI ai) {
    if (ai.isDraw()) {
      ai.setDraw(false);
      if (ai.getPlacedMolesAmount() >= ai.getGameState().getMoles() || ai.isPlacedMoles()) {
        ai.setPlacedMoles(true);
        var pullDisc = ai.getPullDiscs().get(0);
        ai.setCard(pullDisc);
        ai.getPullDiscs().remove(0);
        ai.getPullDiscs().add(pullDisc);
        moveMole(ai);
      } else {
        placeMole(ai);
        ai.setPlacedMolesAmount(ai.getPlacedMolesAmount() + 1);
      }
    }
  }

  /**
   * @param ai
   * @param moles
   * @return if it could be moved or not
   * @author Carina
   * @use moves a mole depending on in a hole or on a field
   */
  private boolean move(@NotNull final AI ai, @NotNull final ArrayList<Mole> moles) {
    var random = new Random();
    for (var ignored : moles) {
      var mole = moles.get(random.nextInt(moles.size()));
      if (mole == null) {
        continue;
      }
      var direction =
          isMoveable(
              ai, ai.getCard(), List.of(mole.getField().getX(), mole.getField().getY()), mole);
      if (direction != null) {
        if (makeMove(mole, direction)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @param ai
   * @author Carina
   * @use moves a mole in a smart way checks if a mole can now get into a hole and when not it takes
   *     a random mole checks if it can be moved and than moves it in the allowed direction by the
   *     value of the drawCard
   */
  public boolean moveMole(@NotNull final AI ai) {
    var openMoles = new ArrayList<>(ai.getMoles());
    var holeMoles = new ArrayList<>(ai.getMoles());
    for (var hole : ai.getGameState().getFloor().getHoles()) {
      for (var mole : new ArrayList<>(openMoles)) {
        if (hole.getX() == mole.getField().getX() && hole.getY() == mole.getField().getY()) {
          openMoles.remove(mole);
        }
      }
    }
    holeMoles.removeAll(openMoles);
    if (!move(ai, openMoles)) move(ai, holeMoles);
    return false;
  }

  /**
   * @param field that will be checked with the mole
   * @param mole that will be checked with the field
   * @return the distance between the mole and the field in form of X and Y
   * @author Carina
   * @use input the parameter and than returns the distance between the mole and the field
   */
  public List<Integer> getDistance(@NotNull final Field field, @NotNull final Mole mole) {
    var x = mole.getField().getX();
    var y = mole.getField().getY();
    var distanceX = Math.abs(field.getX() - x);
    var distanceY = Math.abs(field.getY() - y);
    return List.of(distanceX, distanceY);
  }

  /**
   * @param ai
   * @author Carina
   * @use placed a mole at a random position
   */
  public void placeMole(@NotNull final AI ai) {
    var random = new Random();
    var fields = new ArrayList<>(ai.getMap().getFieldMap().values());
    Field field;
    while (true) {
      field = fields.get(random.nextInt(fields.size()));
      if (!field.isOccupied() && !field.isHole()) {
        break;
      } else {
        fields.remove(field);
      }
    }
    if (ai.isDebug())
      System.out.println("AI: has placed a mole on: [" + field.getX() + "," + field.getY() + "]");
    ai.getAIPacketHandler().placeMolePacket(field);
  }

  /**
   * @param ai instance
   * @author Carina
   * @use checks if a hole is close to a mole with the exact range of the card returns the mole ID
   *     and the x and y cordinates of the hole if the mole is close to a hole
   */
  public List<Object> isHoleCloseToMole(@NotNull final AI ai) {
    for (var mole : ai.getMoles()) {
      if (mole.getPlayer().getClientID() == ai.getClientThread().getThreadID()) {
        for (var hole : ai.getGameState().getFloor().getHoles()) {
          if ((hole.getX() == mole.getField().getX() + ai.getCard()
                  || hole.getX() == mole.getField().getX() - ai.getCard())
              && (hole.getY() == mole.getField().getY() + ai.getCard()
                  || hole.getY() == mole.getField().getY() - ai.getCard())) {
            return List.of(mole, hole.getX(), hole.getY());
          }
        }
      }
    }
    return null;
  }
}
