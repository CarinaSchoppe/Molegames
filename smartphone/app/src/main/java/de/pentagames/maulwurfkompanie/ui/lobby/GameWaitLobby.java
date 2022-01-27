package de.pentagames.maulwurfkompanie.ui.lobby;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;
import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.GameStatus;

public class GameWaitLobby extends AppCompatActivity {

    private TextView successful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_wait_lobby);
        successful = findViewById(R.id.JoinedSuccessfully);

        Timer wait = new Timer();
        wait.schedule(new TimerTask() {
            @Override
            public void run() {
                successful.setAlpha(0);
            }
        }, 5000);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var gameState = Client.messageHandler.currentGameState;
                if (gameState != null && gameState.status != GameStatus.NOT_STARTED ) {
                    finish(); // close this activity and return to preview activity (if there is any);
                }
            }
        }, 1,100);
    }
}