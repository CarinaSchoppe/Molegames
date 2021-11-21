/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "ClientPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;


public class ClientPacketHandler {


  /**
   * @param client the client instance that will be passed to the method for handling
   * @param packet the packet that got send by the server
   * @author Carina
   * @use handles the packet that came in
   * @see PacketHandler the packetHandler by the Server as a reference
   */
  public void handlePacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      handleWelcomePacket(client, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Server sended: " + packet.getValues().get("message").getAsString());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ASSIGNTOGAME.getPacketType())) {
      handleAssignToGamePacket(client, packet);
    }
  }

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use send by server to welcome new client connection with the clientID
   */
  protected void handleWelcomePacket(@NotNull final Client client, @NotNull final Packet packet) {
    if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
      System.exit(3);
    }
    client.setClientID(packet.getValues().get("clientID").getAsInt());
  }


  /**
   * @param client
   * @param name
   * @author Carina
   * @use sends the login packet to the server and wants response with welcome
   */
  public void loginPacket(@NotNull final Client client, @NotNull final String name) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("name", name);
    object.addProperty("type", Packets.LOGIN.getPacketType());
    object.add("value", json);
  }


  protected void handleOverviewPacket(@NotNull final Client client, @NotNull final Packet packet) {
    client.getGames().clear();
    client.getTournaments().clear();
    client.getGames().addAll(new Gson().fromJson(packet.getValues().get("games"), ArrayList.class));
  }


  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the joining of a player into the game
   */
  private void handleAssignToGamePacket(@NotNull final Client client, @NotNull final Packet packet) {
    System.out.println("Client joined game with id: " + packet.getValues().get("gameID").getAsInt());
    client.setGameID(packet.getValues().get("gameID").getAsInt());
  }

  public void joinGamePacket(@NotNull final Client client, @NotNull final int gameID, @NotNull final boolean player) {
    var object = new JsonObject();
    var json = new JsonObject();
    json.addProperty("gameID", gameID);
    json.addProperty("participant", player);
    object.addProperty("type", Packets.JOINGAME.getPacketType());
    object.add("value", json);
    client.getClientThread().sendPacket(new Packet(object));
  }


}











/*

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
/*

  public void loginPacket(@NotNull ClientThread clientConnection, String name){
*/
/*TODO: hier

       clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.LOGIN.getPacketType()).put("value", new JSONObject().put("name", name).toString())));*//*

  }

  */
/*

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
 * @param client
 * @param packet
 * @author Carina
 * @use handles the message that was send with the packet
 * @param client
 * @param packet
 * @author Carina
 * @use handles the packet if the player did in invalid move
 * @param client
 * @param packet
 * @author Carina
 * @use handles the placement of a mole by a player
 * @param client
 * @param packet
 * @author Carina
 * @use handles the movement if a mole was moved by any player
 * @param client
 * @param packet handles the packet when the server gives the player its moleIDs
 * @author Carina
 * @param client
 * @param packet
 * @author Carina
 * @use handles the packet when the player is on the turn
 * @param client
 * @param packet
 * @author Carina
 * @use is send when the server sais the players Turn is over.
 * @param client
 * @param packet
 * @author Carina
 * @use handles the packet if something does not exist
 * @param client
 * @param packet
 * @author Carina
 * @use handles the packet if a drawn card was send
 *//*

  public void logoutPacket(@NotNull ClientThread clientConnection){
   //TODO: hier clientConnection.sendPacket(new Packet(new JSONObject().put("type", Packets.LOGOUT.getPacketType())));
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



