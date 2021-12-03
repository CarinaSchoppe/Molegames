/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 03.12.21, 13:30 by Carina latest changes made by Carina on 03.12.21, 13:12
 * All contents of "Settings" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.GameConfiguration;
import de.thundergames.networking.util.interfaceItems.NetworkConfiguration;
import de.thundergames.playmechanics.game.Game;
import org.jetbrains.annotations.NotNull;

public class Settings extends NetworkConfiguration {

  /*

  ausrichter kann spieler zu einem spiel zuweisen
  TODO: hier mehr adden! und dann auch implementieren
   */

  private final transient GameConfiguration gameConfiguration;
  private final transient Game game;

  public Settings(@NotNull final Game game) {
    this.game = game;
    this.gameConfiguration = new GameConfiguration();
  }

  /**
   * @param config the jsonObject that will update the configuration send by the GameMasterClient
   * @author Carina
   * @use pass in the new configuration from the GameMasterClient and it will automaticly update
   *     every single setting that was included in the jsonObject
   * @use this method is called in the GameMasterClient to the Server
   * @use updates the map and the Game directly
   */
  public void updateConfiuration(@NotNull final NetworkConfiguration config) {
    setMaxPlayers(config.getMaxPlayers());
    setRadius(config.getRadius());
    setNumberOfMoles(config.getNumberOfMoles());
    setFloors(config.getFloors());
    setPullDiscsOrdered(config.isPullDiscsOrdered());
    getPullDiscs().clear();
    getPullDiscs().addAll(config.getPullDiscs());
    setTurnTime(config.getTurnTime());
    setVisualizationTime(config.getVisualizationTime());
    setMovePenalty(config.getMovePenalty());
    game.updateGameState();
  }

  public Punishments getPunishment() {
    return Punishments.getByName(getMovePenalty());
  }

  public GameConfiguration getGameConfiguration() {
    return gameConfiguration;
  }

  public Integer getPunishmentPoints() {
    int punishmentPoints = 5;
    return punishmentPoints;
  }
}
