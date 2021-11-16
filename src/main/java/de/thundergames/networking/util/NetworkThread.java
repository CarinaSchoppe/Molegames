/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:43
 * All contents of "NetworkThread" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.ausrichter.networking.GameMasterClientThread;
import de.thundergames.networking.client.Client;
import de.thundergames.networking.client.ClientThread;
import de.thundergames.networking.server.Server;
import de.thundergames.networking.server.ServerThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public abstract class NetworkThread extends Thread {
  protected final Socket socket;
  private final PrintWriter writer;
  private final BufferedReader reader;
  protected Packet packet;
  protected int id;

  /**
   * Creates a new NetworkThread.
   *
   * @param socket The socket to use.
   * @param id the id of the serverSocketConnection
   */
  public NetworkThread(@NotNull final Socket socket, final int id) throws IOException {
    this.socket = socket;
    this.id = id;
    if (this instanceof ServerThread)
      System.out.println("Connection established with id: " + id + "!");
    reader =
        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    writer = new PrintWriter(socket.getOutputStream(), true);
  }

  /**
   * @author Carina creates a runnable that will create a listener for the incomming packets and
   *     reaches them over to
   * @use will be automaticlly started by a Server- or Client (Thread) it will wait for an incomming
   *     packetmessage than decrypts it and turns it into a Packet
   * @see readStringPacketInput() method to use that packet for a client- or server handling
   */
  @Override
  public void run() {
    if (this instanceof ServerThread && !Server.isKeyboard()) {
      keyBoardListener(false);
      Server.setKeyboard(true);
    } else if (this instanceof ClientThread
        && Client.isKeyListener()
        && !(this instanceof GameMasterClientThread)) {
      keyBoardListener(true);
    }
    try {
      while (true) {
        if (socket.isConnected()) {
          String message = null; // ließt die packetmessage die reinkommt
          try {
            message = reader.readLine();
          } catch (IOException e) {
          }
          if (message != null) {
            System.out.println("input message "+ message);
            var object = new JSONObject(message);
            if (!object.isNull("type")) {
              packet = new Packet(object);
              if ("DISCONNECT".equals(packet.getPacketType())) {
                System.out.println("Content: " + packet.getValues().toString());
                disconnect();
                break;
              }
            }
            if (this.packet != null) {
              if (this instanceof ServerThread
                  && !packet.getPacketType().equals(Packets.MESSAGE.getPacketType()))
                System.out.println(
                    "Client with id: "
                        + this.id
                        + " sended: type: "
                        + packet.getPacketType()
                        + " contents: "
                        + packet.getValues().toString());
              readStringPacketInput(packet, this);
            }
          }
        } else {
          disconnect();
        }
      }
    } catch (PacketNotExistsException exception) {
      exception.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param client checks if the keyBoardlistener is started by a client or a server
   * @author Carina
   */
  private void keyBoardListener(final boolean client) {
    new Thread(
            () -> {
              try {
                System.out.println("Keylistener started!");
                var keyboardReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                  try {
                    var message = keyboardReader.readLine();
                    var object = new JSONObject();
                    if (client) {
                      object.put("type", Packets.MESSAGE.getPacketType());
                      var json = new JSONObject();
                      json.put("message", message);
                      object.put("values", json.toString());
                      System.out.println("keyboard input that will be send: " + object);
                      sendPacket(new Packet(object));
                    } else {
                      for (var iterator =
                              MoleGames.getMoleGames().getServer().getClientThreads().iterator();
                          iterator.hasNext(); ) {
                        ServerThread clientSocket = iterator.next();
                        object.put("type", Packets.MESSAGE.getPacketType());
                        var json = new JSONObject();
                        json.put("message", message);
                        object.put("values", json.toString());
                        clientSocket.sendPacket(new Packet(object));
                      }
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * @param packet that got read in by the runnable listener
   * @param reciever the one that it is recieving the thread of the server
   * @author Carina
   * @use it will automaticlly pass it forwards to the Server or Client to handle the Packet
   *     depending on who recieved it (Server- or Client thread)
   */
  public void readStringPacketInput(
      @NotNull final Packet packet, @NotNull final NetworkThread reciever)
      throws PacketNotExistsException {
    // TODO: How to handle the packet from the client! Player has moved -> now in a hole and than
    // handle it
    if (reciever instanceof ServerThread) {
      MoleGames.getMoleGames().getPacketHandler().handlePacket(packet, (ServerThread) reciever);
    } else if (reciever instanceof ClientThread && !(reciever instanceof GameMasterClientThread)) {
      ((ClientThread) reciever)
          .getClient()
          .getClientPacketHandler()
          .handlePacket(((ClientThread) reciever).getClient(), packet);
    } else if (reciever instanceof GameMasterClientThread) {
      ((GameMasterClientThread) reciever)
          .getGameMasterClient()
          .getClientMasterPacketHandler()
          .handlePacket(((GameMasterClientThread) reciever).getGameMasterClient(), packet);
    } else if (reciever instanceof AIClientThread) {
      ((AIClientThread) reciever)
          .getAIClient()
          .getAIPacketHandler()
          .handlePacket(((AIClientThread) reciever).getAIClient(), packet);
    }
  }

  /**
   * @param data is the packet that will be send in packet format but converted into a string
   *     seperated with #
   * @author Carina
   * @use create a Packet instance of a packet you want to send and pass it in in form of a string
   *     seperating the objects with #
   */
  public void sendPacket(Packet data) {
    writer.println(data.getJsonPacket().toString());
  }

  /**
   * @author Carina
   * @use the basic logic of how a NetworkThread will disconnect
   */
  public abstract void disconnect();

  public int getConnectionId() {
    return id;
  }
}
