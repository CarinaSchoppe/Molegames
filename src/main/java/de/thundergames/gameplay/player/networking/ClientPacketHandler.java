/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
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
import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.gameselection.GameSelection;
import de.thundergames.gameplay.player.ui.gameselection.LobbyObserverGame;
import de.thundergames.gameplay.player.ui.score.LeaderBoard;
import de.thundergames.gameplay.player.ui.tournamentselection.LobbyObserverTournament;
import de.thundergames.gameplay.player.ui.tournamentselection.TournamentSelection;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class ClientPacketHandler {

  protected final Client client;
  protected Timer timer;
  protected boolean timerRunning = false;
  protected Packet packet;

  /**
   * @author Carina
   * @use handles the packet that came in
   * @see PacketHandler the packetHandler by the Server as a reference
   */
  public void handlePacket(@NotNull final Packet packet) {
    this.packet = packet;
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      handleWelcomePacket();
      registerOverviewObserverPacket();
      client.getClientPacketHandler().getOverviewPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        if (client.isDebug() && packet.getValues().get("message") != null)
          System.out.println("Server sended: " + packet.getValues().get("message").getAsString());
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ASSIGNTOGAME.getPacketType())) {
      handleAssignedToGamePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOMEGAME.getPacketType())) {
      handleWelcomeGamePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERJOINED.getPacketType())) {
      handlePlayerJoinedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERLEFT.getPacketType())) {
      handlePlayerLeftPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERKICKED.getPacketType())) {
      handlePlayerKickedFromGame();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.OVERVIEW.getPacketType())) {
      handleOverviewPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.SCORENOTIFICATION.getPacketType())) {
      handleScoreNotificationPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.GAMEHISTORYRESPONE.getPacketType())) {
      handleGameHistoryResponsePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.REMAININGTIME.getPacketType())) {
      handleRemainingTimePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMESTARTED.getPacketType())) {
      handleGameStartedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVER.getPacketType())) {
      handleGameOverPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMECONTINUED.getPacketType())) {
      handleGameContinuedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMECANCELED.getPacketType())) {
      handleGameCanceledPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEPAUSED.getPacketType())) {
      handleGamePausedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERPLACESMOLE.getPacketType())) {
      handlePlayerPlacesMolePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSTURN.getPacketType())) {
      handlePlayersTurnPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEPLACED.getPacketType())) {
      handleMolePlacedPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.MOVEPENALTYNOTIFICATION.getPacketType())) {
      handleMovePentaltyNotificationPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSKIPPED.getPacketType())) {
      handlePlayerSkippedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTLEVEL.getPacketType())) {
      handleNextFloorPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TOURNAMENTSCORE.getPacketType())) {
      handleTournamentScorePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTSTATERESPONSE.getPacketType())) {
      handleTournamentStateResponePacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTPLAYERJOINED.getPacketType())) {
      handleTournamentPlayerJoinedPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTPLAYERJOINED.getPacketType())) {
      handleTournamentPlayerLeftPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTPLAYERKICKED.getPacketType())) {
      handleTournamentPlayerKickedPacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTPLAYERINGAME.getPacketType())) {
      handleTournamentPlayerInGamePacket();
    } else if (packet
        .getPacketType()
        .equalsIgnoreCase(Packets.TOURNAMENTGAMESOVERVIEW.getPacketType())) {
      handleTournamentGamesOverviewPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TOURNAMENTOVER.getPacketType())) {
      handleTournamentOverPacket();
    }
  }

  /**
   * @author Carina
   * @use send to client that he wants the overview for all objects
   */
  public void getOverviewPacket() {
    var object = new JsonObject();
    object.addProperty("type", Packets.GETOVERVIEW.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use handles the client tournament when the tournament is over
   */
  protected void handleTournamentOverPacket() {
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the leftment of a player from the tournament
   */
  protected void handleTournamentPlayerLeftPacket() {
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the new overview of all running tournaments
   */
  protected void handleTournamentGamesOverviewPacket() {
    client
        .getTournaments()
        .addAll(
            new Gson()
                .fromJson(
                    packet.getValues().get("games").getAsString(),
                    new TypeToken<ArrayList<Game>>() {}.getType()));
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles that a player is in game in the tournament
   */
  protected void handleTournamentPlayerInGamePacket() {}

  /**
   * @author Carina
   * @use handles the kick of a player from the tournament
   */
  protected void handleTournamentPlayerKickedPacket() {
    updateTableView();
  }

  /**
   * @author Carina
   * @use sends the leave tournament packet to the server
   */
  public void leaveTournament(final int tournamentID) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.LEAVETOURNAMENT.getPacketType());
    json.addProperty("tournamentID", tournamentID);
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the joining of a player into the tournament
   */
  protected void handleTournamentPlayerJoinedPacket() {
    updateTableView();
    var lobbyObserverTournament = LobbyObserverTournament.getObserver();
    if (lobbyObserverTournament != null) lobbyObserverTournament.showJoiningSuccessfully();
  }

  /**
   * @param tournamentID
   * @param participant
   * @author Carina
   * @use sends the joining of a tournament to the client
   */
  public void enterTournamentPacket(final int tournamentID, final boolean participant) {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.ENTERTOURNAMENT.getPacketType());
    json.addProperty("tournamentID", tournamentID);
    json.addProperty("participant", participant);
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use is called everytime a map gets updated TODO: implement this
   */
  public void updateMap() {}

  /**
   * @author Carina
   * @use handles the movement of a mole send by the server from a client
   */
  protected void handleMoleMovedPacket() {
    if (client.isDebug())
      System.out.println(
          "A mole has been moved"
              + " from "
              + packet.getValues().get("from").getAsString()
              + " to "
              + packet.getValues().get("to").getAsString());
    var from = new Gson().fromJson(packet.getValues().get("from").getAsString(), Field.class);
    var to = new Gson().fromJson(packet.getValues().get("to").getAsString(), Field.class);
    client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).setOccupied(false);
    client.getMap().getFieldMap().get(List.of(to.getX(), to.getY())).setOccupied(true);
    var moleObject = client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).getMole();

    client.getMap().getFieldMap().get(List.of(to.getX(), to.getY())).setMole(moleObject);
    client.getMap().getFieldMap().get(List.of(from.getX(), from.getY())).setMole(null);

    if (moleObject == null) {
      System.out.println("mole is null");
      System.exit(9);
    }
    for (var mole : client.getMoles()) {
      if (mole.getField().getX() == from.getX() && mole.getField().getY() == from.getY()) {
        mole.setField(to);
        break;
      }
    }
    updateMap();
  }

  /**
   * @author Carina
   * @use handles the tournament score send by the server
   */
  protected void handleTournamentScorePacket() {}

  /**
   * @author Carina
   * @use handles when the client gets the new floor
   */
  protected void handleNextFloorPacket() {
    if (client.isDebug()) System.out.println("Client got the new level!");
    var players =
        new ArrayList<Player>(
            new Gson()
                .fromJson(
                    packet.getValues().get("eliminatedPlayers").getAsString(),
                    new TypeToken<ArrayList<Player>>() {}.getType()));
    if (client.isDebug()) {
      System.out.println("Players that are out: ");
      for (Player player : players) {
        System.out.println(player);
      }
    }
    handleFloor();
  }

  /**
   * @author Carina
   * @use handles the floor send by the server to do everything to get it ready
   */
  protected void handleFloor() {
    client.getMoles().clear();
    client.setGameState(
        new Gson().fromJson(packet.getValues().get("gameState").getAsString(), GameState.class));

    if (client.getGameState().getPullDiscs().containsKey(client.getClientThread().getThreadID())) {
      client
          .getPullDiscs()
          .addAll(client.getGameState().getPullDiscs().get(client.getClientThread().getThreadID()));
    }
    // Muss gemacht werden damit der code sicher arbeitet
    client.setMap(
        new Map(
            client.getGameState().getFloor().getHoles(),
            client.getGameState().getFloor().getDrawAgainFields(),
            client.getGameState().getFloor().getPoints()));
    client.getMap().build(client.getGameState());
    if (!client.getGameState().getPlacedMoles().isEmpty()) {
      for (var moles : client.getGameState().getPlacedMoles()) {
        System.out.println("ejfessf");
        System.out.println(
            moles.getPlayer().getClientID()
                + " the id of the player own id:"
                + client.getClientThread().getThreadID());
        if (moles.getPlayer().getClientID() == client.getClientThread().getThreadID()) {
          System.out.println("Soa1");
          client.getMoles().add(moles);
        }
      }
    } else {
      System.out.println("MOLESSSSSSSXxxxx");
      System.out.println(packet.getValues().toString());
    }
    updateMap();
    if (client.isDebug()) {
      client.getMap().printMap();
    }
  }
  // TODO: overview öfter aktualliseren

  /**
   * @author Carina
   * @use handles when a player skipped his turn
   */
  protected void handlePlayerSkippedPacket() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    if (client.isDebug()) {
      if (player.equals(client.getPlayer())) {
        System.out.println("Client is skipping this turn!");
      } else {
        System.out.println("The Playermodel " + player.getName() + " skipped his turn!");
      }
    }
  }

  /**
   * @author Carina
   * @use handles the response after joining a tournament
   */
  protected void handleTournamentStateResponePacket() {
    // TODO: hier response einfügen
  }

  /**
   * @author Carina
   * @use handles if a client did an invalid handling with a punishment
   */
  protected void handleMovePentaltyNotificationPacket() {
    if (client.isDebug())
      System.out.println(
          "The client "
              + new Gson()
                  .fromJson(packet.getValues().get("player").getAsString(), Player.class)
                  .getName()
              + " got a move penalty for the reason"
              + packet.getValues().get("reason").getAsString());
  }

  /**
   * @author Carina
   * @use handles the placement of a mole
   */
  protected void handleMolePlacedPacket() {
    var mole = new Gson().fromJson(packet.getValues().get("mole").getAsString(), Mole.class);
    client
        .getMap()
        .getFieldMap()
        .get(List.of(mole.getField().getX(), mole.getField().getY()))
        .setOccupied(true);
    client
        .getMap()
        .getFieldMap()
        .get(List.of(mole.getField().getX(), mole.getField().getY()))
        .setMole(mole);
    if (mole == null) {
      System.out.println("mole is null");
      System.exit(9);
    }
    client.getGameState().getPlacedMoles().add(mole);
    updateMap();
  }

  /**
   * @author Carina
   * @use handles if the player is now on the turn
   */
  protected void handlePlayersTurnPacket() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    client.setCurrentPlayer(player);
    if (player.getClientID() == client.getClientThread().getThreadID()) {
      if (client.isDebug()) System.out.println("Client is now on the turn!");
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
                  if (client.isDebug()) System.out.println("You ran out of time!");
                }
              }
            },
            packet.getValues().get("until").getAsLong() - System.currentTimeMillis());
      }
    } else {
      if (client.isDebug())
        System.out.println(
            "Client: the player with the id: "
                + player.getClientID()
                + " and name: "
                + player.getName()
                + " is now on the turn!");
    }
  }

  /**
   * @param start
   * @param end
   * @param pullDisc
   * @author Carina
   * @use sends the movement of a mole to the server
   */
  public void makeMovePacket(final int[] start, final int[] end, final int pullDisc) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("from", new Gson().toJson(new Field(start[0], start[1])));
    json.addProperty("to", new Gson().toJson(new Field(end[0], end[1])));
    json.addProperty("pullDisc", pullDisc);
    object.add("value", json);
    object.addProperty("type", Packets.MAKEMOVE.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @param field
   * @author Carina
   * @use handles that the player wants to place a mole at the position
   * @see de.thundergames.playmechanics.util.Player
   * @see Field
   */
  public void placeMolePacket(@NotNull final Field field) {
    var object = new JsonObject();
    object.addProperty("type", Packets.PLACEMOLE.getPacketType());
    var json = new JsonObject();
    json.addProperty("position", new Gson().toJson(field));
    object.add("value", json);
    client.getMoles().add(new Mole(client.getPlayer(), field));
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use handles that this player need to place a mole till the turnTime ends
   * @see de.thundergames.playmechanics.game.Game
   * @see de.thundergames.playmechanics.util.Player
   * @see de.thundergames.playmechanics.util.Mole
   */
  protected void handlePlayerPlacesMolePacket() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    if (player.getClientID() == client.getClientThread().getThreadID()) {
      if (client.isDebug()) {
        System.out.println("Client is now on to place a mole!");
      }
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
                  if (client.isDebug()) System.out.println("You ran out of time!");
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
                    .fromJson(packet.getValues().get("player").getAsString(), Player.class)
                    .getName()
                + " needs to place a mole till: "
                + packet.getValues().get("until").getAsInt());
    }
  }

  /**
   * @author Carina
   * @use handles that the game of the client was paused
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGamePausedPacket() {
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles that a game was canceled
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGameCanceledPacket() {
    handleGameOverPacket();
  }

  /**
   * @author Carina
   * @use handles that game is now continued
   * @see de.thundergames.playmechanics.game.Game
   */
  protected void handleGameContinuedPacket() {
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles that the game of the client is over
   */
  protected void handleGameOverPacket() {
    var score = new Gson().fromJson(packet.getValues().get("result").getAsString(), Score.class);
    if (client.isDebug()) {
      System.out.println(
          "Client: game with the id: "
              + client.getGameID()
              + " has ended! Winners are: "
              + score.getWinners());
      for (var player : score.getPlayers()) {
        System.out.println(
            "Client: player with the name: "
                + player.getName()
                + " has points: "
                + score.getPoints().get(player.getClientID()));
      }
    }
    try {
      new Thread(() -> new LeaderBoard().create(score)).start();
    } catch (IllegalStateException ignored) {
    }
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the packet that a game has started
   */
  protected void handleGameStartedPacket() {
    updateTableView();
    var lobbyObserverGame = LobbyObserverGame.getObserver();
    if (lobbyObserverGame != null) {
      lobbyObserverGame.spectateGame();
    }
    var lobbyObserverTournamentGame = LobbyObserverTournament.getObserver();
    if (lobbyObserverTournamentGame != null) {
      lobbyObserverTournamentGame.spectateGame();
    }
  }

  /**
   * @author Carina
   * @use handles the remaining Time of the client for a turn
   */
  protected void handleRemainingTimePacket() {
    client.setRemainingTime(packet.getValues().get("timeLeft").getAsInt());
  }

  /**
   * @author Carina
   * @use handles the historyResponsePacket from the server
   */
  protected void handleGameHistoryResponsePacket() {}

  /**
   * @author Carina
   * @use handles that the server send this client the score of the game
   */
  protected void handleScoreNotificationPacket() {
    client
        .getGameState()
        .setScore(new Gson().fromJson(packet.getValues().get("score").getAsString(), Score.class));
  }

  /**
   * @author Carina
   * @use send to the server that client wants to get the score
   */
  public void getScorePacket() {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETSCORE.getPacketType());
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @author Carina
   * @use send to the server to get the GameHistory
   */
  public void getGameHistoryPacket(final int gameID) {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETGAMEHISTORY.getPacketType());
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    jsonObject.add("value", json);
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @author Carina
   * @use sends to the server that the client wants to get the remaining time for this turn
   */
  public void getRemainingTimePacket() {
    var jsonObject = new JsonObject();
    jsonObject.addProperty("type", Packets.GETREMAININGTIME.getPacketType());
    client.getClientThread().sendPacket(new Packet(jsonObject));
  }

  /**
   * @author Carina
   * @use handles that a player left the game
   */
  protected void handlePlayerLeftPacket() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    if (client.isDebug()) System.out.println("A player has left the Game + " + player);
    client.getGameState().getActivePlayers().remove(player);
    updateTableView();
  }

  /***
   * @author Carina
   * @use handles that a player was kicked from the game
   */
  protected void handlePlayerKickedFromGame() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    if (client.isDebug()) System.out.println("A player has left the Game + " + player);
    client.getGameState().getActivePlayers().remove(player);
    updateTableView();
  }

  /**
   * @author Carina
   * @use send to the server that this client wants to leave a game
   */
  public void leaveGamePacket() {
    var object = new JsonObject();
    object.addProperty("type", Packets.LEAVEGAME.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
    if (client.isDebug()) System.out.println("Client: left the game!");
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the packet that a player joined the game
   */
  protected void handlePlayerJoinedPacket() {
    var player = new Gson().fromJson(packet.getValues().get("player").getAsString(), Player.class);
    if (client.isDebug()) {
      if (player.getClientID() != client.getClientThread().getThreadID()) {
        System.out.println(
            "The player: " + player.getName() + " has joined the Game " + client.getGameID() + ".");
      }
    }
    client.getGameState().getActivePlayers().add(player);
    updateTableView();
    var lobbyObserverGame = LobbyObserverGame.getObserver();
    if (lobbyObserverGame != null) lobbyObserverGame.showNewPlayer();
  }

  /**
   * @author Carina
   * @use handles the welcomeGamePacket from the server
   */
  protected void handleWelcomeGamePacket() {
    if (client instanceof AI) {
      ((AI) client).setPlacedMolesAmount(0);
      ((AI) client).setPlacedMoles(false);
    }
    System.out.println(packet.getValues().get("gameState").getAsString());
    handleFloor();
  }

  /**
   * @author Carina
   * @use send by server to welcome new client connection with the clientID
   */
  protected void handleWelcomePacket() {
    if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
      System.exit(3);
    }
    client.getClientThread().setThreadID(packet.getValues().get("clientID").getAsInt());
    if (client.isDebug()) System.out.println("Client connected sucessfully to the Server!");
    client.setPlayer(new Player(client));
  }

  /**
   * @param name
   * @author Carina
   * @use sends the login packet to the server and wants response with welcome
   */
  public void loginPacket(@NotNull final String name) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("name", name);
    object.addProperty("type", Packets.LOGIN.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Marc
   * @use sends the logout packet to the server
   */
  public void logoutPacket() {
    var object = new JsonObject();
    var json = new JsonObject();
    object.addProperty("type", Packets.LOGOUT.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * author Carina
   *
   * @use handles the overview packet from the server
   */
  protected void handleOverviewPacket() {
    client.getGames().clear();
    client.getTournaments().clear();
    client
        .getGames()
        .addAll(
            new Gson()
                .fromJson(
                    packet.getValues().get("games").getAsString(),
                    new TypeToken<ArrayList<Game>>() {}.getType()));
    client
        .getTournaments()
        .addAll(
            new Gson()
                .fromJson(
                    packet.getValues().get("tournaments").getAsString(),
                    new TypeToken<ArrayList<Tournament>>() {}.getType()));
    updateTableView();
  }

  /**
   * @author Carina
   * @use handles the joining of a player into the game
   */
  protected void handleAssignedToGamePacket() {
    if (client.isDebug())
      System.out.println(
          "Client joined game with the id: " + packet.getValues().get("gameID").getAsInt());
    client.setGameID(packet.getValues().get("gameID").getAsInt());
    showPlayerJoinedGameLobby();
    updateTableView();
  }

  /**
   * @param gameID
   * @param player
   * @author Carina
   * @use send to the server that this client wants to join a specific game as player or spectator
   * @see de.thundergames.playmechanics.game.Game
   * @see de.thundergames.playmechanics.util.Player
   */
  public void joinGamePacket(final int gameID, final boolean player) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    json.addProperty("participant", player);
    object.addProperty("type", Packets.JOINGAME.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Carina
   * @use send to the server to register as an overview observer
   */
  public void registerOverviewObserverPacket() {
    var object = new JsonObject();
    object.addProperty("type", Packets.REGISTEROBSERVER.getPacketType());
    client.getClientThread().sendPacket(new Packet(object));
  }

  /**
   * @author Marc and Nick
   * @use send to the server to unregister as an overview observer
   */
  public void unregisterOverviewObserverPacket() {
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
