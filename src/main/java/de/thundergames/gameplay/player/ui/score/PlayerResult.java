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
