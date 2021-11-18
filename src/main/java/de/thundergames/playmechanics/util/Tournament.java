package de.thundergames.playmechanics.util;

import de.thundergames.playmechanics.game.Game;
import java.util.List;
import org.json.JSONObject;

public class Tournament {

  private final int tournamentID;
  private int playerCount;
  private List<Game> games;

  public Tournament(int tournamentID) {
    this.tournamentID = tournamentID;
  }

  public String toJsonObject() {
    var jsonObject = new JSONObject();
    var gameArray = new String[games.size()];
    for (int i = 0; i < games.size(); i++) {
      gameArray[i] = games.get(i).toJsonObject();
    }
    jsonObject.put("tournamentID", tournamentID);
    jsonObject.put("playerCount", playerCount);
    jsonObject.put("games", gameArray);

    return jsonObject.toString();
  }

  public int getTournamentID() {
    return tournamentID;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }
}
