/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:18 by Carina latest changes made by Carina on 06.12.21, 22:10
 * All contents of "GameUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class GameUtil {

  private final Game game;

  public GameUtil(@NotNull final Game game) {
    this.game = game;
  }

  /**
   * @return
   * @author Carina
   * @use checks if all holes are filled with moles
   */
  public synchronized boolean allHolesFilled() {
    for (var hole : game.getMap().getHoles()) {
      var inHole = false;
      for (var player : game.getPlayers()) {
        for (var mole : player.getMoles()) {
          if (hole.getX() == mole.getNetworkField().getX() && hole.getY() == mole.getNetworkField().getY()) {
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
  public synchronized boolean allPlayerMolesInHoles() {
    var moleInHoles = 0;
    for (var moles : game.getCurrentPlayer().getMoles()) {
      for (var hole : game.getMap().getHoles()) {
        if (hole.getX() == moles.getNetworkField().getX() && hole.getY() == moles.getNetworkField().getY()) {
          moleInHoles++;
        }
      }
    }
    return moleInHoles == game.getCurrentPlayer().getMoles().size() && game.getCurrentPlayer().getMoles().size() == game.getSettings().getNumberOfMoles();
  }

  /**
   * @author Carina
   * @use sets the next player in the game if all moles are in holes the player is not on turn
   */
  public synchronized void nextPlayer() {
    if (game.getActivePlayers().isEmpty()) game.forceGameEnd();
    if (game.getCurrentGameState() == GameStates.OVER || game.getCurrentGameState() == GameStates.PAUSED) {
      return;
    }
    if (game.getActivePlayers().size() - 1 >= game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1) {
      game.setCurrentPlayer(game.getActivePlayers().get(game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1));
    } else if (!game.getActivePlayers().isEmpty()) {
      game.setCurrentPlayer(game.getClientPlayersMap().get(game.getActivePlayers().get(0).getServerClient()));
    }
    if (allHolesFilled()) {
      System.out.println("Server: All holes are filled going to next Floor or check the winning!");
      nextFloor();
      return;
    }
    if (allPlayerMolesInHoles()) {
      System.out.println("all player moles are in holes! for playerID: " + game.getCurrentPlayer().getServerClient().getConnectionID());
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerSkippedPacket(game.getCurrentPlayer()));
      nextPlayer();
    } else {
      if (game.getCurrentPlayer().getMoles().size() < game.getSettings().getNumberOfMoles() && game.getCurrentFloorID() == 0) {
        MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerPlacesMolePacket(game.getCurrentPlayer().getServerClient()));
      } else {
        var maySkip = true;
        if (!game.getCurrentPlayer().getMoles().isEmpty()) {
          for (var moles : game.getCurrentPlayer().getMoles()) {
            var inHole = false;
            for (var hole : game.getMap().getHoles()) {
              if (moles.getNetworkField().getX() == hole.getX() && moles.getNetworkField().getY() == hole.getY()) {
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
        MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().playersTurnPacket(game.getCurrentPlayer().getServerClient(), game.getCurrentPlayer(), maySkip));
      }
      game.getCurrentPlayer().getPlayerUtil().startThinkTimer();
    }
  }

  /**
   * @author Carina
   * @use goes to the next Floor it it exists TODO: sagen dass man raus ist aber noch updates
   * bekommt
   */
  public synchronized void nextFloor() {
    if (game.getSettings().getFloors().size() > game.getCurrentFloorID() + 1) {
      var eliminated = new ArrayList<>(game.getPlayers());
      for (var hole : game.getGameState().getFloor().getHoles()) {
        for (var player : game.getPlayers()) {
          for (var mole : player.getMoles()) {
            if (eliminated.contains(player)) {
              if (mole.getNetworkField().getX() == hole.getX() && mole.getNetworkField().getY() == hole.getY()) {
                eliminated.remove(player);
                System.out.println("Server: player with id " + player.getServerClient().getConnectionID() + " is in next level!");
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
            if (mole.getNetworkField().getX() == hole.getX() && mole.getNetworkField().getY() == hole.getY()) {
              player.getMoles().add(mole);
            }
          }
        }
      }
      game.getGameUtil().givePoints(); //Giving the points to the players who are in the next level or just won
      game.setCurrentFloorID(game.getCurrentFloorID() + 1);
      game.updateGameState();
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().nextLevelPacket(game.getGameState(), eliminated));
      nextPlayer();
    } else {
      game.getGameUtil().givePoints(); //Giving the points to the players who are in the next level or just won
      // TODO: check winning or do winning.
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
  public synchronized void givePoints() {
    for (var holes : game.getMap().getHoles()) {
      for (var player : game.getActivePlayers()) {
        for (var mole : player.getMoles()) {
          if (mole.getNetworkField().getX() == holes.getX() && mole.getNetworkField().getY() == holes.getY()) {
            game.getScore().getPoints().put(player.getClientID(), game.getScore().getPoints().get(player.getClientID()) + game.getMap().getPoints());
          }
        }
      }
    }
    for (var player : game.getActivePlayers()) {
      System.out.println("the player with the name: " + player.getName() + " got: " + game.getScore().getPoints().get(player.getClientID()) + " points!");
    }
  }
}
