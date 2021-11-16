/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:06
 * All contents of "ServerThread" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
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
   * @param id     Serverthread id
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
  public void disconnect() {
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
