/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "GameMasterClient" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.gamemaster;

import de.thundergames.gameplay.gamemaster.networking.GameMasterClientThread;
import de.thundergames.gameplay.gamemaster.networking.GameMasterPacketHandler;
import de.thundergames.networking.client.Client;
import de.thundergames.networking.client.ClientThread;
import java.io.IOException;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public class GameMasterClient extends Client {

  public GameMasterClientThread getMasterClientThread() {
    return (GameMasterClientThread) clientThread;
  }

  private static GameMasterClient clientInstance;

  public static GameMasterClient getClientInstance() {
    return clientInstance;
  }

  private static int gamesID = 0;

  public static int getGameID() {
    return gamesID;
  }

  public static void setSystemGameID(int gameID) {
    GameMasterClient.gamesID = gameID;
  }

  public static void setClientInstance(GameMasterClient clientInstance) {
    GameMasterClient.clientInstance = clientInstance;
  }

  public GameMasterClient(int port, @NotNull String ip) {
    super(port, ip, "Ausrichter");
    clientPacketHandler = new GameMasterPacketHandler();
  }

  public GameMasterPacketHandler getClientMasterPacketHandler() {
    return (GameMasterPacketHandler) clientPacketHandler;
  }

  /**
   * @author Carina
   * @use connects the client to the server
   * @see ClientThread for more
   */
  @Override
  public void connect() {
    try {
      socket = new Socket(ip, port);
      clientThread = new GameMasterClientThread(socket, 0, this);
      clientThread.start();
    } catch (IOException e) {
      System.out.println("Is the server running?!");
    }
  }
}
