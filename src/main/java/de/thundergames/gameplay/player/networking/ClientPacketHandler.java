/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 22.11.21, 14:45 All contents of "ClientPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.map.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.NotNull;


public class ClientPacketHandler {


  /**
   * @param client the client instance that will be passed to the method for handling
   * @param packet the packet that got send by the server
   * @author Carina
   * @use handles the packet that came in
   * @see PacketHandler the packetHandler by the Server as a reference
   */
  public void handlePacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      handleWelcomePacket(client, packet);
      registerOverviewObserverPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        System.out.println("Server sended: " + packet.getValues().get("message").getAsString());
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ASSIGNTOGAME.getPacketType())) {
      handleAssignToGamePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOMEGAME.getPacketType())) {
      handleWelcomeGamePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERJOINED.getPacketType())) {
      handlePlayerJoinedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERLEFT.getPacketType())) {
      handlePlayerLeftPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERKICKED.getPacketType())) {
      handlePlayerKickedFromGame(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.OVERVIEW.getPacketType())) {
      handleOverviewPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.SCORENOTIFICATION.getPacketType())) {
      handleScoreNotificationPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEHISTORYRESPONE.getPacketType())) {
      handleGameHistoryResponsePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.REMAININGTIME.getPacketType())) {
      handleRemainingTimePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMESTARTED.getPacketType())) {
      handleGameStartedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVER.getPacketType())) {
      handleGameOverPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMECONTINUED.getPacketType())) {
      handleGameContinuedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMECANCELED.getPacketType())) {
      handleGameCanceledPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEPAUSED.getPacketType())) {
      handleGamePausedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERPLACESMOLE.getPacketType())) {
      handlePlayerPlacesMolePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSTURN.getPacketType())) {
      handlePlayersTurnPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEPLACED.getPacketType())) {
      handleMolePlacedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOVEPENALTYNOTIFICATION.getPacketType())) {
      handleMovePentaltyNotificationPacket(client, packet);
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles if a client did an invalid handling with a punishment
   */
  private void handleMovePentaltyNotificationPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("The client " + new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class).getName() + " got a move penalty for the reason" + packet.getValues().get("reason").getAsString());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the placement of a mole
   */
  public void handleMolePlacedPacket(@NotNull final Client client, @NotNull final Packet packet) {
    var mole = new Gson().fromJson(packet.getValues().get("mole").getAsString(), NetworkMole.class);
    client.getMap().getFieldMap().get(List.of(mole.getField().getX(), mole.getField().getY())).setOccupied(true);
    client.getMap().getFieldMap().get(List.of(mole.getField().getX(), mole.getField().getY())).setMole(mole);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles if the player is now on the turn
   */
  public void handlePlayersTurnPacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class).getClientID() == client.getClientThread().getClientThreadID()) {
      System.out.println("Client is now on the turn!");
      client.setDraw(true);
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          client.setDraw(false);
          System.out.println("You ran out of time!");
        }
      }, System.currentTimeMillis() - packet.getValues().get("until").getAsInt());
    }
  }


  /**
   * @param client
   * @param start
   * @param end
   * @param pullDisc
   * @author Carina
   * @use sends the movement of a mole to the server
   */
  public void makeMove(@NotNull final Client client, @NotNull final NetworkField start, @NotNull final NetworkField end, final int pullDisc) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("from", new Gson().toJson(start));
    json.addProperty("to", new Gson().toJson(end));
    json.addProperty("pullDisc", pullDisc);
    object.add("value", json);
    object.addProperty("type", Packets.MAKEMOVE.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @param field
   * @author Carina
   * @use handles that the player wants to place a mole at the position
   * @see de.thundergames.playmechanics.util.Player
   * @see NetworkField
   */
  public void placeMolePacket(@NotNull final Client client, @NotNull final NetworkField field) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLACEMOLE.getPacketType());
    var json = new JsonObject();
    json.addProperty("position", new Gson().toJson(field));
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that this player need to place a mole till the turnTime ends
   * @see de.thundergames.playmechanics.game.Game
   * @see de.thundergames.playmechanics.util.Player
   * @see de.thundergames.playmechanics.util.Mole
   */
  private void handlePlayerPlacesMolePacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("The Client " + new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class).getName() + "needs to place a mole till: " + packet.getValues().get("until").getAsInt());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the game of the client was paused
   * @see de.thundergames.playmechanics.game.Game
   */
  private void handleGamePausedPacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that a game was canceled
   * @see de.thundergames.playmechanics.game.Game
   */
  private void handleGameCanceledPacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that game is now continued
   * @see de.thundergames.playmechanics.game.Game
   */
  private void handleGameContinuedPacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the game of the client is over
   */
  private void handleGameOverPacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet that a game has started
   */
  private void handleGameStartedPacket(@NotNull final Client client, @NotNull final Packet packet) {

  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the remaining Time of the client for a turn
   */
  private void handleRemainingTimePacket(@NotNull final Client client, @NotNull final Packet packet) {
    client.setRemainingTime(packet.getValues().get("timeLeft").getAsInt());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the historyResponsePacket from the server
   */
  private void handleGameHistoryResponsePacket(@NotNull final Client client, @NotNull final Packet packet) {

  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the server send this client the score of the game
   */
  private void handleScoreNotificationPacket(@NotNull final Client client, @NotNull final Packet packet) {
    client.getGameState().setScore(new Gson().fromJson(packet.getValues(), Score.class));
  }

  /**
   * @param client
   * @author Carina
   * @use send to the server that client wants to get the score
   */
  public void getScorePacket(@NotNull final Client client) {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETSCORE.getPacketType());
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @param client
   * @author Carina
   * @use send to the server to get the GameHistory
   */
  public void getGameHistoryPacket(@NotNull final Client client, final int gameID) {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETGAMEHISTORY.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    jsonObject.add("value", json);
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @param client
   * @author Carina
   * @use sends to the server that the client wants to get the remaining time for this turn
   */
  public void getRemainingTimePacket(@NotNull final Client client) {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETREMAININGTIME.getPacketType());
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that a player left the game
   */
  protected void handlePlayerLeftPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("A player has left the Game + " + new Gson().fromJson(packet.getValues(), NetworkPlayer.class));
  }

  /***
   * @author Carina
   * @param client
   * @param packet
   * @use handles that a player was kicked from the game
   */
  protected void handlePlayerKickedFromGame(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("A player has left the Game + " + new Gson().fromJson(packet.getValues(), NetworkPlayer.class));
  }

  /**
   * @param client
   * @author Carina
   * @use send to the server that this client wants to leave a game
   */
  public void leaveGamePacket(@NotNull final Client client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.LEAVEGAME.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
    System.out.println("Client: Send left the game!");
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet that a player joined the game
   */
  protected void handlePlayerJoinedPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("A player has joined the Game + " + new Gson().fromJson(packet.getValues(), NetworkPlayer.class));
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the welcomeGamePacket from the server
   */
  protected void handleWelcomeGamePacket(@NotNull final Client client, @NotNull final Packet packet) {
    client.setGameState(new Gson().fromJson(packet.getValues(), GameState.class));
    client.setMap(new Map(client.getGameState()));
    client.getMap().changeFieldParams(client.getGameState());
    System.out.println(packet.getJsonObject().toString());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use send by server to welcome new client connection with the clientID
   */
  protected void handleWelcomePacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
      System.exit(3);
    }
    client.getClientThread().setID(packet.getValues().get("clientID").getAsInt());
    System.out.println("Client connected sucessfully to the Server!");
    client.setNetworkPlayer(new NetworkPlayer(client.getName(), client.getClientThread().getClientThreadID()));
  }

  /**
   * @param client
   * @param name
   * @author Carina
   * @use sends the login packet to the server and wants response with welcome
   */
  public void loginPacket(@NotNull final Client client, @NotNull final String name) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("name", name);
    object.addProperty("type", Packets.LOGIN.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * author Carina
   *
   * @param client
   * @param packet
   * @use handles the overview packet from the server
   */
  protected void handleOverviewPacket(@NotNull final Client client, @NotNull final Packet packet) {
    client.getGames().clear();
    client.getTournaments().clear();
    client.getGames().addAll(new Gson().fromJson(packet.getValues().get("games"), ArrayList.class));
    client.getTournaments().addAll(new Gson().fromJson(packet.getValues().get("tournaments"), ArrayList.class));
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a player into the game
   */
  protected void handleAssignToGamePacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Client joined game with id: " + packet.getValues().get("gameID").getAsInt());
    client.setGameID(packet.getValues().get("gameID").getAsInt());
  }

  /**
   * @param client
   * @param gameID
   * @param player
   * @author Carina
   * @use send to the server that this client wants to join a specific game as player or spectator
   * @see de.thundergames.playmechanics.game.Game
   * @see de.thundergames.playmechanics.util.Player
   */
  public void joinGamePacket(@NotNull final Client client, @NotNull final int gameID, @NotNull final boolean player) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    json.addProperty("participant", player);
    object.addProperty("type", Packets.JOINGAME.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use send to the server to register as an overview observer
   */
  public void registerOverviewObserverPacket(@NotNull final Client client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.REGISTEROBSERVER.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

}





