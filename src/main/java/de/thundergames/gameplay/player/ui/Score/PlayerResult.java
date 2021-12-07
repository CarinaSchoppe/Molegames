package de.thundergames.gameplay.player.ui.Score;

public class PlayerResult {
  private int score;
  private String name;
  private int placement;

  public PlayerResult(String name,int score, int placement)
  {
    this.name =name;
    this.score = score;
    this.placement = placement;

  }

  public int getScore(){ return score ; }

  public String getName(){ return name; }

  public int getPlacement(){ return placement;}

  public void setScore(int score) { this.score = score; }

  public void setName(String name) { this.name = name; }

  public void setPlacement(int placement) { this.placement = placement; }

}
