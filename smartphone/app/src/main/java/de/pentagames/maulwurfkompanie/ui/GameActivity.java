package de.pentagames.maulwurfkompanie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.board.GameView;
import de.pentagames.maulwurfkompanie.client.Client;
import de.pentagames.maulwurfkompanie.ui.lobby.GameWaitLobby;
import upb.maulwurfcompany.library.data.GameStatus;
import upb.maulwurfcompany.library.messages.LeaveGame;
import upb.maulwurfcompany.library.messages.RegisterOverviewObserver;

public class GameActivity extends AppCompatActivity {

  public static GameActivity instance;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
    var gameState = Client.messageHandler.currentGameState;
    if (gameState.status == GameStatus.NOT_STARTED){
      var waitlobby = new Intent(this, GameWaitLobby.class);
      startActivity(waitlobby);
    }

      setContentView(R.layout.activity_game);
      var fab = (FloatingActionButton) findViewById(R.id.gameRulesfab);
      fab.setOnClickListener(view -> {
        var intentConfig = new Intent(this, ShowConfiguration.class);
        startActivity(intentConfig);
      });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  /**
   * Called when the activity has detected the user's press of the back
   * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
   * chance to handle the back button before the default behavior of
   * {@link Activity#onBackPressed()} is invoked.
   *
   * @see #getOnBackPressedDispatcher()
   */
  @Override
  public void onBackPressed() {
    var leaveGameDialog = new LeaveGameDialog();
    leaveGameDialog.show(getSupportFragmentManager(), "Leave Game Dialog");
  }

  public void leaveGame() {
    super.onBackPressed();
    instance = null;
    Client.messageHandler.currentGameState = null;
    Client.messageHandler.game = null;
    GameView.instance = null;
    Client.sendMessages(new LeaveGame(),new RegisterOverviewObserver());
  }
}
