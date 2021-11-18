package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.game.Game;
import java.util.List;
import org.json.JSONObject;

public class TournamentState {

  private List<Player> players;
  private Score score;
  private List<Game> games;

  public String toJsonObject() {
    var object = new JSONObject();
    object.put("score", score);
    var playersArray = new String[players.size()];
    var gamesArray = new String[games.size()];
    for (int i = 0; i < players.size(); i++) {
      playersArray[i] = players.get(i).toJsonObject();
    }
    for (int i = 0; i < games.size(); i++) {
      gamesArray[i] = games.get(i).toJsonObject();
    }
    object.put("players", playersArray);
    object.put("games", gamesArray);
    return object.toString();
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }
}
