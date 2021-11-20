/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "ClientPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.util.Packet;
import org.jetbrains.annotations.NotNull;


public class ClientPacketHandler {

  public void handlePacket(@NotNull final  Client client,@NotNull final Packet packet) {
  }
/*

TODO: hier
  */
/**
   * @param client the client instance that will be passed to the method for handling
   * @param packet the packet that got send by the server
   * @author Carina
   * @use handles the packet that came in
   * @see PacketHandler the packetHandler by the Server as a reference
   *//*

  public void handlePacket(@NotNull final Client client, @NotNull final Packet packet)
      throws UndefinedError {
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      welcomePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      joinGamePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      handleMessagePacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.INVALIDMOVE.getPacketType())) {
      handleInvalidMovePacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.PLACEMOLE.getPacketType())) {
      handlePlaceMolePacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.MOVEMOLE.getPacketType())) {
      handleMoveMolePacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.MOLES.getPacketType())) {
      handleMolesPacket(client, packet);
    }  else if (packet.getPacketType().equals(Packets.NEXTPLAYER.getPacketType())) {
      nextPlayerPacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.TURNOVER.getPacketType())) {
      turnOverPacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.DRAWNCARD.getPacketType())) {
      handleDrawnCardPacket(client, packet);
    } else if (packet.getPacketType().equals(Packets.NOTEXISTS.getPacketType())) {
      handleNotExistsPacket(client, packet);
    } else {
      throw new UndefinedError(
          "Packet with type: " + packet.getPacketType() + " does not exists");
    }
  }


  */
/**
   * @author Carina
   * @param clientConnection
   * @param name
   * @use sends the login packet to the server and wants response with welcome
   *//*

  public void loginPacket(@NotNull ClientThread clientConnection, String name){
*/
/*TODO: hier

       clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.LOGIN.getPacketType()).put("value", new JSONObject().put("name", name).toString())));*//*

  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use send by server to welcome new client connection with the clientID
   *//*

  public void welcomePacket(@NotNull final Client client, @NotNull final Packet packet) {
*/
/*TODO: hier    var id = packet.getValues().getInt("clientID");
    client.setId(id);
    System.out.println("Client ID: " + id);
    client.getClientThread().setID(id);*//*

  }


  */
/**
   * @author Carina
   * @param clientConnection
   * @use send to the server when a connection will be removed
   *//*

  public void logoutPacket(@NotNull ClientThread clientConnection){
   //TODO: hier clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.LOGOUT.getPacketType())));
  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a player into the game
   *//*

  private void joinGamePacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Client joined game with id: " + packet.getValues().getInt("gameID"));
    client.setGameID(packet.getValues().getInt("gameID"));
  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the message that was send with the packet
   *//*

  private void handleMessagePacket(@NotNull final Client client, @NotNull final Packet packet) {
    try {
      System.out.println("Server sended: " + packet.getValues().getString("message"));
    } catch (JSONException e) {
      System.out.println("Server sended: " + "no packet content!");
    }
  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet if the player did in invalid move
   *//*

  private void handleInvalidMovePacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Server: Youve done an invalid move");
  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the placement of a mole by a player
   *//*

  private void handlePlaceMolePacket(@NotNull final Client client, @NotNull final Packet packet) {
    {

    }

  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the movement if a mole was moved by any player
   *//*

  private void handleMoveMolePacket(@NotNull final Client client, @NotNull final Packet packet) {
  }

  */
/**
   * @param client
   * @param packet handles the packet when the server gives the player its moleIDs
   * @author Carina
   *//*

  private void handleMolesPacket(@NotNull final Client client, @NotNull final Packet packet) {
    for (int i = 0; i < packet.getValues().getJSONArray("moles").toList().size(); i++) {
      client.getMoleIDs().add(packet.getValues().getJSONArray("moles").getInt(i));
    }
  }



  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet when the player is on the turn
   *//*

  private void nextPlayerPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Server sended: You are now on the turn!");
  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use is send when the server sais the players Turn is over.
   *//*

  private void turnOverPacket(@NotNull final Client client, @NotNull final Packet packet) {

  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet if something does not exist
   *//*

  private void handleNotExistsPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("The game you wanted to join does not exist!");

  }

  */
/**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packet if a drawn card was send
   *//*

  private void handleDrawnCardPacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Server sended: Your card value is: " + packet.getValues().getInt("card"));
  }


*/

}
