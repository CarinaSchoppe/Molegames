/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 13:45 by Carina latest changes made by Carina on 23.11.21, 13:45 All contents of "GameUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.playmechanics.util.Player;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

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
  public boolean allHolesFilled() {
    for (var hole : game.getMap().getHoles()) {
      boolean inHole = false;
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
  public boolean allPlayerMolesInHoles() {
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
  public void nextPlayer() {
    if (game.isGamePaused()) {
      return;
    }
    if (game.getActivePlayers().size() - 1 >= game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1) {
      game.setCurrentPlayer(game.getActivePlayers().get(game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1));
    } else if (!game.getActivePlayers().isEmpty()) {
      game.setCurrentPlayer(game.getClientPlayersMap().get(game.getActivePlayers().get(0)));
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
      return;
    } else {
      if (game.getCurrentPlayer().getMoles().size() < game.getSettings().getNumberOfMoles()) {
        MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerPlacesMolePacket(game.getCurrentPlayer().getServerClient()));
      } else if (game.getCurrentPlayer().getMoles().size() >= game.getSettings().getNumberOfMoles()) {
        MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().playersTurnPacket(game.getCurrentPlayer().getServerClient(), game.getCurrentPlayer()));
      }
      game.getCurrentPlayer().getPlayerUtil().startThinkTimer();
    }
  }

  /**
   * @author Carina
   * @use goes to the next Floor it it exists
   */
  public void nextFloor() {
    if (game.getSettings().getLevels().size() > game.getCurrentFloorID() + 1) {
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
      game.getActivePlayers().removeAll(eliminated);
      game.getEliminatedPlayers().addAll(eliminated);
      game.setCurrentFloorID(game.getCurrentFloorID() + 1);
      game.updateGameState();
      MoleGames.getMoleGames().getServer().sendToAllGameClients(game, MoleGames.getMoleGames().getPacketHandler().nextLevelPacket(game.getGameState(), eliminated));
      nextPlayer();
    } else {
      //TODO: check winning or do winning.
      System.out.println("PAAAARTTTTTTTTTTTTTTTTTTTTTTYYYYYYYYYYYYYYYYYYYYYYY");
    }
  }

  public void givePoints(@NotNull final NetworkField field, @NotNull final Player player) {
    game.getScore().getPoints().put(player.getClientID(), game.getScore().getPoints().get(player.getClientID()) + game.getMap().getPoints());
  }
}
