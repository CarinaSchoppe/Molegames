package de.thundergames.playmechanics.util;

import de.thundergames.playmechanics.game.Game;
import java.util.List;

public class Overview {

  private List<Game> games;
  private List<Tournament> tournaments;

  public Overview(List<Game> games, List<Tournament> tournaments) {
    this.games = games;
    this.tournaments = tournaments;
  }

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }

  public List<Tournament> getTournaments() {
    return tournaments;
  }

  public void setTournaments(List<Tournament> tournaments) {
    this.tournaments = tournaments;
  }
}
