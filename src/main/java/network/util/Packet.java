package network.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private String packetString;
  private final ArrayList<String> messages = new ArrayList<>();

  /**
   * @param packetString its the String that will be send to a Server- or Clientthread
   * @author Carina
   * @use seperate the single objects in the string with a #
   */
  public Packet(String packetString) {
    this.packetString = packetString != null ? packetString : "DISCONNECT";
    messages.addAll(List.of(this.packetString.split("#")));
  }

  public String getPacketString() {
    return packetString;
  }


  protected Packet(){}
  /**
   * @param data the packet that was send through the network
   * @return a new Packet that will be encrypted by Base64
   * @author Carina
   */
  //TODO: Implementation
  public static Packet encrypt(Packet data) {
    return new Packet(new String(Base64.getEncoder().encode(data.getPacketString().getBytes())));
  }

  public static Packet decrypt(Packet data) {
    return new Packet(new String(Base64.getDecoder().decode(data.getPacketString().getBytes())));
  }

  public ArrayList<String> getMessages() {
    return messages;
  }

  public static String decrypt(String data) {
    return new String(Base64.getDecoder().decode(data.getBytes()));
  }

  public synchronized Packet modifyMessage(String message) {
    this.packetString = message;
    return this;
  }

  public synchronized Packet appendMessage(String message, boolean prefix) {
    if (prefix) packetString = message + packetString;
    else packetString = packetString + message;
    return this;
  }
}
