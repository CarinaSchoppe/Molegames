/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "Score" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.filehandling;

import de.thundergames.playmechanics.util.Player;
import java.util.List;
import org.json.JSONObject;

public class Score {

  private List<Player> players;
  private String points;
  private List<Player> winner;

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public void setWinner(List<Player> winner) {
    this.winner = winner;
  }

  public String getPoints() {
    return points;
  }

  public void setPoints(String points) {
    this.points = points;
  }


  public String toJsonObject() {
    var object = new JSONObject();
    var playerArray = new String[players.size()];
    var winnerArray = new String[winner.size()];
    var points = new JSONObject();
    for (int i = 0; i < players.size(); i++) {
      playerArray[i] = players.get(i).toJsonObject();
      points.put(String.valueOf(players.get(i).getServerClient().getConnectionID()), players.get(i).getPoints());
    }
    object.put("players", playerArray);
    for (int i = 0; i < winner.size(); i++) {
      winnerArray[i] = winner.get(i).toJsonObject();
    }
    object.put("winner", winnerArray);
    object.put("points", points.toString());

    return object.toString();
  }
}
