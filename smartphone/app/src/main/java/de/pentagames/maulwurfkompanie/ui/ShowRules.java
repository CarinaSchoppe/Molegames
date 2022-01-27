package de.pentagames.maulwurfkompanie.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.pentagames.maulwurfkompanie.R;

/**
 * ShowRules generates a window which displays the rules. Said window can be left via arrow button.
 *
 * @author Lennart
 */

public class ShowRules extends AppCompatActivity {

  @Override
  protected void onCreate(@NonNull final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_rules);
    var mMessageWindow = (TextView) findViewById(R.id.messageWindow);
    var mTextView = (TextView) findViewById(R.id.textView2);
    mTextView.setText("Spielregeln");
    // returns to lobby window if arrow button is pressed
    var rulesbackButton = (Button) findViewById(R.id.rulesbackbutton);
    rulesbackButton.setOnClickListener(view -> {
      super.onBackPressed();
    });

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    var someMessage = "<h2>Regeln:<h2/>" +
            " - das Spiel kann mit zwei oder mehr Spielern gespielt werden<br/>" +
            " - jeder Spieler erhält beim Spielstart eine eigene Maulwurffarbe<br/>" +
            "<br/>" +
            "<b>Spielbeginn:</b><br/>" +
            " - jeder Spieler platziert seine Maulwürfe auf der obersten Ebene<br/>" +
            " - Maulwürfe dürfen nicht auf ein bereits belegtes Feld gestellt werden<br/>" +
            " - Maulwürfe dürfen nicht in ein Loch gestellt werden<br/>" +
            " - Spielerreihnfolge wird festgelegt und nicht verändert<br/>" +
            "<br/>" +
            "<b>Spielablauf:</b><br/>" +
            " - erster Spieler zieht eine Zugkarte und darf nur genau die angegebenen Schritte gehen<br/>" +
            " - pro Runde zieht jeder Spieler nur eine Karte<br/>" +
            " - sobald die Karten aufgebraucht sind, werden sie neu gemischt<br/>" +
            " - sobald alle Löcher in einer Ebene besetzt sind, wird die aktuelle Ebene mit allen Maulwürfen, die sich nicht in Löchern befinden entfernt<br/>" +
            " - hat eine Spieler keine Maulwürfe mehr, spielt er nicht mehr mit<br/>" +
            "<br/>" +
            "<b>Bewegungsmöglichkeiten:</b><br/>" +
            " - der Maulwurf darf nur gerade und in eine Richtung gezogen werden<br/>" +
            " - es dürfen keine anderen Maulwürfe übersprungen werden, egal ob sie in einem Loch sind oder nicht<br/>" +
            " - es darf nur die Anzahl Schritte gegangen werden, die auf der Zugkarte angegeben sind<br/>" +
            " - ein Spielzug enfällt, falls kein eigener Maulwurf (inklusive der Maulwürfe in Löchern) die komplette Anzahl der Schritte gehen kann<br/>" +
            " - wenn alle Maulwürfe eines Spielers in Löchern sitzen, setzt der Spieler aus<br/>" +
            " - erreicht der bewegte Maulwurf am Ende des Zuges ein “Ziehe erneut”-Feld, darf der Spieler einen weiteren Spielzug durchführen<br/>" +
            "<br/>" +
            "<b>Punkte:</b><br/>" +
            " - für jeden eigenen Maulwurf, der eine nächste Ebene erreicht, erhält man Punkte<br/>" +
            " - für das Beenden des Spiels (Erreichen des letzten Lochs) erhält der Spieler zusätzliche Siegpunkte<br/>" +
            "<br/>" +
            "<b>Ziel des Spiels:</b><br/>" +
            " - so viele eigene Maulwürfe wie möglich in Löchern platzieren um in die nächste Ebene zu gelangen<br/>" +
            " - so viele Punkte wie möglich erzielen<br/>" +
            " - der Spieler mit den meisten gesammelten Punkten gewinnt<br/>" +
            "<br/>" +
            "<b>Ende:</b><br/>" +
            " - das Spiel endet, sobald die letzte Ebene erreicht wurde<br/>" +
            "<br/>" +
            "<br/>";
    mMessageWindow.setText(Html.fromHtml(someMessage));
  }
}
