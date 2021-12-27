/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "ServerThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.ui.MainGUI;
import de.thundergames.gameplay.ausrichter.ui.PlayerManagement;
import de.thundergames.networking.util.NetworkThread;
import de.thundergames.playmechanics.util.Player;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class ServerThread extends NetworkThread {

  private Player networkPlayer;
  private String clientName;
  private Player player;
  private Server server;

  /**
   * @param socket the server Socket
   * @param id Serverthread id
   * @author Carina
   */
  public ServerThread(@NotNull final Socket socket, final int id, @NotNull final Server server)
      throws IOException {
    super(socket, id);
    this.server = server;
  }

  /**
   * @author Carina
   * @use disconnects the serverThread and removes it from the lists and maps
   */
  @Override
  public void disconnect() throws IOException {
    if (getPlayer() != null) {
      if (getPlayer().getGame() != null) {
        getPlayer()
            .getGame()
            .removePlayerFromGame(player.getGame().getClientPlayersMap().get(this));
        getPlayer().getGame().getGameUtil().nextPlayer();
      }
    }
    server.getLobbyThreads().remove(this);
    server.getPlayingThreads().remove(this);
    MoleGames.getMoleGames().getServer().getConnectionNames().remove(this.getClientName());
    MoleGames.getMoleGames().getServer().getClientThreads().remove(this);
    MoleGames.getMoleGames().getServer().getThreadIDs().remove(getThreadID());
    socket.close();
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      System.out.println("Disconnecting " + this.getClientName());
    }
    if (MainGUI.getGUI() != null) {
      MainGUI.getGUI().updateTable();
    }
    if (PlayerManagement.getPlayerManagement() != null) {
      PlayerManagement.getPlayerManagement().updateTable();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
