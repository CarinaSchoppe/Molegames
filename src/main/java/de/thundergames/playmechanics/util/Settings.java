/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 17:42 by Carina Latest changes made by Carina on 15.12.21, 17:41 All contents of "Settings" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import com.google.gson.annotations.SerializedName;
import de.thundergames.filehandling.GameConfiguration;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.map.Map;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Settings {

  /*
  TODO: ausrichter kann spieler zu einem spiel zuweisen
  TODO: hier mehr adden! und dann auch implementieren
   */
  private final ArrayList<Integer> pullDiscs = new ArrayList<>(List.of(1, 2, 3, 4));
  private final int deductedPoints = 5;
  private final transient GameConfiguration gameConfiguration;
  private final transient Game game;
  private int maxPlayers = 4;
  private int radius = 10;
  private int numberOfMoles = 4;
  @SerializedName(value = "levels")
  private ArrayList<Map> floors = new ArrayList<>();
  private boolean pullDiscsOrdered = true;
  private long turnTime = 5000;
  private int visualizationTime = 10;
  private String movePenalty = "NOTHING";

  public Settings(@NotNull final Game game) {
    this.game = game;
    this.gameConfiguration = new GameConfiguration();
  }

  /**
   * @param newConfig the jsonObject that will update the configuration send by the AusrichterClient
   * @author Carina
   * @use pass in the new configuration from the AusrichterClient and it will automaticly update
   * every single setting that was included in the jsonObject
   * @use this method is called in the AusrichterClient to the Server
   * @use updates the map and the Game directly
   */
  public void updateConfiuration(@NotNull final Settings newConfig) {
    setMaxPlayers(newConfig.getMaxPlayers());
    setRadius(newConfig.getRadius());
    setNumberOfMoles(newConfig.getNumberOfMoles());
    setFloors(newConfig.getFloors());
    setPullDiscsOrdered(newConfig.isPullDiscsOrdered());
    getPullDiscs().clear();
    getPullDiscs().addAll(newConfig.getPullDiscs());
    setTurnTime(newConfig.getTurnTime());
    setVisualizationTime(newConfig.getVisualizationTime());
    setMovePenalty(newConfig.getMovePenalty());
    game.updateGameState();
  }

  public Punishments getPunishment() {
    return Punishments.getByName(getMovePenalty());
  }
}
