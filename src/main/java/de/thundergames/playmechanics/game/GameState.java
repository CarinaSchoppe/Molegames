/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "GameState" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import com.google.gson.annotations.SerializedName;
import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class GameState {

  private ArrayList<Player> activePlayers = new ArrayList<>();
  private Player currentPlayer;
  private int remainingTime;
  private ArrayList<Mole> placedMoles = new ArrayList<>();
  private int moles;
  private int radius;

  @SerializedName(value = "level")
  private Map floor;

  private boolean pullDiscsOrdered;
  private HashMap<Integer, ArrayList<Integer>> pullDiscs = new HashMap<>();
  private long visualizationTime = 10000;
  private String status;
  private Score score;
}
