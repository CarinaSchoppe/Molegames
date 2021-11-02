package network.client;

import network.util.Packet;
import network.util.Packets;

public class ClientPacketHandler {

  public void handlePacket(Client client, Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.GIVEID.getPacketType())) {
      //ID : 3
      int id = Integer.parseInt(packet.getPacketContent().toString());
      client.setId(id);
      System.out.println("Client ID: " + id);
      client.getClientThread().setID(id);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINEDGAME.getPacketType())) {
      System.out.println("Client joined game with id: " + packet.getPacketContent().toString());
      client.setGameID(Integer.parseInt(packet.getPacketContent().toString()));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Server sended: " + packet.getPacketContent());
    }
  }
}
