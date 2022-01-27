/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 18:19 by Carina Latest changes made by Carina on 20.01.22, 17:59 All contents of "Tournament" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.tournament;

import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.ausrichter.ui.MainGUI_ALT;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.playmechanics.game.Game;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;

@Getter
@Setter
public class Tournament {
  //TODO: Alles machen

  private final transient HashSet<ServerThread> spectators = new HashSet<>();
  private final transient HashSet<ServerThread> players = new HashSet<>();
  private final int tournamentID;
  private final HashSet<Game> games = new HashSet<>();
  private transient Score score;
  private transient TournamentState tournamentState;
  private int playerCount;
  private TournamentStatus status;

  public Tournament(final int tournamentID) {
    this.tournamentID = tournamentID;
  }

  /**
   * @param client
   * @author Carina
   * @use handles the joining of a player to a tournament
   */
  public void joinTournament(@NotNull final ServerThread client, final boolean participant) {
    MoleGames.getMoleGames().getGameHandler().getClientTournaments().put(client, this);
    updateTournamentState();
    playerCount++;
    MainGUI_ALT.getGUI().updateTable();
  }

  /**
   * @author Carina
   * @use creates a new tournament will all stuff needed
   */
  public void create() {
    this.score = new Score();
    this.tournamentState = new TournamentState(score, TournamentStatus.NOT_STARTED);
    MoleGames.getMoleGames().getGameHandler().getTournaments().add(this);
    setStatus(TournamentStatus.NOT_STARTED);
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().put(tournamentID, this);
    for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
      MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(client);
    }
  }

  /**
   * @param client
   * @author Carina
   * @use handles when a player wants to leave the tournament not the game in a tournament
   */
  public void leaveTournament(ServerThread client) {
    playerCount--;
    players.remove(client);
    updateTournamentState();
  }

  public HashSet<Game> addGame(Game game) {
    this.games.add(game);
    return this.games;
  }

  /**
   * @author Carina
   * @use updates the tournament state
   */
  public void updateTournamentState() {
    tournamentState.setScore(score);
    for (var client : players) {
      tournamentState.getPlayers().add(client.getPlayer());
    }
  }

  /**
   * @return tournamentID with a hashtag in front of it
   */
  public String getHashtagWithTournamentID() {
    return "#" + tournamentID;
  }

  /**
   * @return current player count and the maximum player count with a slash between both
   */
  public String getCurrentPlayerCount_MaxCount() {
    return players.size() + "/ XX";
  }

  /**
   * @return current player count and the maximum player count with a slash between both
   */
  public String getStatusForTableView() {
    return Objects.equals(status, TournamentStatus.NOT_STARTED) ? "OPEN" : status.getStatus();
  }
  // endregion

  public HashSet<ServerThread> getSpectators() {
    return spectators;
  }

  public HashSet<ServerThread> getPlayers() {
    return players;
  }

  public int getTournamentID() {
    return tournamentID;
  }

  public HashSet<Game> getGames() {
    return games;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }

  public TournamentState getTournamentState() {
    return tournamentState;
  }

  public void setTournamentState(TournamentState tournamentState) {
    this.tournamentState = tournamentState;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public TournamentStatus getStatus() {
    return status;
  }

  public void setStatus(TournamentStatus status) {
    this.status = status;
  }
}
