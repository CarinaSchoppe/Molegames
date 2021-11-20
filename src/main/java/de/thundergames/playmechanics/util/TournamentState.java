package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.util.interfaceItems.NetworkPlayer;

public class TournamentState {

  private final NetworkPlayer[] players;
  private final Score score;
  private final NetworkGame[] games;


  public TournamentState(NetworkPlayer[] players, Score score, NetworkGame[] games) {
    this.players = players;
    this.score = score;
    this.games = games;
  }
}
