package de.pentagames.maulwurfkompanie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.regex.Pattern;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.client.Client;

/**
 * LoginActivity is used to provide the window in which the app can ask for the users login data including their name,
 * the chosen servers address and port.
 */
public class LoginActivity extends AppCompatActivity {

  private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
  private static final String NAME_PATTERN = "^([a-züäöß]|[A-ZÜAÖ]|[0-9]){1,32}";
  private static final String PORT_PATTERN = "^([0-9]){1,7}";
  private static final Pattern pattern_ip = Pattern.compile(IPV4_PATTERN);
  private static final Pattern pattern_name = Pattern.compile(NAME_PATTERN);
  private static final Pattern pattern_port = Pattern.compile(PORT_PATTERN);
  /**
   * A statically accessible instance of the class.
   */
  private static LoginActivity instance;

  /**
   * This method checks whether the entered IP address is valid.
   *
   * @param input The IP address entered by the User.
   * @return true if address is valid.
   */
  public static boolean isValid_IP(@NonNull final String input) {
    var matcher_ip = pattern_ip.matcher(input);
    return matcher_ip.matches();
  }

  /**
   * This method checks whether the entered name is valid.
   *
   * @param input The name entered by the User.
   * @return true if the name is valid.
   */
  public static boolean isValid_Name(@NonNull final String input) {
    var matcher_name = pattern_name.matcher(input);
    return matcher_name.matches();
  }

  /**
   * This method checks whether the entered port is valid.
   *
   * @param input The port entered by the User.
   * @return true if the port is valid.
   */
  public static boolean isValid_Port(@NonNull final String input) {
    var matcher_port = pattern_port.matcher(input);
    return matcher_port.matches();
  }

  /**
   * Enables static access to the instance of {@link LoginActivity} and its non-static properties.
   *
   * @return The {@link LoginActivity} object stored in 'instance'.
   */
  public static LoginActivity getInstance() {
    return instance;
  }

  @Override
  protected void onCreate(@NonNull final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
    setContentView(R.layout.activity_login);
    var loginButton = (Button) findViewById(R.id.beitretenButton);
    // shows the login rules in a new window if question mark button is pressed
    var loginrulesButton = (Button) findViewById(R.id.loginRulesButton);
    loginrulesButton.setOnClickListener(view -> {
      var loginRulesIntent = new Intent(this, ShowLoginRules.class);
      startActivity(loginRulesIntent);
    });
    // submit on enter if in the last edittext
    var portEditText = (EditText) findViewById(R.id.port);
    portEditText.setOnEditorActionListener((v, actionId, event) -> {
      if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
        loginButton.callOnClick();
      }
      return false;
    });
    loginButton.setOnClickListener(v -> {
      var playersName = ((EditText) findViewById(R.id.playerName)).getText().toString();
      var ip = ((EditText) findViewById(R.id.ip)).getText().toString();
      var port = ((EditText) findViewById(R.id.port)).getText().toString();
      if (playersName.isEmpty()) {
        Client.participant = false;
      }
      if (!ip.isEmpty() && !port.isEmpty()) {          // !playersName.isEmpty() && taken from conditions
        if (playersName.length() > 32) {
          Toast.makeText(getApplicationContext(), "Bitte kürzeren Namen wählen!", Toast.LENGTH_SHORT).show();
        } else if (!playersName.isEmpty() && !isValid_Name(playersName)) {
          Toast.makeText(getApplicationContext(), "Ungültiges Zeichen im Namen!", Toast.LENGTH_SHORT).show();
        } else if (!isValid_Port(port)) {
          Toast.makeText(getApplicationContext(), "Bitte Port überprüfen!!", Toast.LENGTH_SHORT).show();
        } else {
          Client.connectToServer(ip, Integer.parseInt(port), playersName);
//                    int intPort = Integer.parseInt(port);
//                    Intent lobbyIntent = new Intent(this, LobbyActivity.class);
//                    Bundle data = new Bundle();
//                    data.putString("ip", ip);
//                    data.putInt("port", Integer.parseInt(port));
//                    data.putString("playersName", playersName);
//                    lobbyIntent.putExtras(data);
        }
      } else {
        Toast.makeText(getApplicationContext(), "Bitte Eingaben vollständig eingeben!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
