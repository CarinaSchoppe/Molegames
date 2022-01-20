/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:01 by Carina Latest changes made by Carina on 20.01.22, 17:00 All contents of "AILogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static de.thundergames.playmechanics.map.Directions.RIGHT;

@Data
public class AILogic {

  private final AI ai;

  /**
   * @author Carina
   * @use is called to make a move!
   */
  public boolean makeMove(@NotNull Mole mole, @NotNull final Directions direction) {
    if (isHoleCloseToMole(ai) != null) {
      mole = (Mole) isHoleCloseToMole(ai).get(0);
      var x = (int) isHoleCloseToMole(ai).get(1);
      var y = (int) isHoleCloseToMole(ai).get(2);
      if (ai.isDebug()) {
        System.out.println(
          "AI: there is a hole close to a mole within the drawcard. Hole: " + x + "," + y);
      }
      if (GameLogic.wasLegalMove(ai,
        new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
        new int[]{x, y},
        ai.getCard(),
        ai.getMap())) {
        ai.getAIPacketHandler()
          .makeMovePacket(
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            new int[]{x, y},
            ai.getCard());
        if (ai.isDebug()) {
          System.out.println(
            "AI: moving from: ["
              + mole.getPosition().getX()
              + ","
              + mole.getPosition().getY()
              + "] to ["
              + x
              + ","
              + y
              + "]");
        }
        return true;
      } else {
        if (ai.isDebug()) {
          System.out.println("AI: the close hole at: [" + x + "," + y + "] is not a legal move!");
        }
      }
    }
    var endField = endFieldCalculator(ai, mole, direction);
    if (GameLogic.wasLegalMove(ai, new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
      endField,
      ai.getCard(),
      ai.getMap())) {
      if (ai.isDebug()) {
        System.out.println(
          "AI: moving from: ["
            + mole.getPosition().getX()
            + ","
            + mole.getPosition().getY()
            + "] to ["
            + endField[0]
            + ","
            + endField[1]
            + "]");
      }
      ai.getAIPacketHandler()
        .makeMovePacket(
          new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, ai.getCard());
      return true;
    }
    return false;
  }

  /**
   * @param ai
   * @param mole
   * @return says if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public Directions isMoveable(@NotNull final AI ai, @NotNull final Mole mole)
    throws NullPointerException {
    var directionsList = new ArrayList<>(List.of(Directions.values()));
    while (true) {
      if (directionsList.isEmpty()) {
        System.out.println("No possible directions found for this mole!\n");
        return null;
      }
      var direction = directionsList.get(new Random().nextInt(directionsList.size()));
      directionsList.remove(direction);
      var endField = endFieldCalculator(ai, mole, direction);
      if (!ai.getMap().getFieldMap().containsKey(List.of(endField[0], endField[1]))) {
        continue;
      }
      if (direction == Directions.DOWN) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.DOWN;
          }
        } catch (@NotNull final Exception ignored) {
        }
      } else if (direction == Directions.UP) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.UP;
          }
        } catch (@NotNull final Exception ignored) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.LEFT;
          }
        } catch (@NotNull final Exception ignored) {
        }
      } else if (direction == RIGHT) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.RIGHT;
          }
        } catch (@NotNull final Exception ignored) {
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.DOWN_RIGHT;
          }
        } catch (@NotNull final Exception ignored) {
        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (GameLogic.wasLegalMove(ai,
            new int[]{mole.getPosition().getX(), mole.getPosition().getY()},
            endField,
            ai.getCard(),
            ai.getMap())) {
            return Directions.UP_LEFT;
          }
        } catch (@NotNull final Exception ignored) {
        }
      }
    }
  }

  /**
   * @param ai
   * @param mole
   * @param direction
   * @return the end field of the move
   * @author Carina
   * @see Player
   */
  public int[] endFieldCalculator(
    @NotNull final AI ai, @NotNull final Mole mole, @NotNull final Directions direction) {
    var x = 0;
    var y = 0;
    if (direction == Directions.UP) {
      y = mole.getPosition().getY() + ai.getCard();
      x = mole.getPosition().getX();
    } else if (direction == Directions.DOWN) {
      y = mole.getPosition().getY() - ai.getCard();
      x = mole.getPosition().getX();
    } else if (direction == Directions.LEFT) {
      y = mole.getPosition().getY();
      x = mole.getPosition().getX() - ai.getCard();
    } else if (direction == Directions.RIGHT) {
      y = mole.getPosition().getY();
      x = mole.getPosition().getX() + ai.getCard();
    } else if (direction == Directions.UP_LEFT) {
      y = mole.getPosition().getY() - ai.getCard();
      x = mole.getPosition().getX() - ai.getCard();
    } else if (direction == Directions.DOWN_RIGHT) {
      y = mole.getPosition().getY() + ai.getCard();
      x = mole.getPosition().getX() + ai.getCard();
    }
    return new int[]{x, y};
  }

  /**
   * @param ai
   * @author Carina
   * @use handles the placement of a mole if the mole needs to be placed or if all moles has been
   * placed
   */
  public void handleAction(@NotNull final AI ai) {
    if (ai.isDraw()) {
      ai.setDraw(false);
      if (ai.getPlacedMolesAmount() >= ai.getGameState().getMoles() || ai.isPlacedMoles()) {
        ai.setPlacedMoles(true);
        var openMoles = new HashSet<>(ai.getMoles());
        var holeMoles = new HashSet<>(ai.getMoles());
        for (var hole : ai.getGameState().getFloor().getHoles()) {
          openMoles.removeIf(
            mole -> hole.getX() == mole.getPosition().getX() && hole.getY() == mole.getPosition().getY());
        }
        holeMoles.removeAll(openMoles);
        for (var pullDisc : ai.getPullDiscs()) {
          ai.setCard(pullDisc);
          if (moveMole(ai, openMoles, holeMoles)) {
            return;
          }
        }
      } else {
        placeMole(ai);
        ai.setPlacedMolesAmount(ai.getPlacedMolesAmount() + 1);
      }
    }
  }

  /**
   * @param ai
   * @param m
   * @return if it could be moved or not
   * @author Carina
   * @use moves a mole depending on in a hole or on a field
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean move(@NotNull final AI ai, @NotNull final HashSet<Mole> m) {
    var random = new Random();
    var direction = (Directions) null;
    var moles = new ArrayList<>(m);
    while (true) {
      if (moles.isEmpty()) {
        return false;
      } else {
        var mole = (Mole) null;
        if (moles.size() > 1) {
          mole = moles.get(random.nextInt(moles.size()));
        } else {
          mole = moles.get(0);
        }
        direction = isMoveable(ai, mole);
        moles.remove(mole);
        if (direction != null) {
          if (makeMove(mole, direction)) {
            return true;
          }
        }
      }
    }
  }

  /**
   * @param ai
   * @author Carina
   * @use moves a mole in a smart way checks if a mole can now get into a hole and when not it takes
   * a random mole checks if it can be moved and then moves it in the allowed direction by the
   * value of the drawCard
   */
  public boolean moveMole(@NotNull final AI ai, @NotNull final HashSet<Mole> openMoles, @NotNull final HashSet<Mole> holeMoles) {
    if (!move(ai, openMoles)) {
      if (!move(ai, holeMoles)) {
        System.out.println("AI: No move possible!");
        return false;
      }
    }
    return true;
  }

  /**
   * @param ai
   * @author Carina
   * @use placed a mole at a random position
   */
  public void placeMole(@NotNull final AI ai) {
    var random = new Random();
    var fields = new ArrayList<>(ai.getMap().getFieldMap().values());
    var field = (Field) null;
    while (true) {
      field = fields.get(random.nextInt(fields.size()));
      if (!field.isOccupied() && !field.isHole()) {
        break;
      } else {
        fields.remove(field);
      }
    }
    if (ai.isDebug()) {
      System.out.println("AI: has placed a mole on: [" + field.getX() + "," + field.getY() + "]");
    }
    ai.getAIPacketHandler().placeMolePacket(field);
  }

  /**
   * @param ai instance
   * @author Carina
   * @use checks if a hole is close to a mole with the exact range of the card returns the mole ID
   * and the x and y coordinates of the hole if the mole is close to a hole
   */
  public ArrayList<Object> isHoleCloseToMole(@NotNull final AI ai) {
    for (var mole : ai.getMoles()) {
      for (var hole : ai.getMap().getHoles()) {
        if (!ai.getMap().getFieldMap().get(List.of(hole.getX(), hole.getY())).isOccupied()) {
          if ((hole.getX() == mole.getPosition().getX() + ai.getCard()
            || hole.getX() == mole.getPosition().getX() - ai.getCard())
            && (hole.getY() == mole.getPosition().getY() + ai.getCard()
            || hole.getY() == mole.getPosition().getY() - ai.getCard())) {
            return new ArrayList<>(List.of(mole, hole.getX(), hole.getY()));
          }
        }
      }
    }
    return null;
  }
}
