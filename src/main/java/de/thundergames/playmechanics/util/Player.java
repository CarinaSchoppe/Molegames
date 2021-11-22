/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 22.11.21, 14:42 All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.map.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.NotNull;

public class Player extends NetworkPlayer {

  private transient final ArrayList<Mole> moles = new ArrayList<>();
  private transient final ServerThread serverClient;
  private transient final Game game;
  private transient final ArrayList<Integer> cards = new ArrayList<>();
  private transient int drawCard = 0;
  private transient Timer timer;
  private long startRemainingTime;
  private transient boolean timerIsRunning = false;
  private transient boolean canDraw = false;
  private transient boolean hasMoved = true;
  private transient int points;

  /**
   * @param client the serverClient connection established by the Server
   * @param game   the Game a player joined
   * @author Carina
   * @use will only be created on joining a Game
   * @see Game
   * @see ServerThread
   */
  public Player(@NotNull final ServerThread client, @NotNull final Game game) {
    super(client.getClientName(), client.getConnectionID());
    this.serverClient = client;
    this.game = game;
    cards.addAll(game.getSettings().getPullDiscs());

  }


  /**
   * @author Carina
   * @use will be called when a player wants to draw a card and it takes the current first card and puts it afterwards to the back of the list
   * @see Settings
   */
  public void nextCard() {
    drawCard = cards.get(0);
    cards.remove(0);
    refillCards();
  }

  public void refillCards() {
    if (cards.isEmpty()) {
      cards.addAll(game.getSettings().getPullDiscs());
    }
  }

  public void takeCard(int cardValue) {
    drawCard = cardValue;
    cards.remove(cardValue);
    refillCards();
  }

  /**
   * @author Carina
   * @use startes the time a player got for its move
   * @see Settings
   */
  public void startThinkTimer() {
    setStartRemainingTime(System.currentTimeMillis());
    hasMoved = false;
    timer = new Timer();
    canDraw = true;
    timerIsRunning = true;
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            canDraw = false;
            hasMoved = true;
            timerIsRunning = false;
            System.out.println("client " + getServerClient().getClientName() + " ran out of time");
            game.nextPlayer();
          }
        },
        game.getSettings().getTurnTime());
  }

  /**
   * @return the canDraw
   * @author carina
   * @use when called draws a card and returns it depending on
   * @see Settings if the card should be taken in order or randomly if cards a empty refill by the oder
   */
  public void drawACard() {
    if (!game.getCurrentPlayer().equals(this) || hasMoved || !canDraw) {
      return;
    }
    if (!game.getSettings().isPullDiscsOrdered()) {
      nextCard();
    } else {
      drawCard = cards.get(0);
    }
    //TODO: hier MoleGames.getMoleGames().getPacketHandler().drawnPlayerCardPacket(serverClient, drawCard);
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
    if (MoleGames.getMoleGames()
        .getGameLogic()
        .wasLegalMove(
            List.of(x_start, y_start),
            List.of(x_end, y_end),
            cardValue,
            game.getMap())) { // TODO: drawCard - 3
      Mole mole = null;
      for (var m : moles) {
        if (m.getField().getX() == x_start && m.getField().getY() == y_start) {
          mole = m;
          break;
        }
      }
      Objects.requireNonNull(mole).setMoleField(game.getMap().getFieldMap().get(List.of(x_end, y_end)));
      game.getMap()
          .getFieldMap()
          .get(List.of(x_start, y_start))
          .setOccupied(false);
      game.getMap()
          .getFieldMap()
          .get(List.of(x_end, y_end))
          .setOccupied(true);
      game.getMap()
          .getFieldMap()
          .get(List.of(x_end, y_end))
          .setMole(mole);
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
              + " with a card=" + drawCard + "." + "\n\n");
      handleTurnAfterAction();
    } else {
      System.out.println(
          "Client with id: "
              + serverClient.getConnectionID()
              + " has done in invalid move Punishment: "
              + game.getSettings().getPunishment() +
              " player tried to move from X,Y: [" + x_start + "," + y_start + "] to X,Y: [" + x_end + "," + y_end + "] with a card of " + drawCard + "\n\n");
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().movePenaltyNotification(this, game.getSettings().getPunishment(), "INVALID_MOVE"));
      timer.purge();
      timer.cancel();
      game.nextPlayer();

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
      var mole = new Mole(this, game.getMap().getFieldMap().get(List.of(x, y)));
      moles.add(mole);
      game.getMoleMap().put(this, mole);
      game.getMap().getFieldMap().get(List.of(x, y)).setOccupied(true);
      game.getMap().getFieldMap().get(List.of(x, y)).setMole(mole);
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().molePlacedPacket(mole));
      game.getMap().printMap();
      handleTurnAfterAction();
      System.out.println(
          "Player with id: "
              + serverClient.getConnectionID()
              + " has placed his mole on x="
              + x
              + " y="
              + y
              + "." + "\n\n");
    }

  }

  private void handleTurnAfterAction() {
    canDraw = false;
    hasMoved = true;
 /*TODO:    for (var connection : game.getAIs()) {
      game.getMap().sendMap(connection);
    }*/
    if (timerIsRunning) {
      timer.purge();
      timer.cancel();
      game.nextPlayer();
    }
  }


  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public Game getGame() {
    return game;
  }

  public ArrayList<Integer> getCards() {
    return cards;
  }

  public ArrayList<Mole> getMoles() {
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
