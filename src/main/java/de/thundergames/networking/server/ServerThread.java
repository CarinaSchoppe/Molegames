/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:53
 * All contents of "ServerThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import de.thundergames.MoleGames;
import de.thundergames.networking.util.NetworkThread;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends NetworkThread {

  private NetworkPlayer player;
  private String clientName;

  /**
   * @param socket the server Socket
   * @param id Serverthread id
   * @author Carina
   */
  public ServerThread(@NotNull final Socket socket, final int id) throws IOException {
    super(socket, id);
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public NetworkPlayer getPlayer() {
    return player;
  }

  public void setPlayer(NetworkPlayer player) {
    this.player = player;
  }

  /**
   * @author Carina
   * @use disconnects the serverThread and removes it from the lists and maps
   */
  @Override
  public void disconnect() {
    try {
      if (MoleGames.getMoleGames().getGameHandler().getClientGames().containsKey(this)) {
        MoleGames.getMoleGames().getGameHandler().getClientGames().get(this).removePlayerFromGame(MoleGames.getMoleGames().getGameHandler().getClientGames().get(this).getClientPlayersMap().get(this));
      }
      System.out.println("Disconnecting " + this.getClientName());
      MoleGames.getMoleGames().getServer().getConnectionNames().remove(this.getClientName());
      MoleGames.getMoleGames().getServer().getClientThreads().remove(this);
      MoleGames.getMoleGames().getServer().getThreadIds().remove(getConnectionID());
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
