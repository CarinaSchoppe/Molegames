/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.11.21, 20:03 by Carina latest changes made by Carina on 24.11.21, 20:03 All contents of "Tournament" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import java.util.HashSet;
import org.jetbrains.annotations.NotNull;

public class Tournament {

  private final int tournamentID;
  private final transient HashSet<ServerThread> clients = new HashSet<>();
  private int playerCount;
  private Score score;
  private HashSet<NetworkGame> games;
  private transient TournamentState tournamentState;

  public Tournament(int tournamentID) {
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
  }

  /**
   * @author Carina
   * @use creates a new tournament will all stuff needed
   */
  public void create() {
    this.score = new Score();
    this.tournamentState = new TournamentState();
    MoleGames.getMoleGames().getGameHandler().getTournaments().add(this);
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().put(tournamentID, this);
    for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
      MoleGames.getMoleGames().getPacketHandler().overviewPacket(client);
    }
  }

  /**
   * @param client
   * @author Carina
   * @use handles when a player wants to leave the tournament not the game in a tournament
   */
  public void leaveTournament(ServerThread client) {
    playerCount--;
    clients.remove(client);
    updateTournamentState();
  }

  /**
   * @author Carina
   * @use updates the tournament state
   */
  public void updateTournamentState() {
    tournamentState.setScore(score);
    for (var client : clients) {
      tournamentState.getPlayers().add(client.getPlayer());
    }
  }

  public HashSet<ServerThread> getClients() {
    return clients;
  }

  public int getTournamentID() {
    return tournamentID;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }


  public HashSet<NetworkGame> getGames() {
    return games;
  }

  public Score getScore() {
    return score;
  }

  public TournamentState getTournamentState() {
    return tournamentState;
  }
}
