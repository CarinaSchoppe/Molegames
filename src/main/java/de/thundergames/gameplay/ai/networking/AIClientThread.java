package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.gameplay.player.networking.ClientThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class AIClientThread extends ClientThread {

  public AIClientThread(@NotNull Socket socket, int id, Client client) throws IOException {
    super(socket, id, client);

  }

  public AI getAIClient() {
    return (AI) client;
  }
}
