/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 16:53 by Carina Latest changes made by Carina on 20.01.22, 16:52 All contents of "PacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.ausrichter.ui.MainGUI;
import de.thundergames.gameplay.ausrichter.ui.PlayerManagement;
import de.thundergames.gameplay.player.Client;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PacketHandler {
  /**
   * @param packet
   * @param client
   * @author Carina
   * @use handles the incoming packets from the server
   * @see Server
   * @see Packets
   * @see Client
   */
  public void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread client)
      throws NotAllowedError {
    if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      handleLoginPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LOGOUT.getPacketType())) {
      handleLogoutPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        if (packet.getValues().get("message") != null) {
          System.out.println(
              "Client with the name \""
                  + client.getClientName()
                  + "\" sent: "
                  + packet.getValues().get("message"));
        }
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GETOVERVIEW.getPacketType())) {
      handleGetOverviewPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.REGISTEROBSERVER.getPacketType())) {
      handleRegisterOverviewObserverPacket(client);
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.UNREGISTEROBSERVER.getPacketType())) {
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
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MAKEMOVE.getPacketType())) {
      handleMakeMovePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ENTERTOURNAMENT.getPacketType())) {
      handleEnterTournamentPacket(client, packet);
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.GETTOURNAMENTSCORE.getPacketType())) {
      handleGetTournamentScorePacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVETOURNAMENT.getPacketType())) {
      handleLeaveTournamentPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      if (handleJoinGamePacket(client, packet)) {
        welcomeGamePacket(client);
      }
    } else {
      for (var packets : Packets.values()) {
        if (packets.getPacketType().equalsIgnoreCase(packet.getPacketType())) {
          if (MoleGames.getMoleGames().getServer().isDebug()) {
            System.out.println(
                "The packet: " + packet.getPacketType() + " is not handled by the server!");
            return;
          }
        }
      }
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println("Packet not found: " + packet.getPacketType());
      }
    }
  }

  /**
   * @param client
   * @param game
   * @author Carina
   * @use sends the playerKicked packet to the game clients
   */
  public void playerKickedPacket(@NotNull final Player client, @NotNull final Game game) {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERKICKED.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client)));
    object.add("value", json);
    var packet = new Packet(object);
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, packet);
  }

  /**
   * @param client
   * @param game
   * @author Carina
   * @use sends the playerLeft packet to the game clients
   */
  public void playerLeftPacket(@NotNull final Player client, @NotNull final Game game) {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.PLAYERLEFT.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client)));
    object.add("value", json);
    var packet = new Packet(object);
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, packet);
  }

  /**
   * @return the packet itself
   * @author Carina
   * @use send to the clients that the tournament is over
   */
  public Packet tournamentOverPacket() {
    var json = new JsonObject();
    json.addProperty("type", Packets.TOURNAMENTOVER.getPacketType());
    json.add("value", new JsonObject());
    return new Packet(json);
  }

  /**
   * @param client
   * @author Carina
   * @use gets the overview of a tournamentPacket
   */
  public void tournamentGamesOverviewPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTGAMESOVERVIEW.getPacketType());
    json.add(
        "games",
        JsonParser.parseString(
            new Gson()
                .toJson(
                    MoleGames.getMoleGames()
                        .getGameHandler()
                        .getClientTournaments()
                        .get(client)
                        .getGames())));
  }

  /**
   * @param client
   * @return the packet itself
   * @author Carina
   * @use sends that a player is now in a game of a tournament
   */
  public Packet tournamentPlayerInGamePacket(@NotNull final ServerThread client) {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERINGAME.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    json.addProperty(
        "gameID",
        MoleGames.getMoleGames()
            .getGameHandler()
            .getClientTournaments()
            .get(client)
            .getTournamentID());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @return the packet send
   * @author Carina
   * @use gets a player left tournament packet that can be sent to all clients of the tournament
   */
  public Packet tournamentPlayerLeftPacket(@NotNull final ServerThread client) {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERLEFT.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @return the packet send
   * @author Carina
   * @use gets a player kicked tournament packet that can be sent to all clients of the tournament
   */
  public Packet tournamentPlayerKickedPacket(@NotNull final ServerThread client) {
    var json = new JsonObject();
    var object = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERKICKED.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @author Carina
   * @use handles when a player wants to leave a tournament
   */
  private void handleLeaveTournamentPacket(@NotNull final ServerThread client) {
    MoleGames.getMoleGames()
        .getGameHandler()
        .getClientTournaments()
        .get(client)
        .leaveTournament(client);
    MoleGames.getMoleGames()
        .getServer()
        .sendToAllTournamentClients(
            MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client),
            tournamentPlayerLeftPacket(client));
    MainGUI.getGUI().updateTable();
  }

  /**
   * @param client
   * @author Carina
   * @use is the response to the getTournamentState packet
   */
  public void tournamentStateResponsePacket(@NotNull final ServerThread client) {
    var tournament = MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client);
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTSTATERESPONSE.getPacketType());
    json.add(
        "tournamentState",
        JsonParser.parseString(new Gson().toJson(tournament.getTournamentState())));
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
    json.add("player", JsonParser.parseString(new Gson().toJson(player)));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param gameState
   * @param eliminatedPlayers
   * @return the packet
   * @author Carina
   * @use sends the next floor (gameState) to the players
   */
  public Packet nextFloorPacket(
      @NotNull final GameState gameState, @NotNull final ArrayList<Player> eliminatedPlayers) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.NEXTLEVEL.getPacketType());
    json.add("gameState", JsonParser.parseString(new Gson().toJson(gameState)));
    json.add("eliminatedPlayers", JsonParser.parseString(new Gson().toJson(eliminatedPlayers)));
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
  public Packet movePenaltyNotification(
      @NotNull final Player player,
      final int points,
      @NotNull final Punishments punishment,
      @NotNull final String reason) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.MOVEPENALTYNOTIFICATION.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(player)));
    json.addProperty("reason", reason);
    json.addProperty("deductedPoints", points);
    json.addProperty("penalty", punishment.getName());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the movement of a mole from the client to the server
   * @see Game
   * @see de.thundergames.playmechanics.map.Field
   * @see de.thundergames.playmechanics.map.Map
   * @see Player
   */
  private void handleMakeMovePacket(
      @NotNull final ServerThread client, @NotNull final Packet packet) {
    var game = client.getPlayer().getGame();
    if (game.getCurrentGameState() != GameStates.OVER
        && game.getCurrentGameState() != GameStates.NOT_STARTED) {
      for (var player : game.getPlayers()) {
        if (player.getServerClient().equals(client)) {
          var fieldStart = new Gson().fromJson(packet.getValues().get("from"), Field.class);
          var fieldEnd = new Gson().fromJson(packet.getValues().get("to"), Field.class);
          player.moveMole(
              fieldStart.getX(),
              fieldStart.getY(),
              fieldEnd.getX(),
              fieldEnd.getY(),
              packet.getValues().get("pullDisc").getAsInt());
          return;
        }
      }
    }
  }

  /**
   * @param from
   * @param to
   * @param pullDisc
   * @return the package that will be sent to the clients
   * @use sends to the clients that a mole was moved
   * @author Carina
   * @see Mole
   * @see Client
   * @see Player
   */
  public Packet moleMovedPacket(
      @NotNull final Field from, @NotNull final Field to, final int pullDisc) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.add("from", JsonParser.parseString(new Gson().toJson(from)));
    json.add("to", JsonParser.parseString(new Gson().toJson(to)));
    json.addProperty("pullDisc", pullDisc);
    object.addProperty("type", Packets.MOLEMOVED.getPacketType());
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @return the packet
   * @author Carina
   * @use sends to all clients whose players turn it is and with which cards they have
   */
  public Packet playersTurnPacket(@NotNull final ServerThread client, final boolean maySkip) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.PLAYERSTURN.getPacketType());
    var millis = System.currentTimeMillis();
    var until = millis + client.getPlayer().getGame().getTurnTime();
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    json.addProperty("maySkip", maySkip);
    json.addProperty("until", until);
    if (client.getPlayer().getGame().getSettings().isPullDiscsOrdered()) {
      json.add(
          "pullDiscs",
          JsonParser.parseString(
              new Gson().toJson(new int[] {client.getPlayer().getCards().get(0)})));
    } else {
      json.add(
          "pullDiscs", JsonParser.parseString(new Gson().toJson(client.getPlayer().getCards())));
    }
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
    var json = new JsonObject();
    object.addProperty("type", Packets.MOLEPLACED.getPacketType());
    json.add("mole", JsonParser.parseString(new Gson().toJson(mole)));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the placement of a mole by a player
   */
  private void handlePlaceMolePacket(
      @NotNull final ServerThread client, @NotNull final Packet packet) {
    if (client.getSocket().isConnected() && client.getPlayer().getGame() != null) {
      var game = client.getPlayer().getGame();
      if (game != null) {
        if (game.getCurrentGameState() != GameStates.OVER
            && game.getCurrentGameState() != GameStates.NOT_STARTED) {
          var position = new Gson().fromJson(packet.getValues().get("position"), Field.class);
          client.getPlayer().placeMole(position.getX(), position.getY());
        }
      }
    }
  }

  /**
   * @param gameState
   * @author Carina
   * @use sends to the server that the game was started activated by the ausrichter
   * @see Game
   * @see GameStates
   */
  public Packet gameStartedPacket(@NotNull final GameState gameState) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.GAMESTARTED.getPacketType());
    json.add("initialGameState", JsonParser.parseString(new Gson().toJson(gameState)));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param player
   * @author Carina
   * @use sends to the client that he needs to place a mole
   * @see Game
   * @see Player
   * @see Mole
   */
  public Packet playerPlacesMolePacket(@NotNull final Player player) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.PLAYERPLACESMOLE.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(player)));
    json.addProperty(
        "until", System.currentTimeMillis() + player.getGame().getSettings().getTurnTime());
    object.add("value", json);
    return new Packet(object);
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
    var json = new JsonObject();
    object.addProperty("type", Packets.GAMECANCELED.getPacketType());
    json.add("result", JsonParser.parseString(new Gson().toJson(game.getScore())));
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
    var json = new JsonObject();
    object.addProperty("type", Packets.GAMEOVER.getPacketType());
    json.add("result", JsonParser.parseString(new Gson().toJson(game.getScore())));
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
    object.add("value", new JsonObject());
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
    object.add("value", new JsonObject());
    MoleGames.getMoleGames().getServer().sendToAllGameClients(game, new Packet(object));
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
    var game = client.getPlayer().getGame();
    for (var ignored : game.getPlayers()) {
      sendToUsersOnListTimeLeft(game, client);
    }
    for (var ignored : game.getSpectators()) {
      sendToUsersOnListTimeLeft(game, client);
    }
  }

  /**
   * @param game
   * @param client
   * @author Carina
   * @use is sent to the users to tell them the time they got left
   */
  private void sendToUsersOnListTimeLeft(Game game, @NotNull final ServerThread client) {
    if (client.getPlayer().getServerClient().equals(client)) {
      var object = new JsonObject();
      var json = new JsonObject();
      long remainingTime = -1;
      if (game.getCurrentPlayer() != null) {
        remainingTime =
            game.getCurrentPlayer().getStartRemainingTime()
                + game.getSettings().getTurnTime()
                - System.currentTimeMillis();
      }
      object.addProperty("type", Packets.REMAININGTIME.getPacketType());
      json.addProperty("timeLeft", remainingTime);
      object.add("value", json);
      client.getPlayer().getServerClient().sendPacket(new Packet(object));
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use gets the history of the game and sends it back to the client
   */
  private void handleGetGameHistoryPacket(
      @NotNull final ServerThread client, @NotNull final Packet packet) {
    gameHistoryPacket(client, packet.getValues().get("gameID").getAsInt());
  }

  /**
   * TODO: here the messages[] einbauen einer history vom game
   *
   * @param client
   * @param gameID
   * @author Carina
   * @use sends the gameHistory of the game to the client
   */
  public void gameHistoryPacket(@NotNull final ServerThread client, final int gameID) {
    var json = new JsonObject();
    var object = new JsonObject();
    json.addProperty("type", Packets.GAMEHISTORYRESPONE.getPacketType());
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
    if (client.getPlayer().getGame() != null) {
      scoreNotificationPacket(client);
    }
  }

  /**
   * @param client
   * @author Carina
   * @use unregisters the overview observer from the client
   */
  private void handleUnregisterOverviewObserverPacket(@NotNull final ServerThread client) {
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
  }

  /**
   * @param client
   * @author Carina
   * @use handles the registration of an overview observer
   */
  private void handleRegisterOverviewObserverPacket(@NotNull final ServerThread client) {
    MoleGames.getMoleGames().getServer().getObserver().add(client);
  }

  /**
   * @param client
   * @author Carina
   * @see ServerThread
   * @see Client
   * @see Player
   */
  public void handlePlayerLeavePacket(@NotNull final ServerThread client) {
    if (client.getPlayer().getGame() != null) {
      var game = client.getPlayer().getGame();
      if (game.getCurrentGameState() == GameStates.NOT_STARTED
          && game.getActivePlayers().contains(client.getPlayer())) {
        playerLeftPacket(client.getPlayer(), game);
      } else if (game.getCurrentGameState() != GameStates.OVER
          && game.getActivePlayers().contains(client.getPlayer())) {
        MoleGames.getMoleGames()
            .getServer()
            .getPacketHandler()
            .playerKickedPacket(client.getPlayer(), game);
      }
    }
    removeFromGames(client);
    overviewPacket(client);
    client.setPlayer(new Player(client));
    MainGUI.getGUI().updateTable();
  }

  /**
   * @param client
   * @author Carina
   * @use handles the getTournamentScore from the client
   * @see Client
   * @see Score
   */
  private void handleGetTournamentScorePacket(@NotNull final ServerThread client) {
    tournamentScore(
        client,
        MoleGames.getMoleGames().getGameHandler().getClientTournaments().get(client).getScore());
  }

  /**
   * @param client
   * @param score
   * @author Carina
   * @use the score of the tournament is sent to the client
   */
  public void tournamentScore(@NotNull final ServerThread client, @NotNull final Score score) {
    var json = new JsonObject();
    var object = new JsonObject();
    json.addProperty("type", Packets.TOURNAMENTSCORE.getPacketType());
    json.add("score", JsonParser.parseString(new Gson().toJson(score)));
    json.add("value", object);
    client.sendPacket(new Packet(json));
  }

  /**
   * @param client
   * @author Carina
   * @use removes a client from a game
   */
  public void removeFromGames(@NotNull final ServerThread client) {
    client.getServer().getPlayingThreads().remove(client);
    client.getServer().getLobbyThreads().add(client);
    if (client.getPlayer() != null) {
      if (client.getPlayer().getGame() == null) {
        return;
      }
    } else {
      return;
    }
    if (client.getPlayer().getGame().getCurrentPlayer() != null) {
      client.getPlayer().getGame().getCurrentPlayer().getTimer().cancel();
    }
    client.getPlayer().getGame().getPlayers().remove(client.getPlayer());
    client.getPlayer().getGame().getSpectators().remove(client.getPlayer());
    client.getPlayer().getGame().removePlayerFromGame(client.getPlayer());

    MoleGames.getMoleGames().getGameHandler().getClientGames().remove(client);
    System.out.println("Client with id: " + client.getThreadID() + " left the game!");
    client.setPlayer(new Player(client));
  }

  /**
   * @param client
   * @param threadID the threadID of the client that will be sent to the client to give him an id
   * @author Carina
   * @see Client
   */
  public void welcomePacket(@NotNull final ServerThread client, final int threadID) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.WELCOME.getPacketType());
    json.addProperty("clientID", threadID);
    json.addProperty("magic", "mole42");
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param packet
   * @param client
   * @author Carina
   * @use handles the login packet from the client
   */
  private void handleLoginPacket(@NotNull final ServerThread client, @NotNull final Packet packet) {
    var name = (String) null;
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
      for (var i = 1;
          i < MoleGames.getMoleGames().getServer().getConnectionNames().size() + 1;
          i++) {
        var in = false;
        for (var clientName : MoleGames.getMoleGames().getServer().getConnectionNames().keySet()) {
          if (clientName.equalsIgnoreCase(name + i)) {
            in = true;
            break;
          }
        }
        if (!in) {
          client.setClientName(name + i);
          MoleGames.getMoleGames()
              .getServer()
              .getConnectionNames()
              .put(client.getClientName(), client);
          break;
        }
      }
    }
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      System.out.println(
          "Client with id "
              + client.getThreadID()
              + " got the name "
              + client.getClientName()
              + " and logged in!");
    }
    client.setPlayer(new Player(client));
    if (PlayerManagement.getPlayerManagement() != null) {
      PlayerManagement.getPlayerManagement().updateTable();
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
    var json = new JsonObject();
    object.addProperty("type", Packets.ASSIGNEDTOGAME.getPacketType());
    json.addProperty("gameID", gameID);
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends the score of the client to the client
   * @see Score
   */
  private void scoreNotificationPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.SCORENOTIFICATION.getPacketType());
    json.add(
        "score",
        JsonParser.parseString(new Gson().toJson(client.getPlayer().getGame().getScore())));
    object.add("value", json);
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use sends the overview to the clients
   * @see Game
   * @see Tournament
   */
  public void overviewPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.OVERVIEW.getPacketType());
    json.add(
        "games",
        JsonParser.parseString(
            new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getGames())));
    json.add(
        "tournaments",
        JsonParser.parseString(
            new Gson().toJson(MoleGames.getMoleGames().getGameHandler().getTournaments())));
    object.add("value", json);
    client.sendPacket(new Packet(object));
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
    client.getServer().getLobbyThreads().remove(client);
    MoleGames.getMoleGames().getServer().getObserver().remove(client);
    MoleGames.getMoleGames().getServer().getConnectionNames().remove(client.getClientName());
    MainGUI.getGUI().updateTable();
  }

  /**
   * @param client
   * @author Carina
   * @see Game
   * @see Player
   * @see Client
   */
  public boolean handleJoinGamePacket(
      @NotNull final ServerThread client, @NotNull final Packet packet) throws NotAllowedError {
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
        if (client.getPlayer().getGame() != null) {
          if (client.getPlayer().getGame().getPlayers().contains(client.getPlayer())) {
            System.out.println("Client " + client.getClientName() + " is already in a game!");
            return false;
          }
        }
        if (game.getCurrentGameState() == GameStates.NOT_STARTED) {
          if (game.getPlayers().size() < game.getSettings().getMaxPlayers()) {
            MoleGames.getMoleGames()
                .getServer()
                .getPacketHandler()
                .assignToGamePacket(client, game.getGameID());
            game.joinGame(client, false);
            return true;
          }
        }
      } else {
        if (client.getPlayer().getGame() != null) {
          if (client.getPlayer().getGame().getSpectators().contains(client.getPlayer())) {
            return false;
          }
        }
        if (!game.getCurrentGameState().equals(GameStates.OVER)) {
          game.joinGame(client, true);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @param client
   * @return the packet that will be sent to the client
   * @author Carina
   * @use get a player joined tournament packet to send it to the clients of the tournament
   */
  public Packet playerJoinedTournamentPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.TOURNAMENTPLAYERJOINED.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    object.add("value", json);
    return new Packet(object);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a tournament
   */
  private void handleEnterTournamentPacket(
      @NotNull final ServerThread client, @NotNull final Packet packet) {
    var tournament =
        MoleGames.getMoleGames()
            .getGameHandler()
            .getIDTournaments()
            .get(packet.getValues().get("tournamentID").getAsInt());
    tournament.joinTournament(client, packet.getValues().get("participant").getAsBoolean());
    tournamentStateResponsePacket(client);
    MoleGames.getMoleGames()
        .getServer()
        .sendToAllTournamentClients(tournament, playerJoinedTournamentPacket(client));
  }

  /**
   * @param client
   * @author Carina
   * @use sends the welcomePacket to the client when he joins a game
   */
  public void welcomeGamePacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.WELCOMEGAME.getPacketType());
    json.add(
        "gameState",
        JsonParser.parseString(new Gson().toJson(client.getPlayer().getGame().getGameState())));
    object.add("value", json);
    if (client.getPlayer().getGame().getActivePlayers().contains(client.getPlayer())) {
      playerJoinedPacket(client);
    }
    client.sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use calls when a player joined the game sending the message to the clients of the game
   */
  public void playerJoinedPacket(@NotNull final ServerThread client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.PLAYERJOINED.getPacketType());
    json.add("player", JsonParser.parseString(new Gson().toJson(client.getPlayer())));
    object.add("value", json);
    client.getPlayer().getGame().getPlayers().remove(client.getPlayer());
    MoleGames.getMoleGames()
        .getServer()
        .sendToAllGameClients(client.getPlayer().getGame(), new Packet(object));
    client.getPlayer().getGame().getPlayers().add(client.getPlayer());
    for (var ignored : MoleGames.getMoleGames().getServer().getObserver()) overviewPacket(ignored);
  }
}
