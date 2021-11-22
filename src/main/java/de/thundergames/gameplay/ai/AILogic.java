/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 16:34 by Carina latest changes made by Carina on 22.11.21, 16:34 All contents of "AILogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import static de.thundergames.playmechanics.util.Directions.DOWN_LEFT;
import static de.thundergames.playmechanics.util.Directions.RIGHT;

import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Directions;
import de.thundergames.playmechanics.util.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class AILogic {

  /**
   * @author Carina
   * @use is called to make a move!
   */
  public void makeMove(@NotNull final AI ai, @NotNull final NetworkMole mole, @NotNull final Directions direction) {
    int x = 0;
    int y = 0;
    NetworkMole m;
    if (isHoleCloseToMole(ai) != null) {
      m = (NetworkMole) isHoleCloseToMole(ai).get(0);
      x = (int) isHoleCloseToMole(ai).get(1);
      y = (int) isHoleCloseToMole(ai).get(2);
      ai.getAIPacketHandler().makeMove(ai, mole.getField(), new NetworkField(x, y), ai.getCard());
    } else {
      m = mole;
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
    }
    ai.getAIPacketHandler().makeMove(ai, mole.getField(), new NetworkField(x, y), ai.getCard());
    System.out.println("AI: moving from: " + mole.getField().getX() + ", " + mole.getField().getY() + " to " + x + ", " + y);
    System.out.println("ai does smart move");
  }

  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public Directions isMoveable(@NotNull final AI ai, final int cardValue, @NotNull final List<Integer> field) throws NullPointerException {
    for (var direction : Directions.values()) {
      if (direction == Directions.DOWN) {
        try {
          if (!ai.getMap().getFieldMap().get(List.of(field.get(0), field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP) {
        try {
          if (!ai.getMap().getFieldMap().get(List.of(field.get(0), field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1))).isOccupied()) {
            return Directions.LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == RIGHT) {

        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1))).isOccupied()) {
            return Directions.RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == DOWN_LEFT) {
        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_RIGHT) {
        try {
          if (ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP_RIGHT;
          }
        } catch (Exception e) {
        }
      }
    }
    return null;

  }

  /**
   * @param ai
   * @author Carina
   * @use handles the placement of a mole if the mole needs to be placed or if all moles has been placed
   */
  public void handleAction(@NotNull final AI ai) {
    if (ai.isDraw()) {
      if (ai.getPlacedMolesAmount() >= ai.getGameState().getMoles()) {
        ai.setPlacedMoles(true);
        ai.setCard(ai.getPullDiscs().get(0));
        moveMole(ai);
      } else {
        placeMole(ai);
        ai.setPlacedMolesAmount(ai.getPlacedMolesAmount() + 1);
      }
    }
  }

  /**
   * @param ai
   * @author Carina
   * @use moves a mole in a smart way checks if a mole can now get into a hole and when not it takes a random mole checks if it can be moved and than moves it in the allowed direction by the value of the drawCard
   */
  public void moveMole(@NotNull final AI ai) {
    boolean moveable = false;
    for (var moles : ai.getGameState().getPlacedMoles()) {
      if (moles.getPlayer().equals(ai.getNetworkPlayer())) {
        var direction = isMoveable(ai, ai.getCard(), List.of(moles.getField().getX(), moles.getField().getY()));
        if (direction != null) {
          moveable = true;
          System.out.println("mole with id: " + moles + " is movable");
          makeMove(ai, moles, direction);
          break;
        } else {
          System.out.println("all moles are not movable in all directions");
        }
      }
    }
    if (!moveable) {
      for (var moles : ai.getGameState().getPlacedMoles()) {
        if (moles.getPlayer().equals(ai.getNetworkPlayer())) {
          var direction = isMoveable(ai, ai.getCard(), List.of(moles.getField().getX(), moles.getField().getY()));
          if (direction != null) {
            makeMove(ai, moles, direction);
            break;
          }
        }
      }
    }
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
      if (!field.isOccupied()) {
        break;
      }
      ai.getAIPacketHandler().placeMolePacket(ai, field);
    }
  }

  /**
   * @param ai instance
   * @author Carina
   * @use checks if a hole is close to a mole with the exact range of the card returns the mole ID and the x and y cordinates of the hole if the mole is close to a hole
   */
  public List<Object> isHoleCloseToMole(@NotNull final AI ai) {
    for (var mole : ai.getGameState().getPlacedMoles()) {
      if (mole.getPlayer().getClientID() == ai.getClientThread().getClientThreadID()) {
        for (var hole : ai.getGameState().getFloor().getHoles()) {
          if ((hole.getX() == mole.getField().getX() + ai.getCard() || hole.getX() == mole.getField().getX() - ai.getCard()) && (hole.getY() == mole.getField().getY() + ai.getCard() || hole.getY() == mole.getField().getY() - ai.getCard())) {
            System.out.println("AI: there is a hole close to a mole within the drawcard. Hole:" + hole.getX() + "," + hole.getY() + " mole: " + mole);
            return List.of(mole, hole.getX(), hole.getY());
          }
        }
      }
    }
    return null;
  }

}

