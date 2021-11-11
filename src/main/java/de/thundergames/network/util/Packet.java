package de.thundergames.network.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private final JSONObject jsonObject;
  private final String packetType;

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  /**
   * @param json is the JSONobject that will be send to the client
   * @author Carina
   * @use create a json object and add the type parameter
   */
  public Packet(@NotNull final JSONObject json) {
    this.packetType = json.getString("type");
    this.jsonObject = json;
  }

  public String getPacketType() {
    return packetType;
  }
}
