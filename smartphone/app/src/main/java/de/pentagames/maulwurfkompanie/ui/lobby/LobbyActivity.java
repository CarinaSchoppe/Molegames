package de.pentagames.maulwurfkompanie.ui.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.Utils;
import de.pentagames.maulwurfkompanie.client.Client;
import de.pentagames.maulwurfkompanie.databinding.ActivityLobbyBinding;
import de.pentagames.maulwurfkompanie.ui.ShowLoginRules;
import de.pentagames.maulwurfkompanie.ui.ShowRules;
import upb.maulwurfcompany.library.messages.Overview;

public class LobbyActivity extends AppCompatActivity {

  public static LobbyActivity lobbyInstance;
  private TabsAdapter tabsAdapter;

  public static void overviewUpdated(@NotNull final Overview overview) {
    if (lobbyInstance == null || lobbyInstance.tabsAdapter == null) return;
    var mainHandler = new Handler(lobbyInstance.getMainLooper());
    mainHandler.post(() -> lobbyInstance.tabsAdapter.overviewUpdated(overview));
  }

  @Override
  protected void onCreate(@NotNull final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    var binding = ActivityLobbyBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    // returns to login window if arrow button is pressed
    var lobbybackButton = (Button) findViewById(R.id.lobbybackbutton);
    lobbybackButton.setOnClickListener(view -> {
      super.onBackPressed();
    });
    // shows the game rules in a new window if question mark button is pressed
    var lobbyrulesButton = (Button) findViewById(R.id.lobbyrulesbutton);
    lobbyrulesButton.setOnClickListener(view -> {
      var lobbyRulesIntent = new Intent(this, ShowRules.class);
      startActivity(lobbyRulesIntent);
    });
    var roleicon = (ImageView) findViewById(R.id.imageView6);
    var changeroleButton = (Button) findViewById(R.id.changerolebutton);
    changeroleButton.setOnClickListener(view -> {
      if (Client.participant == false) {
        if (Client.username.equals("")) {
          Utils.makeToast("Melde dich mit einem Namen an, um mitzuspielen!", true);
        } else {
          Client.participant = true;
          Utils.makeToast("Du bist jetzt ein Spieler", true);
          roleicon.setImageResource(R.drawable.playericon);
        }
      } else {
        Client.participant = false;
        Utils.makeToast("Du bist jetzt Beobachter", true);
        roleicon.setImageResource(R.drawable.spectatoricon);
      }
    });
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    //______________________________________________
    tabsAdapter = new TabsAdapter();
    binding.viewPager.setAdapter(tabsAdapter);
    new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> tab.setText(TabsAdapter.tabTitles[position])).attach();
    //        FloatingActionButton fab = binding.fab;
    //        fab.setOnClickListener(view -> {
    //            setContentView(new GameView(getApplicationContext()));
    //            // Jump point to Game selected Game
    //        });
    //        Bundle data = getIntent().getExtras();
    //        String ip = data.getString("ip", "localhost");
    //        int port = data.getInt("port", 54321);
    //        String playersName = data.getString("playersName", "");
    //Client.connectToServer(ip, port, playersName);
    lobbyInstance = this;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Client.disconnectFromServer();
    lobbyInstance = null;
    Client.participant = false;
  }
}
