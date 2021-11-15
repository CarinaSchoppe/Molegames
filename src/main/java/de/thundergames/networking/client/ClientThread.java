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
package de.thundergames.networking.client;

import de.thundergames.networking.util.NetworkThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class ClientThread extends NetworkThread {

  protected Client client;

  public ClientThread(@NotNull final Socket socket, final int id, Client client) throws IOException {
    super(socket, id);
    this.client = client;
  }

  public Client getClient() {
    return client;
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
        System.out.println("Server disconnected!");
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void setID(final int id) {
    this.id = id;
  }
}
