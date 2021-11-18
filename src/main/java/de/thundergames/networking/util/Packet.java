/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "Packet" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private final JSONObject value;
  private final String packetType;
  private final JSONObject jsonPacket;

  /**
   * @param json is the JSONobject that will be send to the client
   * @author Carina
   * @use create a json object and add the type parameter
   */
  public Packet(@NotNull final JSONObject json) {
    this.packetType = json.getString("type");
    this.value = !json.isNull("values") ? new JSONObject(json.getString("values")) : new JSONObject();
    this.jsonPacket = json;
  }

  public JSONObject getValues() {
    return value;
  }

  public String getPacketType() {
    return packetType;
  }

  public JSONObject getJsonPacket() {
    return jsonPacket;
  }
}
