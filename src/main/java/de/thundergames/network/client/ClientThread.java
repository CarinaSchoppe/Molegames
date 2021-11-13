package de.thundergames.network.client;

import de.thundergames.network.util.NetworkThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class ClientThread extends NetworkThread {

  private Client client;

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
