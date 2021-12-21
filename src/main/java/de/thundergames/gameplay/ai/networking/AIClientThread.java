/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 13:57 by Carina Latest changes made by Carina on 21.12.21, 13:55 All contents of "AIClientThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.networking.ClientThread;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class AIClientThread extends ClientThread {

  public AIClientThread(@NotNull Socket socket, final int id, @NotNull final Client client)
    throws IOException {
    super(socket, id, client);
  }

  public AI getAIClient() {
    return (AI) client;
  }
}
