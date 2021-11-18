/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "AILogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import static de.thundergames.playmechanics.util.Directions.DOWN_LEFT;
import static de.thundergames.playmechanics.util.Directions.RIGHT;

import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.util.Directions;
import de.thundergames.playmechanics.util.Player;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AILogic {


  /**
   * @author Carina
   * @use is called to make a move!
   */
  public void makeMove(@NotNull final AI ai, final int moleID, @NotNull final Directions direction) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);

    if (!ai.isPlacedMoles()) {
      ai.getAIPacketHandler().randomPositionPacket(ai.getClientThread(), object, json);
    } else {
      if (isHoleCloseToMole(ai) != null) {
        json.put("moleID", (int) isHoleCloseToMole(ai).get(0));
        json.put("x", (int) isHoleCloseToMole(ai).get(1));
        json.put("y", (int) isHoleCloseToMole(ai).get(2));
      } else {
        if (direction == Directions.UP) {
          json.put("x", ai.getMolePositions().get(moleID).get(0)).put("y", ai.getMolePositions().get(moleID).get(1) + ai.getCard());
        } else if (direction == Directions.DOWN) {
          json.put("x", ai.getMolePositions().get(moleID).get(0)).put("y", ai.getMolePositions().get(moleID).get(1) - ai.getCard());
        } else if (direction == Directions.LEFT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) - ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1));
        } else if (direction == Directions.RIGHT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) + ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1));
        } else if (direction == Directions.UP_LEFT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) - ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1) + ai.getCard());
        } else if (direction == Directions.UP_RIGHT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) + ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1) + ai.getCard());
        } else if (direction == Directions.DOWN_RIGHT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) + ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1) - ai.getCard());
        } else if (direction == Directions.DOWN_LEFT) {
          json.put("x", ai.getMolePositions().get(moleID).get(0) - ai.getCard()).put("y", ai.getMolePositions().get(moleID).get(1) - ai.getCard());
        }
      }
      object.put("value", json.toString());
      System.out.println("AI: moving from: " + ai.getMolePositions().get(moleID).get(0) + ", " + ai.getMolePositions().get(moleID).get(1) + " to " + json.getInt("x") + ", " + json.getInt("y"));
      ai.getClientThread().sendPacket(new Packet(object));
      System.out.println("ai does smart move");
    }
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
          if (!ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0), field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP) {
        try {
          if (!ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0), field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1))).isOccupied()) {
            return Directions.LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == RIGHT) {

        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1))).isOccupied()) {
            return Directions.RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == DOWN_LEFT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_RIGHT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP_LEFT;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.UP_RIGHT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) + cardValue)).isOccupied()) {
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
  public void handlePlacement(@NotNull final AI ai) {
    if (ai.isDraw()) {
      if (ai.getPlacedMolesAmount() >= ai.getMoleIDs().size() - 1) {
        ai.setPlacedMoles(true);
        drawCard(ai);
      } else {
        ai.getPlayerMolesOnField().add(ai.getMoleIDs().get(ai.getPlacedMolesAmount()));
        placeMoles(ai, ai.getMoleIDs().get(ai.getPlacedMolesAmount()));
        ai.setPlacedMolesAmount(ai.getPlacedMolesAmount() + 1);
      }
    }
  }

  /**
   * @param ai
   * @author Carina
   * @use moves a mole in a smart way checks if a mole can now get into a hole and when not it takes a random mole checks if it can be moved and than moves it in the allowed direction by the value of the drawCard
   */
  public void moveMoles(@NotNull final AI ai) {
    boolean moveable = false;
    for (var moleID : ai.getPlayerMolesOnField()) {
      if (ai.getMolePositions().containsKey(moleID)) {
        var direction = isMoveable(ai, ai.getCard(), List.of(ai.getMolePositions().get(moleID).get(0), ai.getMolePositions().get(moleID).get(1)));
        if (direction != null) {
          moveable = true;
          System.out.println("mole with id: " + moleID + " is movable");
          makeMove(ai, moleID, direction);
          break;
        } else {
          System.out.println("all moles are not movable in all directions");
        }
      }
    }
    if (!moveable) {
      for (var moleID : ai.getPlayerMolesInHoles()) {
        if (ai.getMolePositions().containsKey(moleID)) {
          var direction = isMoveable(ai, ai.getCard(), List.of(ai.getMolePositions().get(moleID).get(0), ai.getMolePositions().get(moleID).get(1)));
          if (direction != null) {
            makeMove(ai, moleID, direction);
            break;
          }
        }
      }
    }
  }

  /**
   * @param ai
   * @param moleID
   * @author Carina
   * @use placed a mole at a random position
   */
  public void placeMoles(@NotNull final AI ai, final int moleID) {
    if (ai.getPlacedMolesAmount() < ai.getMoleIDs().size() - 1) {
      var object = new JSONObject();
      object.put("type", Packets.PLACEMOLE.getPacketType());
      var json = new JSONObject();
      json.put("moleID", moleID);
      ai.getAIPacketHandler().randomPositionPacket(ai.getClientThread(), object, json);
    }
  }

  /**
   * @param ai
   * @author Carina
   * @use method that is called to draw a card
   */
  public void drawCard(@NotNull final AI ai) {
    var object = new JSONObject();
    object.put("type", Packets.DRAWCARD.getPacketType());
    ai.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param ai instance
   * @author Carina
   * @use checks if a hole is close to a mole with the exact range of the card returns the mole ID and the x and y cordinates of the hole if the mole is close to a hole
   */
  public List<Object> isHoleCloseToMole(@NotNull final AI ai) {
    for (var moleID : ai.getPlayerMolesOnField()) {
      if (ai.getMolePositions().containsKey(moleID)) {
        for (var hole : ai.getMap().getFloor().getFields()) {
          if (hole.isHole()) {
            if ((hole.getX() == ai.getMolePositions().get(moleID).get(0) + ai.getCard() || hole.getX() == ai.getMolePositions().get(moleID).get(0) - ai.getCard()) && (hole.getY() == ai.getMolePositions().get(moleID).get(1) + ai.getCard() || hole.getY() == ai.getMolePositions().get(moleID).get(1) - ai.getCard())) {
              System.out.println("AI: there is a hole close to a mole within the drawcard. Hole:" + hole.getX() + "," + hole.getY() + " mole: " + moleID);
              return List.of(moleID, hole.getX(), hole.getY());
            }
          }
        }
      }
    }
    return null;
  }

}

