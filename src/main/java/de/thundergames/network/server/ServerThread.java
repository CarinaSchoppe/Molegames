package de.thundergames.network.server;

import de.thundergames.network.util.NetworkThread;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends NetworkThread {

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
  public synchronized void disconnect() {
    {
      try {
        Server.getClientThreads().remove(this);
        Server.getThreadIds().remove(getConnectionId());
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
