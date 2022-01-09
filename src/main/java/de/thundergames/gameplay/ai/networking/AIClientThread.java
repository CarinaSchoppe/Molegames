/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "AIClientThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */ient;
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
