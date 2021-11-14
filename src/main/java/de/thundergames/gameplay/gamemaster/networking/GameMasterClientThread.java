/*
 *
 *  *     / **
 *  *      *   Copyright Notice                                             *
 *  *      *   Copyright (c) SwtPra10 | ThunderGames 2021                         *
 *  *      *   Created: 05.05.2018 / 11:59                                  *
 *  *      *   All contents of this source text are protected by copyright. *
 *  *      *   The copyright law, unless expressly indicated otherwise, is  *
 *  *      *   at SwtPra10 | ThunderGames. All rights reserved                    *
 *  *      *   Any type of duplication, distribution, rental, sale, award,  *
 *  *      *   Public accessibility or other use                            *
 *  *      *   Requires the express written consent of SwtPra10 | ThunderGames.   *
 *  *      **
 *  *
 */

package de.thundergames.gameplay.gamemaster.networking;

import de.thundergames.gameplay.gamemaster.GameMasterClient;
import de.thundergames.networking.client.Client;
import de.thundergames.networking.client.ClientThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class GameMasterClientThread extends ClientThread {

  public GameMasterClientThread(@NotNull Socket socket, int id, Client client) throws IOException {
    super(socket, id, client);
  }

  public GameMasterClient getGameMasterClient() {
    return (GameMasterClient) client;
  }

  @Override
  public void disconnect() {
    super.disconnect();
  }
}
