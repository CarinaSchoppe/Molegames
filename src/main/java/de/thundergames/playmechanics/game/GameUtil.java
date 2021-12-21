/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "GameUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
      var inHole = false;
      for (var player : game.getPlayers()) {
        for (var mole : player.getMoles()) {
          if (hole.getX() == mole.getField().getX() && hole.getY() == mole.getField().getY()) {
            inHole = true;
          }
        }
      }
      if (!inHole) {
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
    var moleInHoles = 0;
    for (var moles : game.getCurrentPlayer().getMoles()) {
      for (var hole : game.getMap().getHoles()) {
        if (hole.getX() == moles.getField().getX() && hole.getY() == moles.getField().getY()) {
          moleInHoles++;
        }
      }
    }
    return moleInHoles == game.getCurrentPlayer().getMoles().size()
      && game.getCurrentPlayer().getMoles().size() == game.getSettings().getNumberOfMoles();
  }

  /**
   * @author Carina
   * @use sets the next player in the game if all moles are in holes the player is not on turn
   */
  public void nextPlayer() {
    if (game.getActivePlayers().isEmpty()) game.forceGameEnd();
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
          System.out.println("Player can draw again");
        }
      } else {
        game.setCurrentPlayer(
          game.getActivePlayers()
            .get(game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1));
        game.getGameState().setCurrentPlayer(game.getCurrentPlayer());
      }
    } else if (!game.getActivePlayers().isEmpty()) {
      game.setCurrentPlayer(
        game.getClientPlayersMap().get(game.getActivePlayers().get(0).getServerClient()));
    }
    if (allHolesFilled()) {
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println(
          "Server: All holes are filled going to next Floor or check the winning!");
      nextFloor();
      return;
    }
    if (allPlayerMolesInHoles()) {
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println(
          "all player moles are in holes! for playerID: "
            + game.getCurrentPlayer().getServerClient().getThreadID());
      MoleGames.getMoleGames()
        .getServer()
        .sendToAllGameClients(
          game,
          MoleGames.getMoleGames()
            .getServer()
            .getPacketHandler()
            .playerSkippedPacket(game.getCurrentPlayer()));
      nextPlayer();
    } else {
      if (game.getCurrentPlayer().getMoles().size() < game.getSettings().getNumberOfMoles()
        && game.getCurrentFloorID() == 0) {
        MoleGames.getMoleGames()
          .getServer()
          .sendToAllGameClients(
            game,
            MoleGames.getMoleGames()
              .getServer()
              .getPacketHandler()
              .playerPlacesMolePacket(game.getCurrentPlayer()));
      } else {
        var maySkip = true;
        if (!game.getCurrentPlayer().getMoles().isEmpty()) {
          for (var moles : game.getCurrentPlayer().getMoles()) {
            var inHole = false;
            for (var hole : game.getMap().getHoles()) {
              if (moles.getField().getX() == hole.getX()
                && moles.getField().getY() == hole.getY()) {
                inHole = true;
              }
            }
            if (inHole == false) {
              maySkip = false;
              break;
            }
          }
        } else {
          maySkip = true;
        }
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
    if (game.getSettings().getFloors().size() > game.getCurrentFloorID() + 1) {
      var eliminated = new ArrayList<>(game.getPlayers());
      for (var hole : game.getGameState().getFloor().getHoles()) {
        for (var player : game.getPlayers()) {
          for (var mole : player.getMoles()) {
            if (eliminated.contains(player)) {
              if (mole.getField().getX() == hole.getX() && mole.getField().getY() == hole.getY()) {
                eliminated.remove(player);
                if (MoleGames.getMoleGames().getServer().isDebug())
                  System.out.println(
                    "Server: player with id "
                      + player.getServerClient().getThreadID()
                      + " is in next level!");
                break;
              }
            }
          }
        }
      }
      for (var player : eliminated) {
        game.removePlayerFromGame(player);
        player.getMoles().clear();
        game.getPlayers().add(player);
      }
      game.getActivePlayers().removeAll(eliminated);
      game.getEliminatedPlayers().addAll(eliminated);
      for (var player : game.getActivePlayers()) {
        var moles = new HashSet<>(player.getMoles());
        player.getMoles().clear();
        for (var hole : game.getMap().getHoles()) {
          for (var mole : moles) {
            if (mole.getField().getX() == hole.getX() && mole.getField().getY() == hole.getY()) {
              player.getMoles().add(mole);
            }
          }
        }
      }
      game.getGameUtil()
        .givePoints(); // Giving the points to the players who are in the next level or just won
      game.setCurrentFloorID(game.getCurrentFloorID() + 1);
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
        .givePoints(); // Giving the points to the players who are in the next level or just won
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
  public void givePoints() {
    for (var holes : game.getMap().getHoles()) {
      for (var player : game.getActivePlayers()) {
        for (var mole : player.getMoles()) {
          if (mole.getField().getX() == holes.getX() && mole.getField().getY() == holes.getY()) {
            game.getScore()
              .getPoints()
              .put(
                player.getServerClient().getThreadID(),
                game.getScore().getPoints().get(player.getServerClient().getThreadID())
                  + game.getMap().getPoints());
          }
        }
      }
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
