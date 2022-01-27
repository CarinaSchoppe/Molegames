package de.pentagames.maulwurfkompanie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.pentagames.maulwurfkompanie.R;

/**
 * ShowLoginRules generates a window which displays the Loginrules. Said window can be left via arrow button.
 *
 * @author Lennart
 */

public class ShowLoginRules extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rules);
        var mMessageWindow = (TextView) findViewById(R.id.messageWindow);
        // returns to login window if arrow button is pressed
        var rulesbackButton = (Button) findViewById(R.id.rulesbackbutton);
        rulesbackButton.setOnClickListener(view -> {
            super.onBackPressed();
        });
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        String someLoginMessage = "<h2>Name:<h2/>" +
                " - geben Sie einen beliebigen Namen ein, mit dem Sie für andere Spieler sichtbar sein möchten<br/>" +
                " - der Name darf maximal 32 Zeichen lang sein<br/>" +
                " - es sind nur Buchstaben und Zahlen erlaubt<br/>" +
                " - es können auch Umlaute verwendet werden<br/>" +
                " - Sonderzeichen (z.B. / , &  oder $) und Leerzeichen sind nicht erlaubt<br/>" +
                "<b>Als Spieler ist dieses Feld ein Pflichtfeld. Als Beobachter kann das Feld freigelassen werden.</b><br/>" +
                "<br/><br/>" +
                "<h2>IP-Adresse:<h2/>" +
                " - geben Sie hier eine gültige IP-Adresse des gewünschten Spielservers an<br/>" +
                " - Die IP-Adresse kann sowohl eine Web-Adresse als auch eine Adresse aus dem  lokalen Netzwerk sein <br/>" +
                "<b>Die Angabe einer IP-Adresse ist bei jedem Login erforderlich</b><br/>" +
                "<br/><br/>" +
                "<h2>Port:<h2/>" +
                " - geben Sie hier den passenden Port der Servers mit der zuvor eingegebenen IP-Adresse des gewünschten Spielservers an<br/>" +
                " - Der Server-Port darf nur aus Zahlen bestehen <br/>" +
                " - der Server-Port darf maximal 7 Zeichen lang sein<br/>" +
                "<b>Die Angabe des Server-Ports ist bei jedem Login erforderlich</b>";
        mMessageWindow.setText(Html.fromHtml(someLoginMessage));
    }
}
