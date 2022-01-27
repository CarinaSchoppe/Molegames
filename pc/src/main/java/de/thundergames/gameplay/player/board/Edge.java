/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 19:10 by Carina Latest changes made by Carina on 17.01.22, 19:10 All contents of "Edge" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge extends Line {
  private double x1;
  private double x2;
  private double y1;
  private double y2;

  /**
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @author Alp, Dila, Issam
   * @use an edge of the game
   */
  public Edge(final double x1, final double y1, final double x2, final double y2) {
    super(x2, y2, x1, y1);
    this.getStrokeDashArray().addAll(8d);
    this.setStroke(Color.BLACK);
    this.setStrokeWidth(2);
  }

  public void highlightEdge(boolean highlight) {
    if (highlight) {
      this.setStroke(Color.YELLOW);
    } else {
      this.setStroke(Color.BLACK);
    }
  }
}
