/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 23:09 by Carina latest changes made by Carina on 06.12.21, 23:09
 * All contents of "NetworkThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.player.networking.ClientThread;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public abstract class NetworkThread extends Thread {

  protected final Socket socket;
  private final PrintWriter writer;
  private final BufferedReader reader;
  protected Packet packet;
  protected int id;
  private boolean run = true;

  /**
   * Creates a new NetworkThread.
   *
   * @param socket The socket to use.
   * @param id     the id of the serverSocketConnection
   */
  public NetworkThread(@NotNull final Socket socket, final int id) throws IOException {
    this.socket = socket;
    this.id = id;
    if (this instanceof ServerThread) {
      System.out.println("Connection established with id: " + id + "!");
    }
    reader =
      new BufferedReader(
        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8), 16384);
    writer =
      new PrintWriter(
        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
  }

  /**
   * @author Carina creates a runnable that will create a listener for the incomming packets and
   * reaches them over to
   * @use will be automaticlly started by a Server- or Client (Thread) it will wait for an incomming
   * packetmessage than decrypts it and turns it into a Packet
   */
  @Override
  public void run() {
    if (this instanceof ServerThread && !MoleGames.getMoleGames().getServer().isKeyboard()) {
      keyBoardListener(false);
      MoleGames.getMoleGames().getServer().setKeyboard(true);
    } else if (this instanceof ClientThread) {
      keyBoardListener(true);
    }
    try {
      while (run) {
        if (socket.isConnected()) {
          var message = reader.readLine();
          if (message != null) {
            var object = new Gson().fromJson(message, JsonObject.class);
            if (object.get("type") != null) {
              packet = new Packet(object.get("type").getAsString(), object);
            }
            if (this.packet != null) {
              if (this instanceof ServerThread
                && !packet.getPacketType().equals(Packets.MESSAGE.getPacketType())
                && packet.getValues() != null) {
                if (MoleGames.getMoleGames().getServer().isDebug()) {
                  System.out.println(
                    "Client with id: "
                      + this.id
                      + " sended: type: "
                      + packet.getPacketType()
                      + " contents: "
                      + packet.getValues().toString());
                }
              } else if (this instanceof ServerThread
                && !packet.getPacketType().equals(Packets.MESSAGE.getPacketType())) {
                if (MoleGames.getMoleGames().getServer().isDebug()) {
                  System.out.println(
                    "Client with id: "
                      + this.id
                      + " sended: type: "
                      + packet.getPacketType());
                }
              }
              readStringPacketInput(packet, this);
            }
          } else {
            if (socket.isConnected())
              disconnect();
            return;
          }
        } else {
          if (socket.isConnected())
            disconnect();
          return;
        }
      }
    } catch (@NotNull final Exception exe) {
      if (!(exe instanceof SocketException))
        exe.printStackTrace();
    } finally {
      if (socket.isConnected()) {
        disconnect();
      }
      return;
    }
  }

  /**
   * @param client checks if the keyBoardlistener is started by a client or a server
   * @author Carina
   */
  private synchronized void keyBoardListener(final boolean client) {
    new Thread(
      () -> {
        try {
          System.out.println("Keylistener started!");
          var keyboardReader =
            new BufferedReader(
              new InputStreamReader(System.in, StandardCharsets.UTF_8), 16384);
          while (run) {
            try {
              var message = keyboardReader.readLine();
              var object = new JsonObject();
              if (client) {
                object.addProperty("type", Packets.MESSAGE.getPacketType());
                var json = new JsonObject();
                json.addProperty("message", message);
                object.add("value", json);
                sendPacket(new Packet(object));
              } else {
                for (var iterator =
                     MoleGames.getMoleGames().getServer().getClientThreads().iterator();
                     iterator.hasNext(); ) {
                  ServerThread clientSocket = iterator.next();
                  object.addProperty("type", Packets.MESSAGE.getPacketType());
                  var json = new JsonObject();
                  json.addProperty("message", message);
                  object.add("value", json);
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
   * @param packet   that got read in by the runnable listener
   * @param reciever the one that it is recieving the thread of the server
   * @author Carina
   * @use it will automaticlly pass it forwards to the Server or Client to handle the Packet
   * depending on who recieved it (Server- or Client thread)
   */
  private void readStringPacketInput(
    @NotNull final Packet packet, @NotNull final NetworkThread reciever) throws NotAllowedError {
    if (reciever instanceof ClientThread && !(reciever instanceof AIClientThread)) {
      ((ClientThread) reciever)
        .getClient()
        .getClientPacketHandler()
        .handlePacket(((ClientThread) reciever).getClient(), packet);
    } else if (reciever instanceof AIClientThread) {
      ((AIClientThread) reciever)
        .getAIClient()
        .getAIPacketHandler()
        .handlePacket(((AIClientThread) reciever).getAIClient(), packet);
    } else if (reciever instanceof ServerThread) {
      MoleGames.getMoleGames().getPacketHandler().handlePacket(packet, (ServerThread) reciever);
    }
  }

  /**
   * @param data is the packet that will be send in packet format but converted into a string
   *             seperated with #
   * @author Carina
   * @use create a Packet instance of a packet you want to send and pass it in in form of a string
   */
  public void sendPacket(Packet data) {
    writer.println(new Gson().toJson(data.getJsonObject()));
  }

  /**
   * @author Carina
   * @use the basic logic of how a NetworkThread will disconnect
   */
  public abstract void disconnect();

  public int getConnectionID() {
    return id;
  }

  public Socket getSocket() {
    return socket;
  }

  public void endConnection() {
    run = false;
  }
}
