/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "AIPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.networking.ClientPacketHandler;

public class AIPacketHandler extends ClientPacketHandler {

/* TODO: hier
   * @param ai     the instance of the AI
   * @param packet the packet recieved
   * @throws UndefinedError
   * @author Carina
   * @use the logic for the AI to decide what to do depending on the packet recieved
   *//**//*
  public void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) throws UndefinedError {
    if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTPLAYER.getPacketType())) {
      nextPlayerPacket(ai, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWNCARD.getPacketType())) {
      drawnCardPacket(ai, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      joinGamePacket(ai, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      messagePacket(packet);
    } else if (packet.getPacketType().equals(Packets.MOLES.getPacketType())) {
      molePacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.OCCUPIED.getPacketType())) {
      isOccupiedPacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.INGAME.getPacketType())) {
      inGamePacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.WELCOME.getPacketType())) {
      welcomePacket(ai, packet);
      ai.getClientThread().sendPacket(new Packet(new JsonObject().put("type", Packets.JOINGAME.getPacketType()).put("value", new JsonObject().put("gameID", ai.getGameID()).put("connectType", "player").put("ai", true).toString())));
    } else if (packet.getPacketType().equals(Packets.TURNOVER.getPacketType())) {
      turnOverPacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.PLACEMOLE.getPacketType())) {
      molePlacePacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.NOTEXISTS.getPacketType())) {
      gameNotExistsPacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.INVALIDMOVE.getPacketType())) {
      invalidMovePacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.MOVEMOLE.getPacketType())) {
      moveMolePacket(ai, packet);
    } else if (packet.getPacketType().equals(Packets.FULL.getPacketType())) {
      fullPacket();
    } else {
      throw new UndefinedError(packet.getPacketType());
    }
  }*//*

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use the packet send from the server that the AI is now on the turn
   *//*
  private void nextPlayerPacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AI: Im on turn!");
    ai.setDraw(true);
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    ai.getLogic().handlePlacement(ai);
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use the card drawn by the AI and send from the server to the client as a response
   *//*
  private void drawnCardPacket(@NotNull final AI ai, @NotNull final Packet packet) {
    ai.setCard(packet.get().getInt("card"));
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    ai.getLogic().moveMoles(ai);
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use is send when the AI joins a game with the AIs gameID
   * @see AI
   *//*
  private void joinGamePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AI: Joined the game with id: " + ai.getGameID());
  }

  *//**
   * @param packet
   * @author Carina
   * @use handles the message send by the Server
   *//*
  private void messagePacket(@NotNull final Packet packet) {
    if (!packet.getValues().isEmpty() && !packet.getValues().toString().equalsIgnoreCase("{}")) {
      System.out.println("Server sended: " + packet.getValues().getString("message"));
    }
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use is triggered when the server sends the moleIDs to the clients
   *//*
  private void molePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    for (int i = 0; i < packet.getValues().getJSONArray("moles").toList().size(); i++) {
      ai.getMoleIDs().add(packet.getValues().getJSONArray("moles").getInt(i));
      System.out.println("MoleID is: " + ai.getMoleIDs().get(i));
    }
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use triggers when the AI wants to do something with a mole on an occupied field
   *//*
  private void isOccupiedPacket(@NotNull final AI ai, @NotNull final Packet packet) {
    if (!ai.isPlacedMoles()) {
      System.out.println("AI: placing mole again!");
      ai.getLogic().placeMoles(ai, packet.getValues().getInt("moleID"));
    }
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use fires when the AI tries to join a allready running game
   *//*
  private void inGamePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("Server sended: game with gameID: " + packet.getValues().getInt("gameID") + " is allready running!");

  }



  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use handles when the AIs turn is over
   *//*
  private void turnOverPacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AIs turn is over");
    ai.setDraw(false);
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use handles when a mole was placed by a player
   *//*
  private void molePlacePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("A mole was placed");
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use trigged when the AI tries to join a non existing game with the gameID
   *//*
  private void gameNotExistsPacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AI: The game you want to connect to does not exist!");
  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use is triggered when the AI did an invalid move with a mole
   *//*
  private void invalidMovePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AI has done in invalid move!");

  }

  *//**
   * @param ai
   * @param packet
   * @author Carina
   * @use handles when a player has moved a mole
   *//*
  private void moveMolePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    System.out.println("AI: A mole has been moved!");

  }



  *//**
   * @author Carina
   * @use triggers when a game that the AI wants to join is full
   *//*
  private void fullPacket() {
    System.out.println("AI: the game I should join is allready full!");
  }

*//*TODO: hier
  *//*
*//**
   * @param clientThread
   * @param object
   * @param json
   * @author Carina
   * @use sends a random Positon to the Server to place or move a mole to this position
   *//**//*

  public void randomPositionPacket(@NotNull final ClientThread clientThread, @NotNull final JsonObject object, @NotNull final JsonObject json) {
    System.out.println("AI: Does random move");
    var xZahl = new Random().nextInt(11);
    var yZahl = new Random().nextInt(11);
    json.put("x", xZahl);
    json.put("y", yZahl);
    object.put("value", json.toString());
    System.out.println("AI: moved / placed a mole at: " + xZahl + " " + yZahl);
    clientThread.sendPacket(new Packet(object));
  }
*//*

  *//**
   * @author Carina
   * @param packet
   * @use handles the new gameState for the AI map creator
   *//*
  private void handleIncommingGameState(@NotNull final AI ai, @NotNull final Packet packet){
//TODO hier
  }
  */

}
