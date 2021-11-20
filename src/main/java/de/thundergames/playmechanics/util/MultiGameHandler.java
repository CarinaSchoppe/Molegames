/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "MultiGameHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import com.google.gson.JsonObject;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.game.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Carina
 * @use the handler that is handling the multi de.thundergames.game mechanics that are running by
 * @see de.thundergames.networking.server.Server the server that is using this clas
 * @see ServerThread the thread that is instanciated by the server
 */
public class MultiGameHandler {

  private final ArrayList<NetworkGame> games = new ArrayList<>();
  private final ArrayList<Tournament> tournaments = new ArrayList<>();
  private final HashMap<Integer, Game> idGames = new HashMap<>();
  private final HashMap<Integer, Tournament> idTournaments = new HashMap<>();
  private final HashMap<ServerThread, Game> clientGames = new HashMap<>();

  /**
   * @author Carina
   * @use creates the new Game1
   */
  public void createNewGame(final int gameID, @NotNull final ServerThread ausrichter) {
    if (!idGames.containsKey(gameID)) {
      var game = new Game(gameID);
      try {
        game.create();
      } catch (IOException e) {
        e.printStackTrace();
      }
      games.add(game);
      idGames.put(gameID, game);
    } else {
      var json = new JsonObject();
      json.addProperty("type", Packets.GAMEEXISTS.getPacketType());
      ausrichter.sendPacket(new Packet(json));
    }
  }

  /**
   * @author Carina
   * @use creates the new Game1
   */
  public void createNewTournament(final int tournamentID, @NotNull final ServerThread ausrichter) {
    if (!idGames.containsKey(tournamentID)) {
      var tournament = new Tournament(tournamentID);
      tournament.create();
      tournaments.add(tournament);
      idTournaments.put(tournamentID, tournament);
    } else {
      var json = new JsonObject();
      json.addProperty("type", Packets.GAMEEXISTS.getPacketType());
      ausrichter.sendPacket(new Packet(json));
    }
  }

  public HashMap<Integer, Game> getIDGames() {
    return idGames;
  }

  public HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }

  public ArrayList<Tournament> getTournaments() {
    return tournaments;
  }

  public ArrayList<NetworkGame> getGames() {
    return games;
  }

  public HashMap<Integer, Tournament> getIdTournaments() {
    return idTournaments;
  }
}
