package de.pentagames.maulwurfkompanie.ui.lobby;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.Game;
import upb.maulwurfcompany.library.data.GameStatus;

public class GameListAdapter extends ArrayAdapter<Game> {

  private final Context context;
  private final List<Game> games;

  public GameListAdapter(@NotNull final Context context, @NotNull final List<Game> games, @NotNull final GameStatus gameStatus) {
    super(context, 0, games);
    this.context = context;
    this.games = games;
  }

  private String getTime(final long timestamp) {
    var cal = Calendar.getInstance();
    cal.setTimeInMillis(timestamp);
    return DateFormat.format("HH:mm", cal).toString();
  }

  @NonNull
  @Override
  public View getView(final int position, @Nullable final View convertView, @NotNull final ViewGroup parent) {
    var listItem = convertView;
    if (listItem == null)
      listItem = LayoutInflater.from(context).inflate(R.layout.lobby_game_item, parent, false);
    var game = games.get(position);
    //TextView textView = listItem.findViewById(R.id.title);
    //textView.setText("Spiel: " + game.gameID + "      " + game.currentPlayerCount + "/" + game.maxPlayerCount + " Spieler" + "    " + "Ebenen: " + game.levelCount;);
    // default elements for all games
    var gameID = (TextView) listItem.findViewById(R.id.displayGame);
    gameID.setText("Spiel: " + game.gameID);
    var gamePlayers = (TextView) listItem.findViewById(R.id.displayPlayers);
    gamePlayers.setText("Spieler: " + game.currentPlayerCount + "/" + game.maxPlayerCount);
    var gameLevels = (TextView) listItem.findViewById(R.id.displayLevels);
    gameLevels.setText("Ebenen: " + game.levelCount);
    if (game.status == GameStatus.STARTED) {
      var gameStarttime = (TextView) listItem.findViewById(R.id.displayTime);
      gameStarttime.setText("Start: " + getTime(game.startDateTime));
    } else if (game.status == GameStatus.OVER) {
      var gameEndtime = (TextView) listItem.findViewById(R.id.displayTime);
      gameEndtime.setText("Ende: " + getTime(game.finishDateTime));
      var gameWinners = (TextView) listItem.findViewById(R.id.displayWinner);
      if(game.result.winner.length > 0){
        gameWinners.setText("Gewonnen: " + game.result.winner[0].name.substring(0, Math.min(12, game.result.winner[0].name.length())) + (game.result.winner.length > 1 ? " + " + (game.result.winner.length - 1) : " 👑"));
      }
    } else {
      var gameEndtime = (TextView) listItem.findViewById(R.id.displayTime);
      gameEndtime.setText("");
    }
    // join game
    listItem.setOnClickListener(v -> Client.messageHandler.joinGame(game, Client.participant));
    return listItem;
  }
}
