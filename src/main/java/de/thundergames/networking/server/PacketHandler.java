/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 22.11.21, 14:49 All contents of "PacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class PacketHandler {

  public void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread client) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      handleLoginPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGOUT.getPacketType())) {
      handleLogoutPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        System.out.println("Client with name " + client.getClientName() + " sended: " + packet.getValues().get("message").getAsString());
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETOVERVIEW.getPacketType())) {
      handleGetOverviewPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.REGISTEROBSERVER.getPacketType())) {
      handleRegisterOverviewObserverPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.UNREGISTEROBSERVER.getPacketType())) {
      handleUnregisterOverviewObserverPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVEGAME.getPacketType())) {
      handlePlayerLeavePacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETSCORE.getPacketType())) {
      handleGetScorePacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETGAMEHISTORY.getPacketType())) {
      handleGetGameHistoryPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETREMAININGTIME.getPacketType())) {
      handleGetRemainingTimePacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLACEMOLE.getPacketType())) {
      handlePlaceMolePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      if (handleJoinPacket(packet, client)) {
        welcomeGamePacket(client);
      }
    } else {
      System.out.println("Packet not found!" + packet.getJsonObject());
    }
  }


  /**
   * @author Carina
   * @param player
   * @return the packet
   * @use sends the packet to all clients that this player was skipped!
   */
  public Packet playerSkippedPacket(@NotNull final Player player) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.PLAYERSKIPPED.getPacketType());
    json.addProperty("player", new Gson().toJson(player));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @param gameState
   * @param eliminatedPlayers
   * @return the packet
   * @use sends the next floor (gamestate) to the players
   */
  public Packet nextLevelPacket(@NotNull final GameState gameState, @NotNull final ArrayList<Player> eliminatedPlayers) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.NEXTLEVEL.getPacketType());
    json.addProperty("gameState", new Gson().toJson(gameState));
    json.addProperty("eliminatedPlayers", new Gson().toJson(eliminatedPlayers));
    object.add("value", json);
    return new Packet(object);

  }

  /**
   * @param player
   * @param punishment
   * @param reason
   * @return the packet
   * @author Carina
   * @use get the packet for the clients for the punishment notification
   */
  public Packet movePenaltyNotification(@NotNull final NetworkPlayer player, @NotNull final Punishments punishment, @NotNull final String reason) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.MOVEPENALTYNOTIFICATION.getPacketType());
    json.addProperty("player", new Gson().toJson(player));
    json.addProperty("reason", reason);
    json.addProperty("punishment", punishment.getName());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the movement of a mole
   */
  private void handleMoleMovedPacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
    for (var player : game.getPlayers()) {
      if (player.getServerClient().equals(client)) {
        var fieldStart = new Gson().fromJson(packet.getValues().get("from").getAsString(), NetworkField.class);
        var fieldEnd = new Gson().fromJson(packet.getValues().get("to").getAsString(), NetworkField.class);
        player.moveMole(fieldStart.getX(), fieldStart.getY(), fieldEnd.getX(), fieldEnd.getY(), packet.getValues().get("pullDisc").getAsInt());
        return;
      }
    }
  }

  /**
   * @param client
   * @param player
   * @return the packet
   * @author Carina
   * @use sends to all clients whos players turn it is and with which cards they have
   */
  public Packet playersTurnPacket(@NotNull final ServerThread client, @NotNull final Player player) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERSTURN.getPacketType());
    var json = new JsonObject();
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    json.addProperty("until", System.currentTimeMillis() + player.getGame().getTurnTime());
    json.addProperty("pullDiscs", new Gson().toJson(player.getCards()));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param mole
   * @author Carina
   * @use sends to the clients of the game the placement of a mole
   */
  public Packet molePlacedPacket(@NotNull final NetworkMole mole) {
    var object = new JsonObject();
    object.addProperty("type", Packets.MOLEPLACED.getPacketType());
    var json = new JsonObject();
    json.addProperty("mole", new Gson().toJson(mole));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the placement of a mole by a player
   */
  private void handlePlaceMolePacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
    if (game != null) {
      if (game.getGameState().equals(GameStates.STARTED)) {
        for (var player : game.getPlayers()) {
          if (player.getServerClient().equals(client)) {
            player.placeMole(packet.getValues().get("x").getAsInt(), packet.getValues().get("y").getAsInt());
            return;
          }
        }
      }
    }
  }

  /**
   * @param client
   * @param gameState
   * @author Carina
   * @use sends to the server that the game was started activated by the ausrichter
   * @see Game
   * @see GameStates
   */
  public void gameStartedPacket(@NotNull final ServerThread client, @NotNull final GameStates gameState) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMESTARTED.getPacketType());
    var json = new JsonObject();
    json.addProperty("initialGameState", gameState.getName());
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends to the client that he needs to place a mole
   * @see Game
   * @see Player
   * @see de.thundergames.networking.util.interfaceItems.NetworkMole
   */
  public Packet playerPlacesMolePacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERPLACESMOLE.getPacketType());
    var json = new JsonObject();
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    json.addProperty("until", System.currentTimeMillis() + MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getSettings().getTurnTime());
    object.add("value", json);
    return new Packet(object);
  }


  /**
   * @param client
   * @param score
   * @author Carina
   * @use sends to the server that the game was canceled
   * @see Game
   * @see Score
   */
  public void gameCanceledPacket(@NotNull final ServerThread client, @NotNull final Score score) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMECANCELED.getPacketType());
    var json = new JsonObject();
    json.addProperty("result", new Gson().toJson(score));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @param score
   * @author Carina
   * @use sends to the server that the game was over
   * @see Game
   * @see Score
   */
  public void gameOverPacket(@NotNull final ServerThread client, @NotNull final Score score) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMEOVER.getPacketType());
    var json = new JsonObject();
    json.addProperty("result", new Gson().toJson(score));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends to the server that the game was paused
   * @see Game
   */
  public void gamePausedPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMEPAUSED.getPacketType());
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends to the server that the game was continued
   * @see Game
   */
  public void gameContinuedPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMECONTINUED.getPacketType());
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use handles the remaining Time of the client
   */
  private void handleGetRemainingTimePacket(@NotNull final ServerThread client) {
    remainingTimePacket(client);
  }

  /**
   * @param client
   * @author Carina
   * @use calculates and sends the remaining time to the client
   */
  public void remainingTimePacket(@NotNull final ServerThread client) {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
    for (var player : game.getPlayers()) {
      if (player.getServerClient().equals(client)) {
        var remainingTime = game.getSettings().getTurnTime() - (System.currentTimeMillis() - player.getRemainingTime());
        var object = new JsonObject();
        object.addProperty("type", Packets.REMAININGTIME.getPacketType());
        var json = new JsonObject();
        json.addProperty("timeLeft", remainingTime);
        object.add("value", json);
        player.getServerClient().sendPacket(new Packet(object));
      }
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use gets the history of the game and sends it back to the client
   */
  private void handleGetGameHistoryPacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    gameHistoryPacket(client, packet.getValues().get("gameID").getAsInt());
  }

  /**
   * TODO: here the messages[]
   *
   * @param client
   * @param gameID
   * @author Carina
   * @use sends the gameHistory of the game to the client
   */
  public void gameHistoryPacket(@NotNull final ServerThread client, @NotNull final int gameID) {
    var json = new JsonObject();
    json.addProperty("type", Packets.GAMEHISTORYRESPONE.getPacketType());
    var object = new JsonObject();
    object.addProperty("gameID", gameID);
    json.add("value", object);
    client.sendPacket(new Packet(json));
  }

  /**
   * @param client
   * @author Carina
   * @use handles the getScore packet from the client
   */
  private void handleGetScorePacket(@NotNull final ServerThread client) {
    scoreNotificationPacket(client, MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getScore());
  }

  /**
   * @param client
   * @author Carina
   * @use unregisters the overview observer from the client
   */
  private void handleUnregisterOverviewObserverPacket(@NotNull final ServerThread client) {
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
  }

  private void handleRegisterOverviewObserverPacket(@NotNull final ServerThread client) {
    MoleGames.getMoleGames().getServer().getObserver().add(client);

  }

  /**
   * @param client the client that has left the game
   * @author Carina
   * @see ServerThread
   * @see de.thundergames.gameplay.player.networking.Client
   * @see Player
   */
  private void handlePlayerLeavePacket(@NotNull final ServerThread client) {
    removeFromGames(client);
    overviewPacket(client);
  }

  /**
   * @param client
   * @author Carina
   * @use removes a client from a game
   */
  private void removeFromGames(@NotNull ServerThread client) {
    for (var players : MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getPlayers()) {
      if (players.getServerClient().equals(client)) {
        var player = players;
        MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).removePlayerFromGame(player);
        MoleGames.getMoleGames().getGameHandler().getClientGames().remove(client);
        return;
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
      for (var i = 1; i < MoleGames.getMoleGames().getServer().getConnectionNames().size() + 1; i++) {
        if (!MoleGames.getMoleGames()
            .getServer()
            .getConnectionNames()
            .containsKey(name + i)) {
          client.setClientName(name + i);
          MoleGames.getMoleGames().getServer().getConnectionNames().put(client.getClientName(), client);
          break;
        }
      }
    }
    System.out.println("Client with id" + client.getConnectionID() + " got the name " + client.getClientName() + " and logged in!");
    client.setPlayer(new NetworkPlayer(name, client.getConnectionID()));
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
   * @param score
   * @author Carina
   * @use sends the score of the client to the client
   * @see Score
   */
  private void scoreNotificationPacket(@NotNull final ServerThread client, @NotNull final Score score) {
    var object = new JsonObject();
    object.addProperty("type", Packets.SCORENOTIFICATION.getPacketType());
    var json = new JsonObject();
    json.addProperty("score", new Gson().toJson(score));
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
    json.addProperty("games", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getGames()));
    json.addProperty("tournaments", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getTournaments()));
    object.add("value", json);
    if (client == null) {
      for (var clients : MoleGames.getMoleGames().getServer().getObserver()) {
        clients.sendPacket(new Packet(object));
      }
    } else {
      client.sendPacket(new Packet(object));
    }
  }

  /**
   * @param client
   * @author Carina
   * @use sends the overview packet to the client
   */
  private void handleGetOverviewPacket(@NotNull final ServerThread client) {
    overviewPacket(client);
  }

  /**
   * @param client
   * @author Carina
   * @usa handles the client logout from the game
   */
  private void handleLogoutPacket(@NotNull final ServerThread client) {
    removeFromGames(client);
    client.endConnection();
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
    MoleGames.getMoleGames().getServer().getConnectionNames().remove(client.getClientName());
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
    if (MoleGames.getMoleGames()
        .getGameHandler()
        .getIDGames()
        .containsKey(packet.getValues().get("gameID").getAsInt())) {

      var connectType = packet.getValues().get("participant").getAsBoolean();
      var game =
          MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(packet.getValues().get("gameID").getAsInt());
      if (connectType) {
        if (game.getCurrentGameState() == GameStates.NOT_STARTED) {
          if (game.getPlayers().size() < game.getSettings().getMaxPlayers()) {
            game.joinGame(new Player(clientConnection, game), false);
            return true;
          }
        }
      } else if (!connectType) {
        if (!game.getCurrentGameState().equals(GameStates.OVER)) {
          game.joinGame(new Player(clientConnection, game), true);
          return true;

        }
      }
    }
    return false;
  }


  /**
   * @param clientConnection
   * @author Carina
   * @use sends the welcomePacket to the client when he joins a game
   */
  public void welcomeGamePacket(@NotNull final ServerThread clientConnection) {
    var object = new JsonObject();
    object.addProperty("type", Packets.WELCOMEGAME.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameState", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getGameState()));
    object.add("value", json);
    clientConnection.sendPacket(new Packet(object));
    playerJoinedPacket(clientConnection);
  }

  /**
   * @param clientConnection
   * @author Carina
   * @use calls when a player joined the game sending the message to the clients of the game
   */
  public void playerJoinedPacket(@NotNull final ServerThread clientConnection) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERJOINED.getPacketType());
    var json = new JsonObject();
    for (var players : MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getPlayers()) {
      if (players.getServerClient().equals(clientConnection)) {
        NetworkPlayer player = new NetworkPlayer(players.getName(), players.getClientID());
        json.addProperty("player", new Gson().toJson(player));
        object.add("value", json);
        MoleGames.getMoleGames().getServer().sendToAllGameClients(MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection), new Packet(object));
        return;
      }
    }
  }
}