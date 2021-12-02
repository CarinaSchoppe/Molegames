/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:49
 * All contents of "Mole" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.map.Field;

public class Mole extends NetworkMole {

  private final transient boolean inHole = false;
  private transient Field field;

  public Mole(NetworkPlayer player, Field field) {
    super(player, field);
  }

  public void setMoleField(Field field) {
    setNetworkField(field);
    this.field = field;
  }

  public boolean isInHole() {
    return inHole;
  }

  public Field getField() {
    return field;
  }
}
