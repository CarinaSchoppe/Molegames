/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "AI" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.ai;

import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.ai.networking.AIPacketHandler;
import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Floors;
import de.thundergames.playmechanics.map.Map;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AI extends Client {

  private final ArrayList<Integer> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Integer> playerMolesOnField = new ArrayList<>();
  private final HashMap<Integer, List<Integer>> molePositions = new HashMap<>();
  private final int gameID;
  private boolean isDraw = false;
  private boolean cardValueArrived = false;
  private int card;
  private Thread AIThread;
  private boolean placedMoles;
  private int placedMolesAmount = 0;
  private int clientID;
  private final AILogic logik;
  private Map map;


  public AI(@NotNull final String ip, final int port, final int gameID) {
    super(port, ip, "AI");
    this.gameID = gameID;
    clientPacketHandler = new AIPacketHandler();
    logik = new AILogic();
  }

  /**
   * @author Carina
   * @use is called to make a move!
   */

  //TODO: hier besser random
  private void makeMove(int moleID) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);
    if (!isPlacedMoles()) {
      getAIPacketHandler().randomPositionPacket(clientThread, object, json);
    } else {
      var r1 = new Random().nextInt(2);
      var r2 = new Random().nextInt(2);
      var x = r1 >= 1 ? card : card * -1;
      var y = r2 >= 1 ? card : card * -1;
      json.put("x", molePositions.get(moleID).get(0) + x);
      json.put("y", molePositions.get(moleID).get(1) + y);
      object.put("values", json.toString());
      System.out.println("AI: moving from: " + molePositions.get(moleID).get(0) + ", " + molePositions.get(moleID).get(1) + " to " + molePositions.get(moleID).get(0) + x + ", " + molePositions.get(moleID).get(1) + y);
      clientThread.sendPacket(new Packet(object));
      System.out.println("ai does smart move");
    }
    System.out.println("AI makes a move!");
  }


  public void placeMoles(int moleID) {
    if (placedMolesAmount < getMoleIDs().size() - 1) {
      var object = new JSONObject();
      object.put("type", Packets.PLACEMOLE.getPacketType());
      var json = new JSONObject();
      json.put("moleID", moleID);
      getAIPacketHandler().randomPositionPacket(clientThread, object, json);
    }
  }


  public Map createMapFromJson(JSONObject json) {
    var radius = json.getInt("radius");
    var map = new Map(radius);
    var floor = new Floors(0, map, 0);
    // Top left to mid right
    floor.getFields().clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(List.of(x, y));
        setFieldItems(json, floor, y, x, field);
        floor.getFieldMap().put(List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        setFieldItems(json, floor, y, x, field);
        floor.getFieldMap().put(java.util.List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    map.setFloor(floor);
    molePositions.clear();
    for (var field : map.getFloor().getFields()) {
      if (field.isOccupied()) {
        if (playerMolesOnField.contains(field.getMole()) || playerMolesInHoles.contains(field.getMole())) {
          System.out.println("packe mole in unsere liste");
          molePositions.put(field.getMole(), field.getId());
        }
      }
    }
    return map;
  }

  private void setFieldItems(JSONObject json, Floors floor, int y, int x, Field field) {
    field.setOccupied(json.getBoolean("field["
        + x
        + ","
        + y
        + "].occupied"), json.getInt("field["
        + x
        + ","
        + y
        + "].mole"));

    field.setDoubleMove(json.getBoolean("field["
        + x
        + ","
        + y
        + "].doubleMove"));
    field.setHole(json.getBoolean("field["
        + field.getId().get(0)
        + ","
        + field.getId().get(1)
        + "].hole"));
    field.setFloor(floor);
  }

  @Override
  public void connect() {
    try {
      socket = new Socket(ip, port);
      clientThread = new AIClientThread(socket, 0, this);
      clientThread.start();
    } catch (IOException exception) {
      System.out.println("Is the server running?!");
    }
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public void drawCard() {
    System.out.println("AI: Drawing a card!");
    var object = new JSONObject();
    object.put("type", Packets.DRAWCARD.getPacketType());
    clientThread.sendPacket(new Packet(object));
  }

  public void moveMoles() {
    boolean moveable = false;
    System.out.println("AI: got card and will place a mole!");
    for (var moleID : getPlayerMolesOnField()) {
      if (molePositions.containsKey(moleID)) {
        if (logik.isMoveable(this, card, List.of(molePositions.get(moleID).get(0), molePositions.get(moleID).get(1))) != null) {
          moveable = true;
          System.out.println("mole with id: " + moleID + " is movable");
          makeMove(moleID);
          break;
        } else {
          System.out.println("not movable in all directions");
        }
      }
    }
    if (!moveable) {
      for (var moleID : getPlayerMolesInHoles()) {
        if (molePositions.containsKey(moleID)) {
          if (logik.isMoveable(this, card, List.of(molePositions.get(moleID).get(0), molePositions.get(moleID).get(1))) != null) {
            makeMove(moleID);
            break;
          }
        }
      }
    }
  }


  public ArrayList<Integer> getPlayerMolesInHoles() {
    return playerMolesInHoles;
  }


  public AIPacketHandler getAIPacketHandler() {
    return (AIPacketHandler) clientPacketHandler;
  }


  public void setDraw(boolean draw) {
    isDraw = draw;
  }

  public void setCardValue(boolean cardValueArrived) {
    this.cardValueArrived = cardValueArrived;
  }


  public void setCard(int card) {
    this.card = card;
  }

  public Thread getAIThread() {
    return AIThread;
  }

  public void setAIThread(Thread AIThread) {
    this.AIThread = AIThread;
  }

  public boolean isPlacedMoles() {
    return placedMoles;
  }

  public void handlePlacement() {
    if (isDraw) {
      if (placedMolesAmount >= getMoleIDs().size() - 1) {
        System.out.println("AI: All moles placed!");
        placedMoles = true;
        drawCard();
      } else {
        System.out.println("platziere nun einen mole");
        playerMolesOnField.add(getMoleIDs().get(placedMolesAmount));
        placeMoles(getMoleIDs().get(placedMolesAmount));
        placedMolesAmount++;
      }
    }
  }

  public int getGameID() {
    return gameID;
  }


  public void setClientID(int clientID) {
    this.clientID = clientID;
  }

  public ArrayList<Integer> getPlayerMolesOnField() {
    return playerMolesOnField;
  }
}
