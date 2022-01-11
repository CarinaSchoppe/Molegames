/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 11.01.22, 20:01 by Carina Latest changes made by Carina on 11.01.22, 19:55 All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.networking.ClientThread;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.NetworkThread;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.map.Field;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;

@Getter
@Setter
public class Player {

  private final String name;
  private final int clientID;
  private final transient HashSet<Mole> moles = new HashSet<>();
  private final transient NetworkThread serverClient;
  private final transient ArrayList<Integer> cards = new ArrayList<>();
  private transient Game game;
  private transient Timer timer = new Timer();
  private transient long startRemainingTime;
  private transient boolean timerIsRunning = false;
  private transient PlayerUtil playerUtil;
  private transient boolean drawAgain = false;

  /**
   * @param client the serverClient connection established by the Server
   * @author Carina
   * @use will only be created on joining a Game
   * @see Game
   * @see ServerThread
   */
  public Player(@NotNull final ServerThread client) {
    this.serverClient = client;
    this.name = (client.getClientName());
    this.clientID = client.getThreadID();
  }

  /**
   * @param client the serverClient connection established by the Server
   * @author Carina
   * @use will only be created on joining a Game
   * @see Game
   * @see ClientThread
   */
  public Player(@NotNull final Client client) {
    this.serverClient = client.getClientThread();
    this.name = (client.getName());
    this.clientID = client.getClientThread().getThreadID();
  }

  @Override
  public String toString() {
    return "Playermodel with the name: " + name + " and clientID: " + getClientID() + "";
  }

  /**
   * @param game
   * @author Carina
   * @use creates a player with all needed stuff
   */
  public Player create(@NotNull final Game game) {
    this.game = game;
    game.getScore().getPoints().put(this.getServerClient().getThreadID(), 0);
    cards.addAll(game.getSettings().getPullDiscs());
    this.playerUtil = new PlayerUtil(this);
    return this;
  }

  /**
   * @param x_start the x-coordinate of the start field
   * @param y_start the y-coordinate of the start field
   * @param x_end   the x-coordinate of the end field
   * @param y_end   the y-coordinate of the end field
   * @return true if the move is valid
   * @author Carina
   * @use will check if a field is valid and if the player has the right to move the mole
   * @see Mole
   * @see Player
   * @see Field
   * @see GameLogic
   */
  public synchronized void moveMole(
    final int x_start, final int y_start, final int x_end, final int y_end, final int cardValue) {
    if (!game.getCurrentPlayer().equals(this)) {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println(
          "current " + game.getCurrentPlayer().getName() + " who moved: " + this.getName());
      }
      return;
    }
    if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(),
      new int[]{x_start, y_start}, new int[]{x_end, y_end}, cardValue, game.getMap())) {
      var mole = (Mole) null;
      for (var m : moles) {
        if (m.getPosition().getX() == x_start
          && m.getPosition().getY() == y_start
          && m.getPlayer().getServerClient().getThreadID() == getServerClient().getThreadID()) {
          mole = m;
          moles.remove(m);
          break;
        }
      }
      cards.remove(Integer.valueOf(cardValue));
      if (cards.isEmpty()) {
        playerUtil.refillCards();
      }
      MoleGames.getMoleGames()
        .getServer()
        .sendToAllGameClients(
          game,
          MoleGames.getMoleGames()
            .getServer()
            .getPacketHandler()
            .moleMovedPacket(
              new Field(x_start, y_start), new Field(x_end, y_end), cardValue));
      if (mole != null) {
        mole.setPosition(game.getMap().getFieldMap().get(List.of(x_end, y_end)));
        game.getMap().getFieldMap().get(List.of(x_start, y_start)).setOccupied(false);
        game.getMap().getFieldMap().get(List.of(x_end, y_end)).setOccupied(true);
        game.getMap().getFieldMap().get(List.of(x_end, y_end)).setMole(mole);
        game.getMap().getFieldMap().get(List.of(x_start, y_start)).setMole(null);
        moles.add(mole);
        if (MoleGames.getMoleGames().getServer().isDebug()) {
          var start = new Field(x_start, y_start);
          var end = new Field(x_end, y_end);
          System.out.println(
            "Playermodel with id: "
              + serverClient.getThreadID()
              + " has moved his mole from: "
              + start + " to: "
              + end
              + " with a card="
              + cardValue
              + "."
              + "\n");
        }
      }
      if (game.getMap().getFieldMap().get(List.of(x_end, y_end)).isDrawAgainField()) {
        setDrawAgain(true);
      }
      playerUtil.handleTurnAfterAction();
    } else {
      MoleGames.getMoleGames()
        .getGameHandler()
        .getGameLogic()
        .performPunishment(this, Punishments.INVALIDMOVE);
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println(
          "Client with id: "
            + serverClient.getThreadID()
            + " has done in invalid move Punishment: "
            + game.getSettings().getPunishment()
            + " player tried to move from X,Y: ["
            + x_start
            + ","
            + y_start
            + "] to X,Y: ["
            + x_end
            + ","
            + y_end
            + "] with a card of "
            + cardValue
            + "\n");
      }
      timer.cancel();
      game.getGameUtil().nextPlayer();
    }
  }

  /**
   * @param x the x coordinate where a mole will be placed
   * @param y the y coordinate where a mole will be placed
   * @author Carina
   * @use will check if a field is free, then set the mole on this field
   * @see Mole
   * @see Player
   * @see Field
   */
  public synchronized void placeMole(final int x, final int y) {
    if (!game.getCurrentPlayer().equals(this)
      || moles.size() >= game.getSettings().getNumberOfMoles()) {
      return;
    }
    if (game.getMap().getFieldMap().get(List.of(x, y)).isOccupied()
      || game.getMap().getFieldMap().get(List.of(x, y)).isHole()) {
      MoleGames.getMoleGames()
        .getGameHandler()
        .getGameLogic()
        .performPunishment(this, Punishments.INVALIDMOVE);
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println(
          "Client with id: "
            + serverClient.getThreadID()
            + " has done in invalid move Punishment: "
            + game.getSettings().getPunishment()
            + " player tried to place a mole on X,Y: ["
            + x
            + ","
            + y
            + "]");
      timer.cancel();
      game.getGameUtil().nextPlayer();
    } else {
      var mole = new Mole(this, game.getMap().getFieldMap().get(List.of(x, y)));
      moles.add(mole);
      game.getMoleMap().put(this, mole);
      game.getMap().getFieldMap().get(List.of(x, y)).setOccupied(true);
      game.getMap().getFieldMap().get(List.of(x, y)).setMole(mole);
      MoleGames.getMoleGames()
        .getServer()
        .sendToAllGameClients(
          game, MoleGames.getMoleGames().getServer().getPacketHandler().molePlacedPacket(mole));
      playerUtil.handleTurnAfterAction();
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println(
          "Playermodel with id: "
            + serverClient.getThreadID()
            + " has placed his mole on x="
            + x
            + " y="
            + y
            + ". ("
            + getMoles().size()
            + "/"
            + game.getSettings().getNumberOfMoles()
            + ")\n");
      }
    }
  }

  public String getPlayerIDAndName() {
    return clientID + "/" + name;
  }

  public boolean isSpectator() {
    return game.getSpectators().contains(this);
  }
}
