/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 19:20 by Carina latest changes made by Carina on 06.12.21, 15:02
 * All contents of "MoleModel" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.map.Field;
import org.jetbrains.annotations.NotNull;

public class Mole extends NetworkMole {

  private transient Field field;

  public Mole(@NotNull final NetworkPlayer player, @NotNull final Field field) {
    super(player, field);
  }

  public void setMoleField(Field field) {
    setNetworkField(field);
    this.field = field;
  }

  public Field getField() {
    return field;
  }
}
