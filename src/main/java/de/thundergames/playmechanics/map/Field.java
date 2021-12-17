/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "Field" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
  private transient boolean occupied;
  private transient boolean hole;
  private transient Map map;
  private transient Mole mole;
}
