package network.util;

import java.util.Base64;

/**
 * @author Carina
 * @useage a Cypher encrypt and decrpyt class that will do what it sais for the network traffic
 */
public class CypherUtil {

  /**
   * @param data the packet that was send through the network
   * @return a new Packet that will be encrypted by Base64
   * @author Carina
   */
  //TODO: Implementation
  public synchronized Packet encrypt(Packet data) {
    return new Packet(new String(Base64.getEncoder().encode(data.getPacketString().getBytes())));
  }

  public synchronized Packet decrypt(Packet data) {
    return new Packet(new String(Base64.getDecoder().decode(data.getPacketString().getBytes())));
  }

  public synchronized String decrypt(String data) {
    return new String(Base64.getDecoder().decode(data.getBytes()));
  }
}
