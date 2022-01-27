package de.pentagames.maulwurfkompanie.ui.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.Game;
import upb.maulwurfcompany.library.data.GameStatus;
import upb.maulwurfcompany.library.data.Tournament;
import upb.maulwurfcompany.library.messages.Overview;

public class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.TabHolder> {

  public static final String[] tabTitles = new String[]{"Geplant", "Laufend", "Beendet", "Turniere"};
  private static final GameStatus[] tabStates = new GameStatus[]{GameStatus.NOT_STARTED, GameStatus.STARTED, GameStatus.OVER};
  private static final BaseAdapter[] adapters = new BaseAdapter[4];
  private final ArrayList<Game>[] games = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
  private final ArrayList<Tournament> tournaments = new ArrayList<>();

  public TabsAdapter() {
    if (Client.messageHandler.overview != null) overviewUpdated(Client.messageHandler.overview);
  }

  /**
   * This method refreshes the lists and the shown games according to an updated {@link Overview}.
   *
   * @param overview The current {@link Overview}.
   */
  public void overviewUpdated(@NotNull final Overview overview) {
    for (var i = 0; i < 3; i++) {
      games[i].clear();
      for (var game : overview.games) {
        if (game.status == tabStates[i])
          games[i].add(game);
      }
      if (adapters[i] != null) adapters[i].notifyDataSetChanged();
    }
    tournaments.clear();
    tournaments.addAll(Arrays.asList(overview.tournaments));
    if (adapters[3] != null) adapters[3].notifyDataSetChanged();
  }

  @NonNull
  @Override
  public TabHolder onCreateViewHolder(@NotNull final ViewGroup parent, final int viewType) {
    return new TabHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lobby_tab, parent, false));
  }

  @Override
  public void onBindViewHolder(@NotNull final TabHolder holder, final int position) {
    if (position < 3) {
      adapters[position] = new GameListAdapter(holder.listView.getContext(), games[position], tabStates[position]);
    } else {
      adapters[position] = new TournamentListAdapter(holder.listView.getContext(), tournaments);
    }
    holder.listView.setAdapter(adapters[position]);
  }

  @Override
  public long getItemId(final int position) {
    return super.getItemId(position);
  }

  @Override
  public int getItemCount() {
    return 4;
  }

  public static class TabHolder extends RecyclerView.ViewHolder {

    public final ListView listView;

    public TabHolder(@NotNull final View itemView) {
      super(itemView);
      listView = itemView.findViewById(R.id.listview);
    }
  }
}
