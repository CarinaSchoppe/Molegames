/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 16:25 by Carina Latest changes made by Carina on 15.12.21, 15:27 All contents of "Packet" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
@Getter
public class Packet {

  private final JsonObject jsonObject;
  private final String packetType;
  private final JsonObject values;

  public Packet(@NotNull final String packetType, @NotNull final JsonObject jsonObject) {
    this.jsonObject = jsonObject;
    this.packetType = packetType;
    this.values = jsonObject.getAsJsonObject("value");
  }

  public Packet(@NotNull final JsonObject jsonObject) {
    this.jsonObject = jsonObject;
    this.packetType = jsonObject.get("type").getAsString();
    this.values = jsonObject.getAsJsonObject("value");
  }

}
