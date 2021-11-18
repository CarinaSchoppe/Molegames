package de.thundergames.playmechanics.game;

import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import java.util.List;
import org.json.JSONObject;

public class GameState {

  private List<Player> players;
  private Player currentPlayer;
  private long remainingTime;
  private List<Mole> placedMoles;
  private int moles;
  private int radius;
  private List<Field> holes;
  private List<Field> drawAgainFields;
  private boolean pullDiscsOrdered;
  private JSONObject pullDiscs;
  private long visualizationTime;
  private GameStates status;
  private Score score;

  public String toJsonObject() {
    var object = new JSONObject();
    var playerArray = new String[players.size()];
    var molesArray = new String[placedMoles.size()];
    var holesArray = new String[holes.size()];
    var drawAgainFieldsArray = new String[drawAgainFields.size()];

    for (int i = 0; i < players.size(); i++) {
      playerArray[i] = players.get(i).toJsonObject();
    }
    for (int i = 0; i < placedMoles.size(); i++) {
      molesArray[i] = placedMoles.get(i).toJsonObject();
    }
    for (int i = 0; i < holes.size(); i++) {
      holesArray[i] = holes.get(i).toJsonPosition();
    }
    for (int i = 0; i < drawAgainFields.size(); i++) {
      drawAgainFieldsArray[i] = drawAgainFields.get(i).toJsonPosition();
    }
    for (var player : players) {
      pullDiscs.put(String.valueOf(player.getServerClient().getConnectionID()), player.getCards().toArray());
    }
    object.put("players", playerArray);
    object.put("currentPlayer", currentPlayer.toJsonObject());
    object.put("remainingTime", remainingTime);
    object.put("placedMoles", molesArray);
    object.put("moles", moles);
    object.put("radius", radius);
    object.put("holes", holesArray);
    object.put("drawAgainFields", drawAgainFieldsArray);
    object.put("pullDiscsOrdered", pullDiscsOrdered);
    object.put("pullDiscs", pullDiscs.toString());
    object.put("visualizationTime", visualizationTime);
    object.put("status", status.toString());
    object.put("score", score.toJsonObject());

    return object.toString();
  }


  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public long getRemainingTime() {
    return remainingTime;
  }

  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
  }



  public int getMoles() {
    return moles;
  }

  public void setMoles(int moles) {
    this.moles = moles;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }



  public boolean isPullDiscsOrdered() {
    return pullDiscsOrdered;
  }

  public void setPullDiscsOrdered(boolean pullDiscsOrdered) {
    this.pullDiscsOrdered = pullDiscsOrdered;
  }


  public long getVisualizationTime() {
    return visualizationTime;
  }

  public void setVisualizationTime(long visualizationTime) {
    this.visualizationTime = visualizationTime;
  }

  public GameStates getStatus() {
    return status;
  }

  public void setStatus(GameStates status) {
    this.status = status;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }
}
