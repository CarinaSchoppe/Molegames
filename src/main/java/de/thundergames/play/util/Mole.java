/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:22
 * All contents of "Mole" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
 */
package de.thundergames.play.util;

import de.thundergames.gameplay.player.Player;
import de.thundergames.play.map.Field;
import de.thundergames.play.map.Floors;
import org.jetbrains.annotations.NotNull;

public class Mole {

  private final int MoleID;
  private final Player player;
  private final boolean inHole = false;
  private Floors floor;
  private Field field;

  public Mole(final int moleID, @NotNull final Player player) {
    this.player = player;
    MoleID = moleID;
  }

  public int getMoleID() {
    return MoleID;
  }

  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public boolean isMoveable() {
    // TODO hier logik einbauen
    return false;
  }

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Player getPlayer() {
    return player;
  }
}
