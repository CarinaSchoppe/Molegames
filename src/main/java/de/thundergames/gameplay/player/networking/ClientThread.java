/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 20.12.21, 16:43 by Carina Latest changes made by Carina on 20.12.21, 16:18 All contents of "ClientThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import de.thundergames.gameplay.player.Client;
import de.thundergames.networking.util.NetworkThread;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class ClientThread extends NetworkThread {

  protected Client client;

  public ClientThread(@NotNull final Socket socket, final int id, @NotNull final Client client)
      throws IOException {
    super(socket, id);
    this.client = client;
  }

  /**
   * @author Carina
   * @use disconnects the ClientThread from the System and will run the shutdown-logic of the
   *     playerSystem
   */
  @Override
  public void disconnect() {
    {
      try {
        if (client.isDebug()) System.out.println("Client: Connection to server disconnected!");
        socket.close();
        System.exit(5);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
