/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 16:25 by Carina Latest changes made by Carina on 15.12.21, 15:51 All contents of "Score" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.filehandling;

import com.google.gson.annotations.SerializedName;
import de.thundergames.playmechanics.util.Player;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Score {

  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<Integer, Integer> points = new HashMap<>();
  @SerializedName(value = "winner")
  private final ArrayList<Player> winners = new ArrayList<>();
}
