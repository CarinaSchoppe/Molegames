/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 16:25 by Carina Latest changes made by Carina on 15.12.21, 16:25 All contents of "PacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import de.thundergames.gameplay.player.Client;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.game.Tournament;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Data
public class PacketHandler {

  private final ServerThread client;
  private Packet packet;

  /**
   * @param packet
   * @param client
   * @author Carina
   * @use handles the incomming packets from the server
   * @see Server
   * @see Packets
   * @see Client
   */
  public synchronized void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread client) throws NotAllowedError {
    this.packet = packet;
    if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      handleLoginPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGOUT.getPacketType())) {
      handleLogoutPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        if (packet.getValues().get("message") != null) {
          System.out.println("Client with the name \"" + client.getClientName() + "\" sended: " + packet.getValues().get("message").getAsString());
        }
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETOVERVIEW.getPacketType())) {
      handleGetOverviewPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.REGISTEROBSERVER.getPacketType())) {
      handleRegisterOverviewObserverPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.UNREGISTEROBSERVER.getPacketType())) {
      handleUnregisterOverviewObserverPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVEGAME.getPacketType())) {
      handlePlayerLeavePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETSCORE.getPacketType())) {
      handleGetScorePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETGAMEHISTORY.getPacketType())) {
      handleGetGameHistoryPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETREMAININGTIME.getPacketType())) {
      handleGetRemainingTimePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLACEMOLE.getPacketType())) {
      handlePlaceMolePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MAKEMOVE.getPacketType())) {
      handleMakeMovePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ENTERTOURNAMENT.getPacketType())) {
      handleEnterTournamentPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETTOURNAMENTSCORE.getPacketType())) {
      handleGetTournamentScorePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVETOURNAMENT.getPacketType())) {
      handleLeaveTournamentPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      if (handleJoinPacket()) {
        welcomeGamePacket();
      }
    } else {
      if (MoleGames.getMoleGames().getServer().isDebug()) System.out.println("Packet not found!" + packet.getJsonObject());
    }
  }

  public Packet tournamentOverPacket() {
    var json = new JsonObject();
    json.addProperty("type", Packets.TOURNAMENTOVER.getPacketType());
    return new Packet(json);
  }

  public void tournamentGamesOverviewPacket() {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTGAMESOVERVIEW.getPacketType());
    json.addProperty("games", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client).getGames()));
  }

  public Packet tournamentPlayerInGamePacket() {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERINGAME.getPacketType());
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    json.addProperty("gameID", MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client).getTournamentID());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @return the packet send
   * @author Carina
   * @use gets a player left tournament packet that can be send to all clients of the tournament
   */
  public Packet tournamentPlayerLeftPacket() {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERLEFT.getPacketType());
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @return the packet send
   * @author Carina
   * @use gets a player kicked tournament packet that can be send to all clients of the tournament
   */
  public Packet tournamentPlayerKickedPacket() {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERKICKED.getPacketType());
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @use handles when a player wants to leave a tournament
   */
  private void handleLeaveTournamentPacket() {
    MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client).leaveTournament(client);
    MoleGames.getMoleGames().getServer().sendToAllTournamentClients(MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client), tournamentPlayerLeftPacket());
  }

  public void tournamentStateResponePacket() {
    var tournament = MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client);
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTSTATERESPONSE.getPacketType());
    var json = new JsonObject();
    json.addProperty("tournamentState", new Gson().toJson(tournament.getTournamentState()));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param player
   * @return the packet
   * @author Carina
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
   * @param gameState
   * @param eliminatedPlayers
   * @return the packet
   * @author Carina
   * @use sends the next floor (gamestate) to the players
   */
  public Packet nextFloorPacket(@NotNull final GameState gameState, @NotNull final ArrayList<Player> eliminatedPlayers) {
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
  public Packet movePenaltyNotification(@NotNull final Player player, final int points, @NotNull final Punishments punishment, @NotNull final String reason) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.MOVEPENALTYNOTIFICATION.getPacketType());
    json.addProperty("player", new Gson().toJson(player));
    json.addProperty("reason", reason);
    json.addProperty("deductedPoints", points);
    json.addProperty("punishment", punishment.getName());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @use handles the movement of a mole from the client to the server
   * @see Game
   * @see de.thundergames.playmechanics.map.Field
   * @see de.thundergames.playmechanics.map.Map
   * @see Player
   */
  private synchronized void handleMakeMovePacket() {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
    for (var player : game.getPlayers()) {
      if (player.getServerClient().equals(client)) {
        var fieldStart = new Gson().fromJson(packet.getValues().get("from").getAsString(), Field.class);
        var fieldEnd = new Gson().fromJson(packet.getValues().get("to").getAsString(), Field.class);
        player.moveMole(fieldStart.getX(), fieldStart.getY(), fieldEnd.getX(), fieldEnd.getY(), packet.getValues().get("pullDisc").getAsInt());
        return;
      }
    }
  }

  public Packet moleMovedPacket(@NotNull final Field from, @NotNull final Field to, final int pullDisc) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("from", new Gson().toJson(from));
    json.addProperty("to", new Gson().toJson(to));
    json.addProperty("pullDisc", pullDisc);
    object.addProperty("type", Packets.MOLEMOVED.getPacketType());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param player
   * @return the packet
   * @author Carina
   * @use sends to all clients whos players turn it is and with which cards they have
   */
  public Packet playersTurnPacket(@NotNull final Player player, final boolean maySkip) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERSTURN.getPacketType());
    var json = new JsonObject();
    var millis = System.currentTimeMillis();
    var until = millis + player.getGame().getTurnTime();
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    json.addProperty("maySkip", maySkip);
    json.addProperty("until", until);
    json.addProperty("pullDiscs", new Gson().toJson(player.getCards()));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param mole
   * @author Carina
   * @use sends to the clients of the game the placement of a mole
   */
  public Packet molePlacedPacket(@NotNull final Mole mole) {
    var object = new JsonObject();
    object.addProperty("type", Packets.MOLEPLACED.getPacketType());
    var json = new JsonObject();
    json.addProperty("mole", new Gson().toJson(mole));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @use handles the placement of a mole by a player
   */
  private synchronized void handlePlaceMolePacket() {
    if (client.getSocket().isConnected() && MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(client)) {
      var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
      if (game != null) {
        if (game.getCurrentGameState() == GameStates.STARTED) {
          for (var player : game.getPlayers()) {
            if (((ServerThread) player.getServerClient()).getClientName().equals(client.getClientName())) {
              var position = new Gson().fromJson(packet.getValues().get("position").getAsString(), Field.class);
              player.placeMole(position.getX(), position.getY());
              return;
            }
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
  public Packet gameStartedPacket(@NotNull final GameStates gameState) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMESTARTED.getPacketType());
    var json = new JsonObject();
    json.addProperty("initialGameState", gameState.getName());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @use sends to the client that he needs to place a mole
   * @see Game
   * @see Player
   * @see Mole
   */
  public Packet playerPlacesMolePacket() {
    if (client.getSocket().isConnected() && MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(client)) {
      var object = new JsonObject();
      object.addProperty("type", Packets.PLAYERPLACESMOLE.getPacketType());
      var json = new JsonObject();
      json.addProperty("player", new Gson().toJson(client.getPlayer()));
      json.addProperty("until", System.currentTimeMillis() + MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getSettings().getTurnTime());
      object.add("value", json);
      return new Packet(object);
    }
    return null;
  }

  /**
   * @param game
   * @author Carina
   * @use sends to the server that the game was canceled
   * @see Game
   * @see Score
   */
  public void gameCanceledPacket(@NotNull final Game game) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMECANCELED.getPacketType());
    var json = new JsonObject();
    json.addProperty("result", new Gson().toJson(game.getScore()));
    object.add("value", json);
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, new Packet(object));
  }

  /**
   * @param game
   * @author Carina
   * @use sends to the server that the game was over
   * @see Game
   * @see Score
   */
  public void gameOverPacket(@NotNull final Game game) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMEOVER.getPacketType());
    var json = new JsonObject();
    json.addProperty("result", new Gson().toJson(game.getScore()));
    object.add("value", json);
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, new Packet(object));
  }

  /**
   * @param game
   * @author Carina
   * @use sends to the server that the game was paused
   * @see Game
   */
  public void gamePausedPacket(@NotNull final Game game) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMEPAUSED.getPacketType());
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, new Packet(object));
  }

  /**
   * @param game
   * @author Carina
   * @use sends to the server that the game was continued
   * @see Game
   */
  public void gameContinuedPacket(@NotNull final Game game) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GAMECONTINUED.getPacketType());
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, new Packet(object));
  }

  /**
   * @author Carina
   * @use handles the remaining Time of the client
   */
  private void handleGetRemainingTimePacket() {
    remainingTimePacket();
  }

  /**
   * @author Carina
   * @use calculates and sends the remaining time to the client
   */
  public synchronized void remainingTimePacket() {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(client);
    for (var player : game.getPlayers()) {
      sendToUsersOnListTimeLeft(game, player);
    }
    for (var player : game.getSpectators()) {
      sendToUsersOnListTimeLeft(game, player);
    }
  }

  private void sendToUsersOnListTimeLeft(Game game, Player player) {
    if (player.getServerClient().equals(client)) {
      var remainingTime = game.getSettings().getTurnTime() - (System.currentTimeMillis());
      var object = new JsonObject();
      object.addProperty("type", Packets.REMAININGTIME.getPacketType());
      var json = new JsonObject();
      json.addProperty("timeLeft", remainingTime);
      object.add("value", json);
      player.getServerClient().sendPacket(new Packet(object));
    }
  }

  /**
   * @author Carina
   * @use gets the history of the game and sends it back to the client
   */
  private void handleGetGameHistoryPacket() {
    gameHistoryPacket(packet.getValues().get("gameID").getAsInt());
  }

  /**
   * TODO: here the messages[] einbauen einer history vom game
   *
   * @param gameID
   * @author Carina
   * @use sends the gameHistory of the game to the client
   */
  public void gameHistoryPacket(@NotNull final int gameID) {
    var json = new JsonObject();
    json.addProperty("type", Packets.GAMEHISTORYRESPONE.getPacketType());
    var object = new JsonObject();
    object.addProperty("gameID", gameID);
    json.add("value", object);
    client.sendPacket(new Packet(json));
  }

  /**
   * @author Carina
   * @use handles the getScore packet from the client
   * //TODO: wirft ne exception
   */
  private void handleGetScorePacket() {
    if (MoleGames.getMoleGames().getGameHandler().getClientGames().get(client) != null) scoreNotificationPacket(MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getScore());
  }

  /**
   * @author Carina
   * @use unregisters the overview observer from the client
   */
  private void handleUnregisterOverviewObserverPacket() {
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
  }

  private void handleRegisterOverviewObserverPacket() {
    MoleGames.getMoleGames().getServer().getObserver().add(client);
  }

  /**
   * @author Carina
   * @see ServerThread
   * @see Client
   * @see Player
   */
  private void handlePlayerLeavePacket() {
    removeFromGames();
    overviewPacket();
    client.setPlayer(null);
  }

  /**
   * @author Carina
   * @use handles the getTournamentScore from the client
   * @see Client
   * @see Score
   */
  private void handleGetTournamentScorePacket() {
    tournamentScore(MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client).getScore());
  }

  public void tournamentScore(@NotNull final Score score) {
    var json = new JsonObject();
    json.addProperty("type", Packets.TOURNAMENTSCORE.getPacketType());
    var object = new JsonObject();
    object.addProperty("score", new Gson().toJson(score));
    json.add("value", object);
    client.sendPacket(new Packet(json));
  }

  /**
   * @author Carina
   * @use removes a client from a game
   */
  private void removeFromGames() {
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      if (!MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(client)) {
        System.out.println("Client with id: " + client.getThreadID() + " tried to leave a game but was not part of one!");
        return;
      }
    }
    if (MoleGames.getMoleGames().getGameHandler().getClientGames().get(client) != null) return;
    if (MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getCurrentPlayer() != null) {
      MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getCurrentPlayer().getTimer().cancel();
    }
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).removePlayerFromGame(client.getPlayer());
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getActivePlayers().remove(client.getPlayer());
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getPlayers().remove(client.getPlayer());
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getSpectators().remove(client.getPlayer());
    MoleGames.getMoleGames().getGameHandler().getClientGames().remove(client);
  }

  /**
   * @param threadID the threadID of the client that will be send to the client to give hima id
   * @author Carina
   * @see Client
   */
  public void welcomePacket(final int threadID) {
    var object = new JsonObject();
    object.addProperty("type", Packets.WELCOME.getPacketType());
    var json = new JsonObject();
    json.addProperty("clientID", threadID);
    json.addProperty("magic", "mole42");
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use handles the login packet from the client
   */
  private synchronized void handleLoginPacket() {
    String name;
    if (packet.getValues().get("name") == null) {
      name = "PlayerModel";
    } else {
      name = packet.getValues().get("name").getAsString();
    }
    var inList = false;
    for (var clientName : MoleGames.getMoleGames().getServer().getConnectionNames().keySet()) {
      if (clientName.equalsIgnoreCase(name)) {
        inList = true;
        break;
      }
    }
    if (!inList) {
      client.setClientName(name);
      MoleGames.getMoleGames().getServer().getConnectionNames().put(client.getClientName(), client);
    } else {
      for (var i = 1; i < MoleGames.getMoleGames().getServer().getConnectionNames().size() + 1; i++) {
        var in = false;
        for (var clientName : MoleGames.getMoleGames().getServer().getConnectionNames().keySet()) {
          if (clientName.equalsIgnoreCase(name + i)) {
            in = true;
            break;
          }
        }
        if (!in) {
          client.setClientName(name + i);
          MoleGames.getMoleGames().getServer().getConnectionNames().put(client.getClientName(), client);
          break;
        }
      }
    }
    if (MoleGames.getMoleGames().getServer().isDebug()) System.out.println("Client with id " + client.getThreadID() + " got the name " + client.getClientName() + " and logged in!");
    client.setPlayer(new Player(client));
  }

  /**
   * @param gameID
   * @author Carina
   * @use sends that a player has joined or been assigned to the game with the gameID
   * @see Game
   */
  public void assignToGamePacket(final int gameID) {
    var object = new JsonObject();
    object.addProperty("type", Packets.ASSIGNTOGAME.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param score
   * @author Carina
   * @use sends the score of the client to the client
   * @see Score
   */
  private void scoreNotificationPacket(@NotNull final Score score) {
    var object = new JsonObject();
    object.addProperty("type", Packets.SCORENOTIFICATION.getPacketType());
    var json = new JsonObject();
    json.addProperty("score", new Gson().toJson(score));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use sends the overview to the clients
   * @see Game
   * @see Tournament
   */
  public synchronized void overviewPacket() {
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
   * @author Carina
   * @use sends the overview packet to the client
   */
  private void handleGetOverviewPacket() {
    overviewPacket();
  }

  /**
   * @author Carina
   * @usa handles the client logout from the game
   */
  private void handleLogoutPacket() {
    removeFromGames();
    client.endConnection();
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
    MoleGames.getMoleGames().getServer().getConnectionNames().remove(client.getClientName());
  }

  /**
   * @author Carina
   * @see Game
   * @see Player
   * @see Client
   */
  private boolean handleJoinPacket() throws NotAllowedError {
    if (MoleGames.getMoleGames().getGameHandler().getIDGames().containsKey(packet.getValues().get("gameID").getAsInt())) {
      var connectType = packet.getValues().get("participant").getAsBoolean();
      var game = MoleGames.getMoleGames().getGameHandler().getIDGames().get(packet.getValues().get("gameID").getAsInt());
      if (connectType) {
        if (MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(client)) {
          if (MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getPlayers().contains(client)) {
            return false;
          }
        }
        if (game.getCurrentGameState() == GameStates.NOT_STARTED) {
          if (game.getPlayers().size() < game.getSettings().getMaxPlayers()) {
            game.joinGame(client, false);
            return true;
          }
        }
      } else {
        if (MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(client)) {
          if (MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getSpectators().contains(client)) {
            return false;
          }
        }
        // TODO: implement client logic for spectator
        if (!game.getCurrentGameState().equals(GameStates.OVER)) {
          game.joinGame(client, true);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @return the packet that will be send to the client
   * @author Carina
   * @use get a player joined tournament packet to send it to the clients of the tournament
   */
  public Packet playerJoinedTournamentPacket() {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    object.addProperty("type", Packets.TOURNAMENTPLAYERJOINED.getPacketType());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @author Carina
   * @use handles the joining of a tournament
   */
  private void handleEnterTournamentPacket() {
    var tournament = MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(packet.getValues().get("tournamentID").getAsInt());
    tournament.joinTournament(client, packet.getValues().get("participant").getAsBoolean());
    tournamentStateResponePacket();
    MoleGames.getMoleGames().getServer().sendToAllTournamentClients(tournament, playerJoinedTournamentPacket());
  }

  /**
   * @author Carina
   * @use sends the welcomePacket to the client when he joins a game
   */
  public void welcomeGamePacket() {
    var object = new JsonObject();
    object.addProperty("type", Packets.WELCOMEGAME.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameState", new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getClientGames().get(client).getGameState()));
    object.add("value", json);
    client.sendPacket(new Packet(object));
    playerJoinedPacket();
  }

  /**
   * @author Carina
   * @use calls when a player joined the game sending the message to the clients of the game
   */
  public synchronized void playerJoinedPacket() {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERJOINED.getPacketType());
    var json = new JsonObject();
    json.addProperty("player", new Gson().toJson(client.getPlayer()));
    object.add("value", json);
    MoleGames.getMoleGames().getServer().sendToAllGameClients(MoleGames.getMoleGames().getGameHandler().getClientGames().get(client), new Packet(object));
    for (var ignored : MoleGames.getMoleGames().getServer().getObserver()) overviewPacket();
  }
}
