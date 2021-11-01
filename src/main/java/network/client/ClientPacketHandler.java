package network.client;

import network.util.Packet;
import network.util.Packets;

public class ClientPacketHandler {

  public void handlePacket(Client client, Packet packet) {
    if (packet.getPacketContent().split("#")[0].equalsIgnoreCase(Packets.GIVEID.getContent())) {
      //ID#3
      int id = Integer.parseInt(packet.getPacketContent().split("#")[1]);
      client.setId(id);
      client.getClientThread().setID(id);
    } else if (packet.getPacketContent().split("#")[0].equalsIgnoreCase(Packets.JOINEDGAME.getContent())) {
      client.setGameID(Integer.parseInt(packet.getPacketContent().split("#")[1]));
    }
  }
}
