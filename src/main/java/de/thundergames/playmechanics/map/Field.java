/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 16:05 by Carina Latest changes made by Carina on 09.01.22, 16:05 All contents of "Field" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.map;

import de.thundergames.playmechanics.util.Mole;
import lombok.Data;

@Data
public class Field {
  private final int x;
  private final int y;
  private transient boolean drawAgainField = false;
  private transient boolean occupied = false;
  private transient boolean hole = false;
  private transient Map map;
  private transient Mole mole;
}
