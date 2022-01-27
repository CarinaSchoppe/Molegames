package de.pentagames.maulwurfkompanie.client;

import org.jetbrains.annotations.NotNull;

import upb.maulwurfcompany.library.Message;

public class Client {
  public static ServerHandler serverHandler;
  public static MessageHandler messageHandler;
  /**
   * Variable to store the ip address of the server the user wants to connect to.
   */
  public static String ip;
  /**
   * Variable to store the port the user entered.
   */
  public static int port;
  /**
   * Variable to store the users username.
   */
  public static String username;
  /**
   * Variable to store the clientId assigned to the client by the server.
   */
  public static int clientId = -1;
  /**
   * Variable that states whether the user is a spectator or active player.
   */
  public static boolean participant = false;

  /**
   * This method establishes the connection to the server.
   *
   * @param ip       Ip address of the server the user wants to connect to.
   * @param port     Port the user entered.
   * @param username The users username.
   */
  public static void connectToServer(@NotNull final String ip, final int port, @NotNull final String username) {
    if (serverHandler != null && !serverHandler.isClosed() && ip.equals(Client.ip) && port == Client.port && username.equals(Client.username))
      return;
    Client.disconnectFromServer();
    Client.ip = ip;
    Client.port = port;
    Client.username = username;
    messageHandler = new MessageHandler();
    serverHandler = new ServerHandler(ip, port, messageHandler);
    new Thread(serverHandler).start();
  }

  /**
   * This method disconnects the client from the server.
   */
  public static void disconnectFromServer() {
    if (serverHandler != null)
      serverHandler.close();
  }

  /**
   * This method is used to send {@link Message} objects to the server via the {@link ServerHandler}.
   *
   * @param message {@link Message} object to send to the server.
   */
  public static void sendMessage(Message message) {
    serverHandler.sendMessage(message);
  }
  public static void sendMessages(Message... messages) {
    serverHandler.sendMessages(messages);
  }
}
