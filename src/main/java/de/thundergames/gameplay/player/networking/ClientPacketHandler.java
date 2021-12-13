/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:18 by Carina latest changes made by Carina on 06.12.21, 22:18
 * All contents of "ClientPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.TournamentSelection.LobbyObserverTournament;
import de.thundergames.gameplay.player.ui.TournamentSelection.TournamentSelection;
import de.thundergames.gameplay.player.ui.gameselection.GameSelection;
import de.thundergames.gameplay.player.ui.gameselection.LobbyObserverGame;
import de.thundergames.gameplay.player.ui.score.LeaderBoard;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.interfaceitems.NetworkField;
import de.thundergames.networking.util.interfaceitems.NetworkGame;
import de.thundergames.networking.util.interfaceitems.NetworkMole;
import de.thundergames.networking.util.interfaceitems.NetworkPlayer;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.Tournament;
import de.thundergames.playmechanics.map.Map;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClientPacketHandler {

  protected Timer timer;
  private boolean timerRunning = false;

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
      client.getClientPacketHandler().getOverviewPacket(client);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        if (client.isDebug() && packet.getValues().get("message") != null)
          System.out.println("Server sended: " + packet.getValues().get("message").getAsString());
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ASSIGNTOGAME.getPacketType())) {
      handleAssignedToGamePacket(client, packet);
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
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.GAMEHISTORYRESPONE.getPacketType())) {
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
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.MOVEPENALTYNOTIFICATION.getPacketType())) {
      handleMovePentaltyNotificationPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSKIPPED.getPacketType())) {
      handlePlayerSkippedPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTLEVEL.getPacketType())) {
      handleNextFloorPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TOURNAMENTSCORE.getPacketType())) {
      handleTournamentScorePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTSTATERESPONSE.getPacketType())) {
      handleTournamentStateResponePacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTPLAYERJOINED.getPacketType())) {
      handleTournamentPlayerJoinedPacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTPLAYERJOINED.getPacketType())) {
      handleTournamentPlayerLeftPacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTPLAYERKICKED.getPacketType())) {
      handleTournamentPlayerKickedPacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTPLAYERINGAME.getPacketType())) {
      handleTournamentPlayerInGamePacket(client, packet);
    } else if (packet
      .getPacketType()
      .equalsIgnoreCase(Packets.TOURNAMENTGAMESOVERVIEW.getPacketType())) {
      handleTournamentGamesOverviewPacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TOURNAMENTOVER.getPacketType())) {
      handleTournamentOverPacket(client);
    }
  }

  /**
   * @param client
   * @author Carina
   * @use send to client that he wants the overview for all objects
   */
  public void getOverviewPacket(@NotNull final Client client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.GETOVERVIEW.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param client
   * @author Carina
   * @use handles the client tournament when the tournament is over
   */
  protected void handleTournamentOverPacket(@NotNull final Client client) {
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the leftment of a player from the tournament
   */
  protected void handleTournamentPlayerLeftPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the new overview of all running tournaments
   */
  protected void handleTournamentGamesOverviewPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    client
      .getTournaments()
      .addAll(
        new Gson().fromJson(packet.getValues().get("games").getAsString(), new TypeToken<ArrayList<NetworkGame>>() {
        }.getType()));
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that a player is in game in the tournament
   */
  protected void handleTournamentPlayerInGamePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the kick of a player from the tournament
   */
  protected void handleTournamentPlayerKickedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
  }

  /**
   * @param client
   * @author Carina
   * @use sends the leave tournament packet to the server
   */
  public void leaveTournament(@NotNull final Client client, final int tournamentID) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.LEAVETOURNAMENT.getPacketType());
    json.addProperty("tournamentID", tournamentID);
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a player into the tournament
   */
  protected void handleTournamentPlayerJoinedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
    var lobbyObserverTournament = LobbyObserverTournament.getObserver();
    if (lobbyObserverTournament != null) lobbyObserverTournament.showJoiningSuccessfully();
  }

  /**
   * @param client
   * @param tournamentID
   * @param participant
   * @author Carina
   * @use sends the joining of a tournament to the client
   */
  public void enterTournamentPacket(
    @NotNull final Client client, final int tournamentID, final boolean participant) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.ENTERTOURNAMENT.getPacketType());
    json.addProperty("tournamentID", tournamentID);
    json.addProperty("participant", participant);
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param map
   * @author Carina
   * @use is called everytime a map gets updated
   */
  public void updateMap(@NotNull final Map map) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the movement of a mole send by the server from a client
   */
  protected synchronized void handleMoleMovedPacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println(
        "A mole has been moved by: "
          + client.getNetworkPlayer().getClientID()
          + " from "
          + packet.getValues().get("from").getAsString()
          + " to "
          + packet.getValues().get("to").getAsString());
    var from =
      new Gson().fromJson(packet.getValues().get("from").getAsString(), NetworkField.class);
    var to = new Gson().fromJson(packet.getValues().get("to").getAsString(), NetworkField.class);
    client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).setOccupied(false);
    client.getMap().getFieldMap().get(List.of(to.getX(), to.getY())).setOccupied(true);
    var mole = client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).getMole();
    client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).setMole(null);
    client.getMap().getFieldMap().get(List.of(to.getX(), to.getY())).setMole(mole);
    for (var m : client.getMoles()) {
      if (m.getNetworkField().getX() == from.getX() && m.getNetworkField().getY() == from.getY()) {
        client.getMoles().remove(m);
        client.getMoles().add(new NetworkMole(client.getNetworkPlayer(), to));
        break;
      }
    }
    updateMap(client.getMap());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the tournament score send by the server
   */
  protected void handleTournamentScorePacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles when the client gets the new floor
   */
  protected synchronized void handleNextFloorPacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println("Client got the new level!");
    var players = new ArrayList<NetworkPlayer>(new Gson().fromJson(packet.getValues().get("eliminatedPlayers").getAsString(), new TypeToken<ArrayList<NetworkPlayer>>() {
    }.getType()));
    if (client.isDebug()) {
      System.out.println("Players that are out: ");
      for (NetworkPlayer player : players) {
        System.out.println(player);
      }
    }
    handleFloor(client, packet);
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the floor send by the server to do everything to get it ready
   */
  protected synchronized void handleFloor(@NotNull final Client client, @NotNull final Packet packet) {
    client.getMoles().clear();
    client.setGameState(new Gson().fromJson(packet.getValues().get("gameState").getAsString(), GameState.class));
    if (!client.getGameState().getPullDiscs().isEmpty()) {
      if (client.getGameState().getPullDiscs().containsKey(client.getClientThread().getClientThreadID())) {
        client.getPullDiscs().addAll(client.getGameState().getPullDiscs().get(client.getClientThread().getClientThreadID()));
      }
    } //TODO: warum ist das so?!
    client.setMap(new Map(client.getGameState()));
    if (!client.getGameState().getPlacedMoles().isEmpty()) {
      for (var moles : client.getGameState().getPlacedMoles()) {
        if (moles.getPlayer().getClientID() == client.getClientThread().getClientThreadID()) {
          client.getMoles().add(moles);
        }
      }
    }
    updateMap(client.getMap());
    client.getMap().printMap();
  }
  //TODO: overview öfter aktualliseren
  //TODO: Punkte richtig anzeigen
  //TODO: beobachten bei laufendem spiel!

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles when a player skipped his turn
   */
  protected void handlePlayerSkippedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    var player =
      new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class);
    if (client.isDebug()) {
      if (player.equals(client.getNetworkPlayer())) {
        System.out.println("Client is skipping this turn!");
      } else {
        System.out.println("The Playermodel " + player.getName() + " skipped his turn!");
      }
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the response after joining a tournament
   */
  protected void handleTournamentStateResponePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    // TODO: hier response einfügen
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles if a client did an invalid handling with a punishment
   */
  protected void handleMovePentaltyNotificationPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println(
        "The client "
          + new Gson()
          .fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class)
          .getName()
          + " got a move penalty for the reason"
          + packet.getValues().get("reason").getAsString());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the placement of a mole
   */
  protected void handleMolePlacedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    var mole = new Gson().fromJson(packet.getValues().get("mole").getAsString(), NetworkMole.class);
    client
      .getMap()
      .getFieldMap()
      .get(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY()))
      .setOccupied(true);
    client
      .getMap()
      .getFieldMap()
      .get(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY()))
      .setMole(mole);
    client
      .getGameState()
      .getPlacedMoles()
      .add(mole); // TODO: check hier ob das so okay ist oder doch gelöscht werden muss
    updateMap(client.getMap());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles if the player is now on the turn
   */
  protected synchronized void handlePlayersTurnPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    var player =
      new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class);
    if (player.getClientID() == client.getClientThread().getClientThreadID()) {
      if (client.isDebug())
        System.out.println("Client is now on the turn!");
      client.setDraw(true);
      if (!timerRunning) {
        timerRunning = true;
        timer = new Timer();
        timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              if (client.isDraw()) {
                client.setDraw(false);
                timerRunning = false;
                if (client.isDebug())
                  System.out.println("You ran out of time!");
              }
            }
          },
          packet.getValues().get("until").getAsLong() - System.currentTimeMillis());
      }
    } else {
      if (client.isDebug())
        System.out.println("Client: the player with the id: " + player.getClientID() + " and name: " + player.getName() + " is now on the turn!");
    }
  }

  protected boolean isTimerRunning() {
    return timerRunning;
  }

  public void setTimerRunning(boolean timerRunning) {
    this.timerRunning = timerRunning;
  }

  /**
   * @param client
   * @param start
   * @param end
   * @param pullDisc
   * @author Carina
   * @use sends the movement of a mole to the server
   */
  public void makeMovePacket(
    @NotNull final Client client,
    @NotNull final int[] start,
    @NotNull final int[] end,
    final int pullDisc) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("from", new Gson().toJson(new NetworkField(start[0], start[1])));
    json.addProperty("to", new Gson().toJson(new NetworkField(end[0], end[1])));
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
    client.getMoles().add(new NetworkMole(client.getNetworkPlayer(), field));
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
  protected synchronized void handlePlayerPlacesMolePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    var player =
      new Gson().fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class);
    if (player.getClientID() == client.getClientThread().getClientThreadID()) {
      if (client.isDebug())
        System.out.println("Client is now on to place a mole!");
      client.setDraw(true);
      if (!timerRunning) {
        timerRunning = true;
        timer = new Timer();
        timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              if (client.isDraw()) {
                client.setDraw(false);
                timerRunning = false;
                if (client.isDebug())
                  System.out.println("You ran out of time!");
              }
            }
          },
          20000);
      }
    } else {
      if (client.isDebug())
        System.out.println(
          "The Client "
            + new Gson()
            .fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class)
            .getName()
            + " needs to place a mole till: " + packet.getValues().get("until").getAsInt());
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the game of the client was paused
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGamePausedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that a game was canceled
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGameCanceledPacket(@NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
    //TODO: Rufe Leaderboard auf
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that game is now continued
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGameContinuedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the game of the client is over
   */
  protected synchronized void handleGameOverPacket(@NotNull final Client client, @NotNull final Packet packet) {
    var score = new Gson().fromJson(packet.getValues().get("result").getAsString(), Score.class);
    if (!score.getPoints().isEmpty()) {
      var playerIDs = new ArrayList<>(score.getPoints().keySet());
      var players = new ArrayList<>(score.getPlayers());
      var max = Collections.max(score.getPoints().values());
      //sort the players by score
      Collections.sort(players, (o1, o2) -> score.getPoints().get(o2.getClientID()).compareTo(score.getPoints().get(o1.getClientID())));
      for (var player : players) {
        if (score.getPoints().get(player.getClientID()) == max) {
          score.getWinners().add(player);
        }
      }
      if (client.isDebug())
        System.out.println("Server: game with id: " + client.getGameID() + " has ended! Winners are: " + score.getWinners());
    }
    LeaderBoard.create();
    //TODO: Rufe Leaderboard auf unter gewinner sind nun die gewinner des ersten platzes. unter players findest du die reihenfolge der spieler wie sie im score stehen sollten!
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet that a game has started
   */
  protected void handleGameStartedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    updateTableView();
    var lobbyObserverGame = LobbyObserverGame.getObserver();
    if (lobbyObserverGame != null) lobbyObserverGame.spectateGame();
    var lobbyObserverTournamentGame = LobbyObserverTournament.getObserver();
    if (lobbyObserverTournamentGame != null) lobbyObserverTournamentGame.spectateGame();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the remaining Time of the client for a turn
   */
  protected void handleRemainingTimePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    client.setRemainingTime(packet.getValues().get("timeLeft").getAsInt());
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the historyResponsePacket from the server
   */
  protected void handleGameHistoryResponsePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles that the server send this client the score of the game
   */
  protected void handleScoreNotificationPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    client
      .getGameState()
      .setScore(new Gson().fromJson(packet.getValues().get("score").getAsString(), Score.class));
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
  protected void handlePlayerLeftPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println(
        "A player has left the Game + "
          + new Gson()
          .fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class));
    updateTableView();
  }

  /***
   * @author Carina
   * @param client
   * @param packet
   * @use handles that a player was kicked from the game
   */
  protected void handlePlayerKickedFromGame(
    @NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println(
        "A player has left the Game + "
          + new Gson()
          .fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class));
    updateTableView();
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
    if (client.isDebug())
      System.out.println("Client: left the game!");
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet that a player joined the game
   */
  protected void handlePlayerJoinedPacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    var player = new Gson()
      .fromJson(packet.getValues().get("player").getAsString(), NetworkPlayer.class);
    if (client.isDebug()) {
      if (player.getClientID() != client.getClientThread().getClientThreadID()) {
        System.out.println(
          "The player: " + player.getName() + " has joined the Game " + client.getGameID() + ".");
      }
    }
    updateTableView();
    var lobbyObserverGame = LobbyObserverGame.getObserver();
    if (lobbyObserverGame != null) lobbyObserverGame.showNewPlayer();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the welcomeGamePacket from the server
   */
  protected void handleWelcomeGamePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    handleFloor(client, packet);
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
    if (client.isDebug())
      System.out.println("Client connected sucessfully to the Server!");
    client.setNetworkPlayer(
      new NetworkPlayer(client.getName(), client.getClientThread().getClientThreadID()));
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
   * @param client
   * @author Marc
   * @use sends the logout packet to the server
   */
  public void logoutPacket(@NotNull final Client client) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.LOGOUT.getPacketType());
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
    client
      .getGames()
      .addAll(
        new Gson()
          .fromJson(packet.getValues().get("games").getAsString(),
            new TypeToken<ArrayList<NetworkGame>>() {
            }.getType()));
    client
      .getTournaments()
      .addAll(new Gson().fromJson(packet.getValues().get("tournaments").getAsString(),
        new TypeToken<ArrayList<Tournament>>() {
        }.getType()));
    updateTableView();
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a player into the game
   */
  protected void handleAssignedToGamePacket(
    @NotNull final Client client, @NotNull final Packet packet) {
    if (client.isDebug())
      System.out.println(
        "Client joined game with id: " + packet.getValues().get("gameID").getAsInt());
    client.setGameID(packet.getValues().get("gameID").getAsInt());
    showPlayerJoinedGameLobby();
    updateTableView();
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
  public void joinGamePacket(
    @NotNull final Client client, @NotNull final int gameID, final boolean player) {
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

  /**
   * @param client
   * @author Marc and Nick
   * @use send to the server to unregister as an overview observer
   */
  public void unregisterOverviewObserverPacket(@NotNull final Client client) {
    var object = new JsonObject();
    object.addProperty("type", Packets.UNREGISTEROBSERVER.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Marc
   * @use update tableview of game/tournament selection
   */
  private void updateTableView() {
    // Update tableview of GameSelection
    var gameSelection = GameSelection.getGameSelection();
    if (gameSelection != null) gameSelection.updateTable();
    // Update tableview of TournamentSelection
    var tournamentSelection = TournamentSelection.getTournamentSelection();
    if (tournamentSelection != null) tournamentSelection.updateTable();
  }

  /**
   * @author Nick
   * @use show player joined message at game lobby
   */
  private void showPlayerJoinedGameLobby() {
    var lobbyObserverGame = LobbyObserverGame.getObserver();
    if (lobbyObserverGame != null) lobbyObserverGame.showJoiningSuccessfully();
  }
}
