/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 16.12.21, 16:15 by Carina Latest changes made by Carina on 16.12.21, 16:13 All contents of "ServerThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import de.thundergames.MoleGames;
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

  /**
   * @param socket the server Socket
   * @param id     Serverthread id
   * @author Carina
   */
  public ServerThread(@NotNull final Socket socket, final int id) throws IOException {
    super(socket, id);
  }

  /**
   * @author Carina
   * @use disconnects the serverThread and removes it from the lists and maps
   */
  @Override
  public void disconnect() {
    try {
      getPlayer().getGame().getGameUtil().nextPlayer();
      if (getPlayer() != null) {
        if (getPlayer().getGame() != null) {
          getPlayer().getGame().removePlayerFromGame(MoleGames.getMoleGames().getGameHandler().getClientGames().get(this).getClientPlayersMap().get(this));
        }
      }
      MoleGames.getMoleGames().getServer().getConnectionNames().remove(this.getClientName());
      MoleGames.getMoleGames().getServer().getClientThreads().remove(this);
      MoleGames.getMoleGames().getServer().getThreadIDs().remove(getThreadID());
      socket.close();
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println("Disconnecting " + this.getClientName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
