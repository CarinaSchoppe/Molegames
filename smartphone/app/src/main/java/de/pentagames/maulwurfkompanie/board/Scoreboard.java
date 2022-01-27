package de.pentagames.maulwurfkompanie.board;

import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import de.pentagames.maulwurfkompanie.Utils;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.Game;
import upb.maulwurfcompany.library.data.GameState;

/**
 * Shows a scoreboard containing all players, their scores and their disks
 *
 * @author Lennart
 */
public class Scoreboard {

  private static final int playersPerPage = 5;
  final Paint boxPaint;
  final Paint colorPaint;
  final Paint textPaint;
  public int currentPage = 1;

  /**
   * the constructor of the scoreboard
   */
  public Scoreboard() {
    boxPaint = new Paint();
    boxPaint.setColor(0xcc000000);
    colorPaint = new Paint();
    colorPaint.setAntiAlias(true);
    colorPaint.setTextSize(40);
    textPaint = new Paint();
    textPaint.setColor(0xffffffff);
    textPaint.setAntiAlias(true);
  }

  /**
   * changes the page of the scoreboard
   */
  public void nextPage() {
    if (Client.messageHandler.currentGameState.activePlayers.length > 0)
      currentPage = 1 + currentPage % (int) Math.ceil(Client.messageHandler.currentGameState.activePlayers.length / (float) playersPerPage);
  }

  /**
   * Draws the state of the game at the end of the game. Shows players, points and who has won
   *
   * @param gameState The current {@link GameState}.
   * @param canvas    The canvas object to draw on.
   */
  public void drawWinningScoreboard(@NotNull final GameState gameState, @NotNull final Canvas canvas) {
    // Generate Scoreboard by sorting player list by score
    var scoreboardPlayers = new ArrayList<>(Arrays.asList(gameState.score.players));
    //        noinspection ComparatorCombinators
    Collections.sort(scoreboardPlayers, (p1, p2) -> gameState.score.points.get(p2.clientID) - gameState.score.points.get(p1.clientID));
    var winnername = scoreboardPlayers.get(0).name;
    var singlewinner = true;
    for (var i = 1; i < scoreboardPlayers.size(); i++) {
      if (Objects.equals(gameState.score.points.get(scoreboardPlayers.get(i).clientID), gameState.score.points.get(scoreboardPlayers.get(0).clientID))) {
        singlewinner = false;
        if (i+1 == scoreboardPlayers.size()) {
          winnername += " und " + scoreboardPlayers.get(i).name;
        } else {
          winnername += ", " + scoreboardPlayers.get(i).name;
        }
      } else {
        break;
      }
    }
    if (singlewinner == false){
      winnername += " haben";
    } else {
      winnername += " hat";
    }
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setTextSize(min(80, 1800 / winnername.length()));
    canvas.drawRect(50, 50, canvas.getWidth() - 50, canvas.getHeight() - 50, boxPaint);
    canvas.drawText(winnername , canvas.getWidth() / 2, 200, textPaint);
    canvas.drawText(" mit " + gameState.score.points.get(scoreboardPlayers.get(0).clientID) + " Punkten gewonnen.", canvas.getWidth() / 2, 300, textPaint);
    textPaint.setColor(0xffbbbbbb);
    textPaint.setTextSize(60);
    if (scoreboardPlayers.get(0).clientID == Client.clientId) {
      canvas.drawText("Du hast gewonnen", canvas.getWidth() / 2, 380, textPaint);
    } else if (gameState.score.points.containsKey(Client.clientId)) { // only shown if active player
      canvas.drawText("Du hast verloren", canvas.getWidth() / 2, 380, textPaint);
    }
    textPaint.setTextSize(45);
    textPaint.setColor(0xffffffff);
    for (var i = 0; i < scoreboardPlayers.size(); i++) {
      textPaint.setTextAlign(Paint.Align.LEFT);
      canvas.drawText(scoreboardPlayers.get(i).name, 150, 480 + 60 * i, textPaint);
      // Draw Line
      colorPaint.setColor(GameView.getPlayerColor(gameState, scoreboardPlayers.get(i)));
      canvas.drawRect(150, 480 + 60 * i, canvas.getWidth() - 150, 482 + 60 * i, colorPaint);
      textPaint.setTextAlign(Paint.Align.RIGHT);
      canvas.drawText(gameState.score.points.get(scoreboardPlayers.get(i).clientID) + "", canvas.getWidth() - 150, 480 + 60 * i, textPaint);
    }
  }

  /**
   * Draws the state of the game during game. Shows players, points and which disks they have at the moment
   *
   * @param game      The current {@link Game} object.
   * @param gameState The current {@link GameState}.
   * @param canvas    The canvas object to draw on.
   */
  public void drawInGameScoreboard(Game game, GameState gameState, Canvas canvas) {
    canvas.drawRect(0, 0, canvas.getWidth(), 300, boxPaint);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setTextSize(40);
    var maxNumberOnPage = min(gameState.activePlayers.length, (playersPerPage * currentPage));
    //String titleText = (playersPerPage * currentPage - playersPerPage + 1) + "-" + maxNumberOnPage + " / " + gameState.activePlayers.length;
    //if (playersPerPage >= gameState.activePlayers.length) titleText = "";
    if (canvas.getWidth() < canvas.getHeight()) {
      textPaint.setFakeBoldText(true);
      canvas.drawText("SCOREBOARD", canvas.getWidth() / 2f, 40, textPaint);
      textPaint.setFakeBoldText(false);
    } else canvas.translate(0, -40); // move everything up
    int currentPlayer = 0;
    if (gameState.currentPlayer != null) {
      for (var i = 0; i < gameState.activePlayers.length; i++) {
        if (gameState.activePlayers[i].clientID == gameState.currentPlayer.clientID) {
          currentPlayer = i;
          break;
        }
      }
      if (currentPage == 1) {
        // Underline current player
        var remaining = Client.messageHandler.until - Utils.getTime();
        if (remaining < 0) remaining = Client.messageHandler.game.turnTime;
        var x = (int) ((canvas.getWidth() - 40) * Math.min(1, remaining / (float) Client.messageHandler.game.turnTime));
        colorPaint.setColor(GameView.getPlayerColor(gameState, gameState.currentPlayer));
        canvas.drawRect(20, 48, x, 52, colorPaint);
        canvas.drawRect(20, 78, x, 82, colorPaint);
      }
    }
    for (var i = 0; i < maxNumberOnPage - playersPerPage * (currentPage - 1); i++) {
      var player = gameState.activePlayers[(currentPlayer + i + (playersPerPage * currentPage - playersPerPage)) % gameState.activePlayers.length];
      // colored rectangles
      colorPaint.setColor(GameView.getPlayerColor(gameState, player));
      canvas.drawRect(18, 48 + 40 * i, 50, 82 + 40 * i, colorPaint);
      canvas.drawRect(canvas.getWidth() - 52, 48 + 40 * i, canvas.getWidth() - 18, 82 + 40 * i, colorPaint);
      textPaint.setTextAlign(Paint.Align.LEFT);
      canvas.drawText(player.name.substring(0, Math.min(12, player.name.length())), 70, 80 + 40 * i, textPaint);
      textPaint.setTextAlign(Paint.Align.RIGHT);
      canvas.drawText(gameState.score.points.get(player.clientID) + "", canvas.getWidth() - 70, 80 + 40 * i, textPaint);
      // draw Cards available to Player
      var card = 0;
      var cardText = new StringBuilder();
      int k = 0;
      for (var j = 0; j < game.pullDiscs.length; j++) {
        if (k >= Objects.requireNonNull(Objects.requireNonNull(gameState.pullDiscs.get(player.clientID))).length) break;
        card = Objects.requireNonNull(Objects.requireNonNull(gameState.pullDiscs.get(player.clientID)))[k];
        if (card == game.pullDiscs[j]) {
          k++;
          cardText.append(card);
        } else {
          cardText.append("      ");
        }
      }
      textPaint.setTextAlign(Paint.Align.RIGHT);
      //textPaint.setTextSize(20);
      canvas.drawText(cardText.toString(), canvas.getWidth() - 200, 80 + 40 * i, textPaint);
    }
    if (playersPerPage < gameState.activePlayers.length) {
      textPaint.setColor(0xaaffffff);
      textPaint.setTextAlign(Paint.Align.LEFT);
      canvas.drawText("Seite " + currentPage + "/" + (int) Math.ceil(1f * gameState.activePlayers.length / playersPerPage), 50, 280, textPaint);
      textPaint.setTextAlign(Paint.Align.RIGHT);
      canvas.drawText("Tippe für nächste ", canvas.getWidth() - 59, 280, textPaint);
      textPaint.setColor(0xffffffff);
    }
    if (canvas.getWidth() >= canvas.getHeight()) {
      canvas.translate(0, 40); // move everything up
    }
  }
}
