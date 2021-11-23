/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 14:33 by Carina latest changes made by Carina on 23.11.21, 14:33 All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.map.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import org.jetbrains.annotations.NotNull;

public class Player extends NetworkPlayer {

  private transient final HashSet<Mole> moles = new HashSet<>();
  private transient final ServerThread serverClient;
  private transient final ArrayList<Integer> cards = new ArrayList<>();
  private transient Game game;
  private transient Timer timer;
  private transient long startRemainingTime;
  private transient boolean timerIsRunning = false;
  private transient boolean hasMoved = true;
  private transient PlayerUtil playerUtil;

  /**
   * @param client the serverClient connection established by the Server
   * @author Carina
   * @use will only be created on joining a Game
   * @see Game
   * @see ServerThread
   */
  public Player(@NotNull final ServerThread client) {
    super(client.getClientName(), client.getConnectionID());
    this.serverClient = client;

  }

  /**
   * @param game
   * @author Carina
   * @use creates a player with all needed stuff
   */
  public Player create(@NotNull final Game game) {
    this.game = game;
    game.getScore().getPoints().put(this.getClientID(), 0);
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
  public void moveMole(final int x_start, final int y_start, final int x_end, final int y_end, final int cardValue) {
    if (!game.getCurrentPlayer().equals(this) || hasMoved) {
      return;
    }
    if (GameLogic
        .wasLegalMove(new int[]{x_start, y_start}, new int[]{x_end, y_end}, cardValue, game.getMap())) {
      Mole mole = null;
      for (var m : moles) {
        if (m.getNetworkField().getX() == x_start && m.getNetworkField().getY() == y_start && m.getPlayer().getClientID() == getClientID()) {
          mole = m;
          break;
        }
      }
      cards.remove(Integer.valueOf(cardValue));
      if (cards.isEmpty()) {
        playerUtil.refillCards();
      }
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().moleMovedPacket(new NetworkField(x_start, y_start), new NetworkField(x_end, y_end), cardValue));
      Objects.requireNonNull(mole).setMoleField(game.getMap().getFieldMap().get(List.of(x_end, y_end)));
      game.getMap().getFieldMap().get(List.of(x_start, y_start)).setOccupied(false);
      game.getMap().getFieldMap().get(List.of(x_end, y_end)).setOccupied(true);
      game.getMap().getFieldMap().get(List.of(x_end, y_end)).setMole(mole);
      game.getMap().getFieldMap().get(List.of(x_start, y_start)).setMole(null);
      for (var m : moles) {
        if (m.getNetworkField().getX() == x_start && m.getNetworkField().getY() == y_start) {
          moles.remove(m);
          moles.add(mole);
        }
      }
      game.getGameUtil().givePoints(game.getMap().getFieldMap().get(List.of(x_end, y_end)), this);

      mole.setNetworkField(new NetworkField(x_end, y_end));
      System.out.println(
          "Player with id: "
              + serverClient.getConnectionID()
              + " has moved his mole from: x="
              + x_start
              + " y="
              + y_start
              + " to x="
              + x_end
              + " y="
              + y_end
              + " with a card=" + cardValue + "." + "\n\n");
      playerUtil.handleTurnAfterAction();
    } else {
      System.out.println(
          "Client with id: "
              + serverClient.getConnectionID()
              + " has done in invalid move Punishment: "
              + game.getSettings().getPunishment() +
              " player tried to move from X,Y: [" + x_start + "," + y_start + "] to X,Y: [" + x_end + "," + y_end + "] with a card of " + cardValue + "\n\n");
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().movePenaltyNotification(this, game.getSettings().getPunishment(), "INVALID_MOVE"));
      timer.purge();
      timer.cancel();
      game.getGameUtil().nextPlayer();

    }
  }

  /**
   * @param x the x cordinate where a mole will be placed
   * @param y the y cordinate where a mole will be placed
   * @author Carina
   * @use will check if a field is free than set the mole on this field
   * @see Mole
   * @see Player
   * @see Field
   */
  public void placeMole(final int x, final int y) {
    if (!game.getCurrentPlayer().equals(this) || hasMoved || moles.size() >= game.getSettings().getNumberOfMoles()) {
      return;
    }
    if (game.getMap().getFieldMap().get(List.of(x, y)).isOccupied()
        || game.getMap().getFieldMap().get(List.of(x, y)).isHole()) {
    } else {
      var mole = new Mole(this, new Field(List.of(x, y)));
      moles.add(mole);
      game.getMoleMap().put(this, mole);
      game.getMap().getFieldMap().get(List.of(x, y)).setOccupied(true);
      game.getMap().getFieldMap().get(List.of(x, y)).setMole(mole);
      var field = new NetworkField(mole.getNetworkField().getX(), mole.getNetworkField().getY());
      var player = new NetworkPlayer(mole.getPlayer().getName(), mole.getPlayer().getClientID());
      var netMole = new NetworkMole(player, field);
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().molePlacedPacket(netMole));
      playerUtil.handleTurnAfterAction();
      System.out.println(
          "Player with id: "
              + serverClient.getConnectionID()
              + " has placed his mole on x="
              + x
              + " y="
              + y
              + ". (" + getMoles().size() + "/" + game.getSettings().getNumberOfMoles() + ")\n\n");
    }

  }

  public void setHasMoved(boolean hasMoved) {
    this.hasMoved = hasMoved;
  }

  public boolean isTimerIsRunning() {
    return timerIsRunning;
  }

  public void setTimerIsRunning(boolean timerIsRunning) {
    this.timerIsRunning = timerIsRunning;
  }

  public Game getGame() {
    return game;
  }

  public PlayerUtil getPlayerUtil() {
    return playerUtil;
  }

  public Timer getTimer() {
    return timer;
  }

  public void setTimer(Timer timer) {
    this.timer = timer;
  }

  public ArrayList<Integer> getCards() {
    return cards;
  }

  public HashSet<Mole> getMoles() {
    return moles;
  }

  public ServerThread getServerClient() {
    return serverClient;
  }

  public long getRemainingTime() {
    return startRemainingTime;
  }

  public void setStartRemainingTime(long startRemainingTime) {
    this.startRemainingTime = startRemainingTime;
  }
}
