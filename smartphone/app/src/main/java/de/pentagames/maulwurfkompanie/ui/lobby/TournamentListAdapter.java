package de.pentagames.maulwurfkompanie.ui.lobby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.pentagames.maulwurfkompanie.R;
import upb.maulwurfcompany.library.data.Tournament;

public class TournamentListAdapter extends ArrayAdapter<Tournament> {

  private final Context context;
  private final List<Tournament> tournaments;

  public TournamentListAdapter(@NonNull final Context context, @NonNull final List<Tournament> tournaments) {
    super(context, 0, tournaments);
    this.context = context;
    this.tournaments = tournaments;
  }

  @NonNull
  @Override
  public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
    var listItem = convertView;
    if (listItem == null)
      listItem = LayoutInflater.from(context).inflate(R.layout.lobby_tournament_item, parent, false);
    var tournament = tournaments.get(position);
    var textView = (TextView) listItem.findViewById(R.id.title);
    textView.setText("Turnier: " + tournament.tournamentID + "      " + tournament.playerCount + " Spieler");
    return listItem;
  }
}
