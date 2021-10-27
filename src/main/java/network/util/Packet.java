package network.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carina
 * @usage a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private String packetString;
  private ArrayList<String> messages = new ArrayList<String>();

  /**
   * @author Carina
   * @param packetString its the String that will be send to a Server- or Clientthread
   * @useage seperate the single objects in the string with a #
   */
  public Packet(String packetString) {
    this.packetString = packetString != null ? packetString : "DISCONNECT";
    messages.addAll(List.of(packetString.split("#")));
  }

  public String getPacketString() {
    return packetString;
  }

  public ArrayList<String> getMessages() {
    return messages;
  }
}
