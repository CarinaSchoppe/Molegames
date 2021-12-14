/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "PlayerResult" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.score;

import org.jetbrains.annotations.NotNull;

public class PlayerResult {
  private int score;
  private String name;
  private int placement;

  /**
   * @param name
   * @param score
   * @param placement
   * @author Lennart
   * @use the result for a player
   * @see de.thundergames.filehandling.Score
   */
  public PlayerResult(@NotNull final String name, final int score, final int placement) {
    this.name = name;
    this.score = score;
    this.placement = placement;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPlacement() {
    return placement;
  }

  public void setPlacement(int placement) {
    this.placement = placement;
  }
}
