/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.networking.server;

import de.thundergames.MoleGames;
import de.thundergames.networking.util.NetworkThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class ServerThread extends NetworkThread {

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

  /**
   * @author Carina
   * @use disconnects the serverThread and removes it from the lists and maps
   */
  @Override
  public synchronized void disconnect() {
    {
      try {
        MoleGames.getMoleGames().getServer().getClientThreads().remove(this);
        Server.getThreadIds().remove(getConnectionId());
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
