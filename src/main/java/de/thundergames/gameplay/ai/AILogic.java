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

  public void makeMove(AI ai, int moleID, Directions direction) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);
    if (!ai.isPlacedMoles()) {
      ai.getAIPacketHandler().randomPositionPacket(ai.getClientThread(), object, json);
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
      object.put("values", json.toString());
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


  public void handlePlacement(AI ai) {
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

  public void moveMoles(AI ai) {
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

  public void placeMoles(AI ai, int moleID) {
    if (ai.getPlacedMolesAmount() < ai.getMoleIDs().size() - 1) {
      var object = new JSONObject();
      object.put("type", Packets.PLACEMOLE.getPacketType());
      var json = new JSONObject();
      json.put("moleID", moleID);
      ai.getAIPacketHandler().randomPositionPacket(ai.getClientThread(), object, json);
    }
  }

  public void drawCard(AI ai) {
    var object = new JSONObject();
    object.put("type", Packets.DRAWCARD.getPacketType());
    ai.getClientThread().sendPacket(new Packet(object));
  }
}

