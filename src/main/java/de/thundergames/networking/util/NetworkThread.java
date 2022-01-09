/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:21 by Carina Latest changes made by Carina on 09.01.22, 21:18 All contents of "NetworkThread" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

@Getter
public abstract class NetworkThread extends Thread {

  protected final Socket socket;
  private final PrintWriter writer;
  private final BufferedReader reader;
  protected Packet packet;
  @Setter
  protected int threadID;
  private boolean run = true;

  /**
   * Creates a new NetworkThread.
   *
   * @param socket   The socket to use.
   * @param threadID the id of the serverSocketConnection
   */
  public NetworkThread(@NotNull final Socket socket, final int threadID) throws IOException {
    this.socket = socket;
    this.threadID = threadID;
    if (this instanceof ServerThread) {
      System.out.println("Connection established with id: " + threadID + "!");
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
   * @use will be automatically started by a Server- or Client (Thread) it will wait for an incoming
   * packetMessage than decrypts it and turns it into a Packet
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
          var inputString = reader.readLine();
          if (inputString != null) {
            var object = new Gson().fromJson(inputString, JsonObject.class);
            packet = new Packet(object);
            if (this.packet != null) {
              if (this instanceof ServerThread
                && !packet.getPacketType().equals(Packets.MESSAGE.getPacketType())
                && packet.getValues() != null) {
                if (MoleGames.getMoleGames().getServer().isDebug()) {
                  System.out.println(
                    "Client with id: "
                      + this.threadID
                      + " sent: type: "
                      + packet.getPacketType()
                      + " contents: "
                      + packet.getValues().toString());
                }
              } else if (this instanceof ServerThread
                && !packet.getPacketType().equals(Packets.MESSAGE.getPacketType())) {
                if (MoleGames.getMoleGames().getServer().isDebug()) {
                  System.out.println(
                    "Client with id: "
                      + this.threadID
                      + " sent: type: "
                      + packet.getPacketType());
                }
              }
              readStringPacketInput(packet, this);
            }
          } else {
            if (socket.isConnected()) disconnect();
            return;
          }
        } else {
          if (socket.isConnected()) disconnect();
          return;
        }
      }
    } catch (@NotNull final Exception exe) {
      if (!(exe instanceof SocketException)) exe.printStackTrace();
    } finally {
      if (socket.isConnected()) {
        try {
          disconnect();
        } catch (IOException e) {
          e.printStackTrace();
        }
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
          var keyboardReader =
            new BufferedReader(
              new InputStreamReader(System.in, StandardCharsets.UTF_8), 16384);
          while (run) {
            try {
              var message = keyboardReader.readLine();
              var object = new JsonObject();
              var json = new JsonObject();
              if (client) {
                object.addProperty("type", Packets.MESSAGE.getPacketType());
                json.addProperty("message", message);
                object.add("value", json);
                sendPacket(new Packet(object));
              } else {
                for (ServerThread clientSocket :
                  new HashSet<>(MoleGames.getMoleGames().getServer().getClientThreads())) {
                  object.addProperty("type", Packets.MESSAGE.getPacketType());
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
   * @param receiver the one that it is receiving the thread of the server
   * @author Carina
   * @use it will automatically pass it forwards to the Server or Client to handle the Packet
   * depending on who received it (Server- or Client thread)
   */
  private void readStringPacketInput(
    @NotNull final Packet packet, @NotNull final NetworkThread receiver) throws NotAllowedError {
    if (receiver instanceof ClientThread && !(receiver instanceof AIClientThread)) {
      ((ClientThread) receiver).getClient().getClientPacketHandler().handlePacket(packet);
    } else if (receiver instanceof AIClientThread) {
      ((AIClientThread) receiver)
        .getAIClient()
        .getAIPacketHandler()
        .handlePacket(((AIClientThread) receiver).getAIClient(), packet);
    } else if (receiver instanceof ServerThread) {
      MoleGames.getMoleGames()
        .getServer()
        .getPacketHandler()
        .handlePacket(packet, (ServerThread) receiver);
    }
  }

  /**
   * @param data is the packet that will be sent in packet format but converted into a string
   *             seperated with #
   * @author Carina
   * @use create a Packet instance of a packet you want to send and pass it in form of a string
   */
  public void sendPacket(Packet data) {
    writer.println(new Gson().toJson(data.getJsonObject()));
  }

  /**
   * @author Carina
   * @use the basic logic of how a NetworkThread will disconnect
   */
  public abstract void disconnect() throws IOException;

  public void endConnection() {
    run = false;
  }
}
