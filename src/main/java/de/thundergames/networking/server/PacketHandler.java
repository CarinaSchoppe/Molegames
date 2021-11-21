/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 14:13 by Carina latest changes made by Carina on 21.11.21, 14:11 All contents of "PacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Player;
import java.io.IOException;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class PacketHandler {


  public void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread client) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      handleLoginPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGOUT.getPacketType())) {
      handleLogoutPacket(packet, client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Client with id: " + client.getConnectionID() + " sended: " + packet.getValues().get("message").getAsString());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETOVERVIEW.getPacketType())) {
      getOverviewPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      if (handleJoinPacket(packet, client)) {
        welcomeGamePacket(client);
      }
    }
  }


  /**
   * @param clientConnection the client that logged in into the server
   * @param threadID         the threadID of the client that will be send to the client to give hima id
   * @author Carina
   * @see de.thundergames.gameplay.player.networking.Client
   */
  public void welcomePacket(@NotNull final ServerThread clientConnection, final int threadID) {
    var object = new JsonObject();
    object.addProperty("type", Packets.WELCOME.getPacketType());
    var json = new JsonObject();
    json.addProperty("clientID", threadID);
    json.addProperty("magic", "mole42");
    object.add("value", json);
    clientConnection.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the login packet from the client
   */
  private void handleLoginPacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    String name;

    if (packet.getValues().get("name") == null) {
      name = "Player";
    } else {
      name = packet.getValues().get("name").getAsString();
    }
    if (!MoleGames.getMoleGames()
        .getServer()
        .getConnectionNames()
        .containsKey(name)) {
      client.setClientName(name);
      MoleGames.getMoleGames().getServer().getConnectionNames().put(client.getClientName(), client);
    } else {
      for (var i = 1; i < MoleGames.getMoleGames().getServer().getConnectionNames().size(); i++) {
        if (!MoleGames.getMoleGames()
            .getServer()
            .getConnectionNames()
            .containsKey(name + i)) {
          client.setClientName(name + i);
          break;
        }
      }
    }
  }

  /**
   * @param client
   * @param gameID
   * @author Carina
   * @use sends that a player has joined or been assigned to the game with the gameID
   * @see Game
   */
  public void assignToGamePacket(@NotNull final ServerThread client, final int gameID) {
    var object = new JsonObject();
    object.addProperty("type", Packets.ASSIGNTOGAME.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }


  /**
   * @param client
   * @author Carina
   * @use sends the overview to the clients
   * @see de.thundergames.networking.util.interfaceItems.NetworkGame
   * @see de.thundergames.playmechanics.util.Tournament
   */
  public void overviewPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.OVERVIEW.getPacketType());
    var json = new JsonObject();
    json.addProperty("games", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getGames().toArray()));
    json.addProperty("tournaments", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getTournaments().toArray()));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends the overview packet to the client
   */
  private void getOverviewPacket(@NotNull final ServerThread client) {
    overviewPacket(client);
  }

  /**
   * @param packet
   * @param client
   * @author Carina
   * @usa handles the client logout from the game
   */
  private void handleLogoutPacket(@NotNull Packet packet, @NotNull final ServerThread client) {
    Player player;
    for (var players : MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getPlayers()) {
      if (players.getServerClient().equals(client)) {
        player = players;
        MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).removePlayerFromGame(player);
        client.endConnection();
        return;
      }

    }
  }

  /**
   * @param packet           the packet that will be send to the client
   * @param clientConnection the client that has joined a game depending on the packet content if spectator or player
   * @author Carina
   * @see Game
   * @see Player
   * @see de.thundergames.gameplay.player.networking.Client
   */
  private boolean handleJoinPacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    var object = new JsonObject();
    // JOIN-GAME#ID
    if (MoleGames.getMoleGames()
        .getGameHandler()
        .getIDGames()
        .containsKey(packet.getValues().get("gameID").getAsString())) {
      var connectType = packet.getValues().get("participant").getAsBoolean();
      var game =
          MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(packet.getValues().get("gameID").getAsInt());
      if (connectType) {
        if (game.getCurrentGameState().equals(GameStates.NOT_STARTED)) {
          if (game.getPlayers().size() < game.getSettings().getMaxPlayers()) {
            game.joinGame(new Player(clientConnection, game), false);
            return true;

          } else {
            object.addProperty("type", Packets.FULL.getPacketType());
            clientConnection.sendPacket(new Packet(object));
          }
        } else {
          object.addProperty("type", Packets.INGAME.getPacketType());
          var json = new JsonObject();
          json.addProperty("gameID", packet.getValues().get("gameID").getAsInt());
          object.add("value", json);
          clientConnection.sendPacket(new Packet(object));
        }
      } else if (!connectType) {
        if (!game.getCurrentGameState().equals(GameStates.OVER)) {
          game.joinGame(new Player(clientConnection, game), true);
          return true;

        }
      }
    } else {
      object.addProperty("type", Packets.NOTEXISTS.getPacketType());
      clientConnection.sendPacket(new Packet(object));
    }
    return false;
  }

  public void welcomeGamePacket(@NotNull final ServerThread clientConnection) {
    var object = new JsonObject();
    object.addProperty("type", Packets.WELCOMEGAME.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameState", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getGameState()));
    object.add("value", json);
    clientConnection.sendPacket(new Packet(object));
    playerJoinedPacket(clientConnection);
  }

  public void playerJoinedPacket(@NotNull final ServerThread clientConnection) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERJOINED.getPacketType());
    var json = new JsonObject();
    for (var players : MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getPlayers()) {
      if (players.getServerClient().equals(clientConnection)) {
        NetworkPlayer player = players;
        json.addProperty("player", new Gson().toJson(Objects.requireNonNull(player)));
        object.add("value", json);
        MoleGames.getMoleGames().getServer().sendToAllGameClients(MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection), new Packet(object));
        return;
      }

    }

  }


























  /*

   */
  /**
   * @param packet           the packet that got send to the server
   * @param clientConnection the client that send the packet
   * @throws IOException
   * @author Carina
   * @use handles the packets that are comming in and handles it with the client connected
   * @see Packets the packets that can be send
   *//*
  public void handlePacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection)
      throws UndefinedError {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      createGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      joinGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(GAMEOVERVIEW.getPacketType())) {
      gameOverviewPacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLACEMOLE.getPacketType())) {
      placeMolePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOVEMOLE.getPacketType())) {
      moveMolePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TURNTIME.getPacketType())) {
      turnTimePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVEGAME.getPacketType())) {
      leaveGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWCARD.getPacketType())) {
      drawPlayerCardPacket(clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.CONFIGURATION.getPacketType())) {
      updateConfigurationPacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues().isEmpty() || packet.getValues().toString().equalsIgnoreCase("{}")) {
        return;
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMESTART.getPacketType())) {
      startGamePacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEPAUSE.getPacketType())) {
      freezeGamePacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMERESUME.getPacketType())) {
      resumeGamePacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGOUT.getPacketType())) {
      handleLogoutPacket(clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      handleLoginPacket(clientConnection, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETOVERVIEW.getPacketType())) {
      getOverViewPacket(clientConnection);
    } else {
      throw new UndefinedError("Packet not exists");
    }
  }

  public void overviewPacket(@NotNull final ServerThread clientConnection, @NotNull final Overview overview) {
    var mapper = new ObjectMapper();
    try {
      var json = mapper.writeValueAsString(overview);
      clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.GAMEOVERVIEW.getPacketType()).put("value", json)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  *//**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the login packet from the client
   *//*
  private void handleLoginPacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    if (!MoleGames.getMoleGames()
        .getServer()
        .getConnectionNames()
        .containsKey(packet.getValues().getString("name"))) {
      client.setClientName(packet.getValues().getString("name"));
      MoleGames.getMoleGames().getServer().getConnectionNames().put(client.getClientName(), client);
    } else {
      for (var i = 1; i < MoleGames.getMoleGames().getServer().getConnectionNames().size(); i++) {
        if (!MoleGames.getMoleGames()
            .getServer()
            .getConnectionNames()
            .containsKey(packet.getValues().getString("name") + i)) {
          client.setClientName(packet.getValues().getString("name") + i);
          break;
        }
      }
    }
  }

  *//**
   * @param client
   * @author Carina
   * @use client requesting the overview
   *//*
  private void getOverViewPacket(@NotNull final ServerThread client) {

  }

  *//**
   * @param clientConnection
   * @author Carina
   * @use handles the logout packet for a client
   *//*
  private void handleLogoutPacket(@NotNull final ServerThread clientConnection) {

  }

  *//**
   * @param packet the packet that got send to the server
   *//*
  private void freezeGamePacket(@NotNull final Packet packet) {
    var game =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getGames()
            .get(packet.getValues().getString("gameId"));
    if (game != null) {
      if (!game.isGamePaused()) {
        game.pauseGame();
      }
    }
  }

  *//**
   * @param packet the packet that got send to the server
   * @author Carina
   * @use resumes the paused game if it was paused
   * @see Game
   *//*
  private void resumeGamePacket(@NotNull final Packet packet) {
    var game =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getGames()
            .get(packet.getValues().getString("gameId"));
    if (game != null) {
      if (game.isGamePaused()) {
        game.resumeGame();
      }
    }
  }

  *//**
   * @param clientConnection the player that will be on the turn next
   * @author Carina
   *//*
  public void nextPlayerPacket(@NotNull final ServerThread clientConnection) {
    clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.NEXTPLAYER.getPacketType())));
  }

  *//**
   * @param packet the packet that got send to the server
   * @author Carina
   * @use starts the game by the ID
   *//*
  private void startGamePacket(@NotNull final Packet packet) {
    if (MoleGames.getMoleGames()
        .getGameHandler()
        .getGames()
        .containsKey(packet.getValues().getInt("gameID"))) {
      MoleGames.getMoleGames()
          .getGameHandler()
          .getGames()
          .get(packet.getValues().getInt("gameID"))
          .startGame();
    }
  }

  *//**
   * @param packet the packet that got send to the server
   * @author Carina
   * @use updates the game-configuration of the game in the packet by the settings in the packet
   *//*
  private void updateConfigurationPacket(@NotNull final Packet packet) {
    Game game =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getGames()
            .get(packet.getValues().getInt("gameID"));
    if (game != null) {
      var classObject = new Gson().fromJson(packet.getJsonObject().get("value"), NetworkConfiguration.class);
      game.getSettings().updateConfiuration(classObject);
    }
  }

  *//**
   * @param clientConnection the client will recieve that packet
   * @param card             the cardID that will be send to the client by its value
   * @author Carina
   * @see Game
   * @see Player
   *//*
  public void drawnPlayerCardPacket(@NotNull final ServerThread clientConnection, final int card) {
    var object = new JsonObject();
    object.addProperty("type", Packets.DRAWNCARD.getPacketType());
    var json = new JsonObject();
    json.addProperty("card", card);
    object.add("value", json);
    clientConnection.sendPacket(new Packet(object));
  }

  *//**
   * @param clientConnection the client that send the packet and that will identify the game
   * @author Carina
   * @use calles on the player connected to the client the draw method to draw a new card
   * @see Player
   * @see Game
   *//*
  private void drawPlayerCardPacket(@NotNull final ServerThread clientConnection) {
    var player =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getClientGames()
            .get(clientConnection)
            .getClientPlayersMap()
            .get(clientConnection);
    player.drawACard();
  }

  *//**
   * @param moleID the mole that was placed by its ID
   * @param x      the x-coordinate of the mole
   * @param y      the y-coordinate of the mole
   * @return the mole that was placed in form of a Packet by its new location that will be send to all clients
   * @author Carina
   *//*
  public Packet playerPlacesMolePacket(final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    var json = new JSONObject();
    json.put("moleID", moleID);
    json.put("x", x);
    json.put("y", y);
    object.put("value", json.toString());
    return new Packet(object);
  }

  *//**
   * @param moleID the mole that was moved
   * @param x      the x-coordinate of the mole
   * @param y      the y-coordinate of the mole
   * @return the new location of the Mole in the game on the map in form of a Packet by its new location that will be send to all clients
   * @author Carina
   *//*
  public Packet playerMovesMolePacket(final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    var json = new JSONObject();

    json.put("moleID", moleID);
    json.put("x", x);
    json.put("y", y);
    object.put("value", json.toString());
    return new Packet(object);
  }

  *//**
   * @return the packet that will be send to all clients that the game is over
   * @author Carina
   *//*
  public Packet gameOverPacket() {
    var object = new JSONObject();
    object.put("type", Packets.GAMEOVER.getPacketType());
    return new Packet(object);
  }

  *//**
   * @return the packet that will be send to a client that did an invalid move and will be punished afterwards
   * @author Carina
   *//*
  public Packet invalidMovePacket() {
    var object = new JSONObject();
    object.put("type", Packets.INVALIDMOVE.getPacketType());
    return new Packet(object);
  }

  *//**
   * @param clientConnection the client that will be kicked
   * @return the packet that will be send to all clients that a client got kicked
   * @author Carina
   * @use the client will be kicked from the game and than remove all of his moles from the field
   *//*
  public Packet playerKickedPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.KICKPLAYER.getPacketType());
    var json = new JSONObject();
    json.put("clientID", clientConnection.getConnectionID());
    object.put("value", json.toString());
    return new Packet(object);
  }

  *//**
   * @param map the map that will give all clients a next floor
   * @return the Packet that will be send to all clients that the map is changed
   * @author Carina
   *//*
  public Packet nextFloorPacket(@NotNull final Map map) {
    var object = new JSONObject();
    object.put("type", Packets.NEXTFLOOR.getPacketType());
    var json = new JSONObject();

    json.put("floor", map.getFloor());
    object.put("value", json.toString());
    return new Packet(object);
  }

  public void playerTurnPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERTURN.getPacketType());
    clientConnection.sendPacket(new Packet(object));
  }

  public void sendMoleIDs(
      @NotNull final ServerThread clientConnection, ArrayList<Integer> moleIDs) { //moles: [23, 24, 25, 26 ...]
    var object = new JSONObject();
    object.put("type", Packets.MOLES.getPacketType());
    var json = new JSONObject();
    json.put("moles", moleIDs);
    object.put("value", json.toString());
    clientConnection.sendPacket(new Packet(object));
  }

  *//**
   * @param packet           that will be send to all clients that a client left the game
   * @param clientConnection the client that has left the game
   * @author Carina
   * @see ServerThread
   * @see de.thundergames.gameplay.player.networking.Client
   * @see Player
   *//*
  private void leaveGamePacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  *//**
   * @param packet           that will be send to the client that is now on the think time for a move
   * @param clientConnection the client that is now thinking for a period of time
   * @author Carina
   *//*
  private void turnTimePacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  *//**
   * @return the packet that will be send to all clients that the game will start now
   * @author Carina
   *//*
  public Packet startGamePacket() {
    return new Packet(new JSONObject().put("type", Packets.GAMESTART.getPacketType()));
  }

  *//**
   * @param clientConnection the client that logged in into the server
   * @param threadID         the threadID of the client that will be send to the client to give hima id
   * @author Carina
   * @see de.thundergames.gameplay.player.networking.Client
   *//*
  public void welcomePacket(@NotNull final ServerThread clientConnection, final int threadID) {
    var object = new JSONObject();
    object.put("type", Packets.WELCOME.getPacketType());
    var json = new JSONObject();
    json.put("clientID", threadID);
    object.put("value", json.toString());
    clientConnection.sendPacket(new Packet(object));
  }

  *//**
   * @param packet           that will be send to all clients that a move of a mole was made
   * @param clientConnection the client that performed the move
   * @author Carina
   * @see Player
   * @see de.thundergames.playmechanics.util.Mole
   *//*
  private void moveMolePacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    var position =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getClientGames()
            .get(clientConnection)
            .getMoleIDMap()
            .get(packet.getValues().getInt("moleID"))
            .getMoleField();
    MoleGames.getMoleGames()
        .getGameHandler()
        .getClientGames()
        .get(clientConnection)
        .getClientPlayersMap()
        .get(clientConnection)
        .moveMole(
            packet.getValues().getInt("moleID"),
            position.getX(),
            position.getY(),
            packet.getValues().getInt("x"),
            packet.getValues().getInt("y"));
  }

  *//**
   * @param packet           the packet that was send by the client that he placed a mole
   * @param clientConnection the client that has placed a mole
   * @author Carina
   * @see de.thundergames.playmechanics.util.Mole
   * @see de.thundergames.gameplay.player.networking.Client
   * @see Player
   *//*
  private void placeMolePacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    MoleGames.getMoleGames()
        .getGameHandler()
        .getClientGames()
        .get(clientConnection)
        .getClientPlayersMap()
        .get(clientConnection)
        .placeMole(
            packet.getValues().getInt("x"),
            packet.getValues().getInt("y"),
            packet.getValues().getInt("moleID"));
  }

  *//**
   * @param packet           the packet that will display all the running games at the moment
   * @param clientConnection the cleint that this packet will be send to
   * @author Carina
   *//*
  private void gameOverviewPacket(
      @NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }



  *//**
   * @param packet     the packet that will be send to the Method
   * @param ausrichter the ausrichter who send the packet
   * @use the packet that will be send by the client that a game should be created
   * @author Carina
   * @see Game
   *//*
  private void createGamePacket(@NotNull final Packet packet, @NotNull final ServerThread ausrichter) {
    //    "CREATE-GAME#ID"
    MoleGames.getMoleGames().getGameHandler().createNewGame(packet.getValues().getInt("gameID"), ausrichter);
  }*/
}
