/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "GameUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
public class GameUtil {

  private final Game game;

  /**
   * @return
   * @author Carina
   * @use checks if all holes are filled with moles
   */
  public boolean allHolesFilled() {
    for (var hole : game.getMap().getHoles()) {
      if (!game.getMap().getFieldMap().get(List.of(hole.getX(), hole.getY())).isOccupied()) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return
   * @author Carina
   * @use checks if all moles of a player are in a hole
   */
  public boolean allPlayerMolesInHoles() {
    if (game.getCurrentPlayer().getMoles().isEmpty()) {
      return false;
    }
    for (var moles : game.getCurrentPlayer().getMoles()) {
      if (!moles.getField().isHole()) {
        return false;
      } else {
        System.out.println("field: " + moles.getField().getX() + " " + moles.getField().getY());
      }
    }
    return true;
  }

  /**
   * @author Carina
   * @use sets the next player in the game if all moles are in holes the player is not on turn
   */
  public void nextPlayer() {
    if (game.getActivePlayers().isEmpty()) {
      game.forceGameEnd();
    }
    if (game.getCurrentGameState() == GameStates.OVER
        || game.getCurrentGameState() == GameStates.PAUSED) {
      return;
    }
    // setting the new current player and if current can draw again or not
    if (game.getActivePlayers().size() - 1
        >= game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1) {
      if (game.getCurrentPlayer() != null) {
        if (!game.getCurrentPlayer().isDrawAgain()) {
          game.setCurrentPlayer(
              game.getActivePlayers()
                  .get(game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1));

        } else {
          game.getCurrentPlayer().setDrawAgain(false);
          System.out.println("Server: Player can draw again!");
        }
      } else {
        game.setCurrentPlayer(
            game.getActivePlayers()
                .get(game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1));
      }
    } else if (!game.getActivePlayers().isEmpty()) {
      game.setCurrentPlayer(
          game.getClientPlayersMap().get(game.getActivePlayers().get(0).getServerClient()));
    }
    game.getGameState().setCurrentPlayer(game.getCurrentPlayer());

    // selection of the new next player is done
    if (allHolesFilled()) {
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println(
            "Server: All holes are filled going to next Floor or check the winning!");
      nextFloor();
      return;
    }
    if (allPlayerMolesInHoles()) {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println(
            "all player moles are in holes! for player: " + game.getCurrentPlayer().getName());
      }

      MoleGames.getMoleGames()
          .getServer()
          .sendToAllGameClients(
              game,
              MoleGames.getMoleGames()
                  .getServer()
                  .getPacketHandler()
                  .playerSkippedPacket(game.getCurrentPlayer()));
      if (game.getActivePlayers().size() == 1) {
        nextFloor();
      } else {
        nextPlayer();
      }
    } else {
      if (game.getCurrentPlayer().getMoles().size() < game.getSettings().getNumberOfMoles()
          && game.getGameState().getCurrentFloorID() == 0) {

        MoleGames.getMoleGames()
            .getServer()
            .sendToAllGameClients(
                game,
                MoleGames.getMoleGames()
                    .getServer()
                    .getPacketHandler()
                    .playerPlacesMolePacket(game.getCurrentPlayer()));
      } else {
        var maySkip = allPlayerMolesInHoles();
        MoleGames.getMoleGames()
            .getServer()
            .sendToAllGameClients(
                game,
                MoleGames.getMoleGames()
                    .getServer()
                    .getPacketHandler()
                    .playersTurnPacket(
                        (ServerThread) game.getCurrentPlayer().getServerClient(), maySkip));
      }
      game.getCurrentPlayer().getPlayerUtil().startThinkTimer();
    }
  }

  /**
   * @author Carina
   * @use goes to the next Floor it it exists bekommt
   */
  public void nextFloor() {
    if (game.getSettings().getFloors().size() > game.getGameState().getCurrentFloorID() + 1) {

      var eliminated = new ArrayList<>(game.getActivePlayers());
      for (var player : game.getActivePlayers()) {
        for (var mole : player.getMoles()) {
          if (game.getMap()
              .getFieldMap()
              .get(List.of(mole.getField().getX(), mole.getField().getY()))
              .isHole()) {
            eliminated.remove(player);
            if (MoleGames.getMoleGames().getServer().isDebug()) {
              System.out.println(
                  "Server: player with id "
                      + player.getServerClient().getThreadID()
                      + " is in next level!");
            }
            break;
          }
        }
      }

      for (var player : eliminated) {
        System.out.println("Player: " + player + " is eliminated!");
        game.removePlayerFromGame(player);
      }
      game.getEliminatedPlayers().addAll(eliminated);
      for (var player : game.getActivePlayers()) {
        for (var mole : new HashSet<>(player.getMoles())) {
          if (!game.getMap()
              .getFieldMap()
              .get(List.of(mole.getField().getX(), mole.getField().getY()))
              .isHole()) {
            player.getMoles().remove(mole);
          }
        }
      }
      game.getGameUtil()
          .givePoints(
              false); // Giving the points to the players who are in the next level or just won
      game.getGameState().setCurrentFloorID(game.getGameState().getCurrentFloorID() + 1);
      game.updateGameState();
      MoleGames.getMoleGames()
          .getServer()
          .sendToAllGameClients(
              game,
              MoleGames.getMoleGames()
                  .getServer()
                  .getPacketHandler()
                  .nextFloorPacket(game.getGameState(), eliminated));
      nextPlayer();
    } else {
      game.getGameUtil()
          .givePoints(
              true); // Giving the points to the players who are in the next level or just won
      MoleGames.getMoleGames().getGameHandler().getGameLogic().checkWinning(game);
      game.setCurrentGameState(GameStates.OVER);
    }
  }

  /**
   * @author Carina
   * @use gives points to the player who are in holes when a next floor comes
   * @sse PlayerModel
   * @see de.thundergames.filehandling.Score
   */
  public void givePoints(final boolean lastFloor) {
    for (var player : game.getActivePlayers()) {
      game.getScore()
          .getPoints()
          .put(
              player.getClientID(),
              game.getScore().getPoints().get(player.getClientID())
                  + game.getMap().getPoints() * (lastFloor ? 1 : player.getMoles().size()));
    }
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      for (var player : game.getActivePlayers()) {
        System.out.println(
            "the player with the name: "
                + player.getName()
                + " got: "
                + game.getScore().getPoints().get(player.getServerClient().getThreadID())
                + " points!");
      }
    }
  }
}
