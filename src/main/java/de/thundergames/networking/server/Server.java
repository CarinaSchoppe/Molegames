/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 18:17 by Carina latest changes made by Carina on 02.12.21, 18:17
 * All contents of "Server" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.server;

import de.thundergames.MoleGames;
import de.thundergames.networking.util.Network;
import de.thundergames.networking.util.NetworkThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.Tournament;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Network {

  private final ArrayList<ServerThread> clientThreads = new ArrayList<>();
  private final ArrayList<ServerThread> observer = new ArrayList<>();
  private final HashMap<Integer, ServerThread> threadIDs = new HashMap<>();
  private final HashMap<String, ServerThread> connectionNames = new HashMap<>();
  private final HashMap<Integer, ServerThread> connectionIDs = new HashMap<>();
  private int threadID = 0;
  private boolean keyboard = false;

  /**
   * @param port obvious the Serverport in case of empty localhost
   * @param ip   obvious the ServerIp in case of empty localhost
   * @author Carina
   * @use creates a Server with a @param serverSocket and uses this one to create a ServerThread which will handle the Inputreading and got info about the Outputsending adds every ServerThread to a List and adds an Id to it and puts that into a Map
   */
  public Server(final int port, @NotNull final String ip) {
    super(port, ip);
  }

  public boolean isKeyboard() {
    return keyboard;
  }

  public void setKeyboard(final boolean keyboard) {
    this.keyboard = keyboard;
  }

  public HashMap<Integer, ServerThread> getThreadIds() {
    return threadIDs;
  }

  public HashMap<String, ServerThread> getConnectionNames() {
    return connectionNames;
  }

  public HashMap<Integer, ServerThread> getConnectionIDs() {
    return connectionIDs;
  }


  /**
   * @author Carina
   * @use creates the Server starts it and runs the handler for the incomming client-connections
   * @see NetworkThread as the Network for the instance of the ServerThread
   * @see ServerThread as an instance that will be created here
   */
  @Override
  public void create() {
    new Thread(
            () -> {
              try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server listening on port " + getPort());
                while (true) {
                  socket = serverSocket.accept();
                  ServerThread serverThread = new ServerThread(socket, threadID);
                  getConnectionIDs().put(threadID, serverThread);
                  getClientThreads().add(serverThread);
                  serverThread.start();
                  MoleGames.getMoleGames().getPacketHandler().welcomePacket(serverThread, threadID);

                  threadIDs.put(serverThread.getConnectionID(), serverThread);
                  threadID++;
                }
              } catch (IOException e) {
                e.printStackTrace();
              } finally {
                try {
                  socket.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
  }

  /**
   * @param game   the game that all clients are connected to
   * @param packet the packet that should be send
   * @use the method will send a packet to all connected clients of the game
   */
  public synchronized void sendToAllGameClients(
      @NotNull final Game game, @NotNull final Packet packet) {
    try {
      if (!game.getPlayers().isEmpty()) {
        for (var clients : game.getPlayers()) {
          clients.getServerClient().sendPacket(packet);
        }
      } else {
        System.out.println("The game with the ID: " + game.getGameID() + " is empty!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param tournament that all clients are connected to
   * @param packet     the packet that should be send
   * @use the method will send a packet to all connected clients of the game
   */
  public synchronized void sendToAllTournamentClients(
      @NotNull final Tournament tournament, @NotNull final Packet packet) {
    try {
      if (!tournament.getClients().isEmpty()) {
        for (var clients : tournament.getClients()) {
          clients.sendPacket(packet);
        }
      } else {
        System.out.println("The game with the ID: " + tournament.getTournamentID() + " is empty!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ServerThread> getClientThreads() {
    return clientThreads;
  }


  public ArrayList<ServerThread> getObserver() {
    return observer;
  }
}
