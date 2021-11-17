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
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AI extends Client implements Runnable {

  private final ArrayList<Integer> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Integer> playerMolesOnField = new ArrayList<>();
  private final int gameID;
  private boolean isDraw = false;
  private boolean cardValueArrived = false;
  private int card;
  private Thread AIThread;
  private boolean placedMoles;
  private int placedMolesAmount = 0;
  private int clientID;


  public AI(@NotNull final String ip, final int port, final int gameID) {
    super(port, ip, "AI");
    this.gameID = gameID;
    clientPacketHandler = new AIPacketHandler();
  }

  /**
   * @author Carina
   * @use is called to make a move!
   */
  private void makeMove(int moleID) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);
    getAIPacketHandler().randomPositionPacket(clientThread, object, json);
    System.out.println("AI makes a move!");
  }


  public void placeMoles(int moleID) {
    var object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);
    getAIPacketHandler().randomPositionPacket(clientThread, object, json);
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

  /**
   * TODO: intelligentere AI
   *
   * @author Carina
   * @use is called when an AI starts its job
   */
  @Override
  public void run() {
    System.out.println("AI is running!");
    while (!placedMoles) {
      synchronized (this) {
        if (isDraw) {
          placeMoles(getMoleIDs().get(placedMolesAmount));
          placedMolesAmount++;
          if (placedMolesAmount >= getMoleIDs().size()) {
            System.out.println("AI: All moles placed!");

            placedMoles = true;
          }
          isDraw = false;
        }
      }
    }
    boolean moveable = false;
    while (true) {
      if (isDraw) {
        var object = new JSONObject();
        object.put("type", Packets.DRAWCARD.getPacketType());
        clientThread.sendPacket(new Packet(object));
        isDraw = false;
      }
      if (cardValueArrived) {
        for (var moleID : getMoleIDs()) {
          if (!moveable) {
            moveable = true;
            makeMove(moleID);
            break;
          }
        }
        if (!moveable) {
          for (var moleID : getPlayerMolesInHoles()) {
            if (!moveable) {
              makeMove(moleID);
              break;
            }
          }
        }
        moveable = false;
        cardValueArrived = false;
        isDraw = false;
      }
    }
  }


  public ArrayList<Integer> getPlayerMolesInHoles() {
    return playerMolesInHoles;
  }

  public ArrayList<Integer> getPlayerMolesOnField() {
    return playerMolesOnField;
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

  public int getGameID() {
    return gameID;
  }


  public void setClientID(int clientID) {
    this.clientID = clientID;
  }
}
