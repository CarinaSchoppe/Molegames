/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 21:48 by Carina latest changes made by Carina on 22.11.21, 21:48 All contents of "AILogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Directions;
import de.thundergames.playmechanics.util.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class AILogic {

  /**
   * @author Carina
   * @use is called to make a move!
   */
  public boolean makeMove(@NotNull final AI ai, @NotNull final NetworkMole mole, @NotNull final Directions direction) {
    int x = 0;
    int y = 0;
    NetworkMole m;
    if (isHoleCloseToMole(ai) != null) { //TODO: not smart
      m = (NetworkMole) isHoleCloseToMole(ai).get(0);
      x = (int) isHoleCloseToMole(ai).get(1);
      y = (int) isHoleCloseToMole(ai).get(2);
      if (GameLogic.wasLegalMove(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY()), List.of(x, y), ai.getCard(), ai.getMap())) {
        ai.getAIPacketHandler().makeMovePacket(ai, mole.getNetworkField(), new NetworkField(x, y), ai.getCard());
        return true;
      }
    }
    m = mole;
    if (direction == Directions.UP) {
      y = mole.getNetworkField().getY() + ai.getCard();
      x = mole.getNetworkField().getX();
    } else if (direction == Directions.DOWN) {
      y = mole.getNetworkField().getY() - ai.getCard();
      x = mole.getNetworkField().getX();
    } else if (direction == Directions.LEFT) {
      y = mole.getNetworkField().getY();
      x = mole.getNetworkField().getX() - ai.getCard();
    } else if (direction == Directions.RIGHT) {
      y = mole.getNetworkField().getY();
      x = mole.getNetworkField().getX() + ai.getCard();
    } else if (direction == Directions.UP_LEFT) {
      y = mole.getNetworkField().getY() + ai.getCard();
      x = mole.getNetworkField().getX() - ai.getCard();
    } else if (direction == Directions.UP_RIGHT) {
      y = mole.getNetworkField().getY() + ai.getCard();
      x = mole.getNetworkField().getX() + ai.getCard();
    } else if (direction == Directions.DOWN_RIGHT) {
      y = mole.getNetworkField().getY() - ai.getCard();
      x = mole.getNetworkField().getX() + ai.getCard();
    } else if (direction == Directions.DOWN_LEFT) {
      y = mole.getNetworkField().getY() - ai.getCard();
      x = mole.getNetworkField().getX() - ai.getCard();
    }

    if (GameLogic.wasLegalMove(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY()), List.of(x, y), ai.getCard(), ai.getMap())) {
      ai.getAIPacketHandler().makeMovePacket(ai, mole.getNetworkField(), new NetworkField(x, y), ai.getCard());
      System.out.println("AI: moving from: " + mole.getNetworkField().getX() + ", " + mole.getNetworkField().getY() + " to " + x + ", " + y);
      System.out.println("ai does smart move");
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
  public Directions isMoveable(@NotNull final AI ai, final int cardValue, @NotNull final List<Integer> field) throws NullPointerException {
    var directions = Arrays.stream(Directions.values()).toArray();
    var random = new Random();

    while (true) {
      var direction = directions[random.nextInt(directions.length)];
      System.out.println("Direction " + direction);
      if (direction == Directions.DOWN) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0), field.get(1) - cardValue)).isOccupied())) {
            return Directions.DOWN;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0), field.get(1) + cardValue)).isOccupied())) {
            return Directions.UP;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1))).isOccupied())) {
            return Directions.LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == RIGHT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1))).isOccupied())) {
            return Directions.RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == DOWN_LEFT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) - cardValue)).isOccupied())) {
            return Directions.DOWN_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) - cardValue)).isOccupied())) {
            return Directions.DOWN_RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) + cardValue)).isOccupied())) {
            return Directions.UP_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_RIGHT) {
        try {
          if (!(ai.getMap().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) + cardValue)).isOccupied())) {
            return Directions.UP_RIGHT;
          }
        } catch (Exception e) {
        }
      }
    }

  }

  /**
   * @param ai
   * @author Carina
   * @use handles the placement of a mole if the mole needs to be placed or if all moles has been placed
   */
  public void handleAction(@NotNull final AI ai) {
    if (ai.isDraw()) {
      ai.setDraw(false);
      if (ai.getPlacedMolesAmount() >= ai.getGameState().getMoles() || ai.isPlacedMoles()) {
        ai.setPlacedMoles(true);
        var pullDisc = ai.getPullDiscs().get(0);
        System.out.println("PullDisc value: " + pullDisc);
        ai.setCard(pullDisc);
        ai.getPullDiscs().remove(0);
        ai.getPullDiscs().add(pullDisc);
        moveMole(ai);
        System.out.println("AI has moved a mole!");

      } else {
        placeMole(ai);
        ai.setPlacedMolesAmount(ai.getPlacedMolesAmount() + 1);
        System.out.println("AI has placed a mole!");
      }
    }
  }

  /**
   * @param ai
   * @author Carina
   * @use moves a mole in a smart way checks if a mole can now get into a hole and when not it takes a random mole checks if it can be moved and than moves it in the allowed direction by the value of the drawCard
   */
  public void moveMole(@NotNull final AI ai) {
    for (var moles : ai.getGameState().getPlacedMoles()) {
      if (moles == null) {
        continue;
      }
      if (moles.getPlayer().getClientID() == ai.getClientThread().getClientThreadID()) {
        var direction = isMoveable(ai, ai.getCard(), List.of(moles.getNetworkField().getX(), moles.getNetworkField().getY()));
        if (direction != null) {
          System.out.println("mole with position: " + moles.getNetworkField().getX() + ", " + moles.getNetworkField().getY() + " is movable");
          if (makeMove(ai, moles, direction)) {
            break;
          }
        } else {
          System.out.println("all moles are not movable in all directions");
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
      if (!field.isOccupied() && !field.isHole()) {
        break;
      }
    }
    ai.getAIPacketHandler().placeMolePacket(ai, field);
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
          if ((hole.getX() == mole.getNetworkField().getX() + ai.getCard() || hole.getX() == mole.getNetworkField().getX() - ai.getCard()) && (hole.getY() == mole.getNetworkField().getY() + ai.getCard() || hole.getY() == mole.getNetworkField().getY() - ai.getCard())) {
            System.out.println("AI: there is a hole close to a mole within the drawcard. Hole:" + hole.getX() + "," + hole.getY() + " mole: " + mole);
            return List.of(mole, hole.getX(), hole.getY());
          }
        }
      }
    }
    return null;
  }

}

