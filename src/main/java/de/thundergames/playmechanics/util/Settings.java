/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 11.01.22, 20:27 by Carina Latest changes made by Carina on 11.01.22, 20:27 All contents of "Settings" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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

  private final ArrayList<Integer> pullDiscs = new ArrayList<>(List.of(1, 2, 3, 4));
  private final int deductedPoints = 15;
  private final transient GameConfiguration gameConfiguration;
  private final transient Game game;
  private int maxPlayers = 4;
  private int radius = 4;
  private int numberOfMoles = 4;

  @SerializedName(value = "levels")
  private ArrayList<Map> floors = new ArrayList<>();

  private boolean pullDiscsOrdered = true;
  private long turnTime = 15000;
  private int visualizationTime = 10000;
  private String movePenalty = "NOTHING";

  public Settings(@NotNull final Game game) {
    this.game = game;
    this.gameConfiguration = new GameConfiguration();
  }

  public Punishments getPunishment() {
    return Punishments.getByName(getMovePenalty());
  }
}
