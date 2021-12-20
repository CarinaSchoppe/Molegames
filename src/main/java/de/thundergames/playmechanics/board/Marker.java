/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 20.12.21, 16:43 by Carina Latest changes made by Carina on 20.12.21, 16:18 All contents of "Marker" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.board;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Marker extends Rectangle {
  private final int DEFAULT_SIZE = 16;

  /**
   * @author Issam
   * @use constructor
   */
  public Marker() {
    super();
    this.addStyles();
  }

  /**
   * @author Issam
   * @use add styles to the marker
   */
  public void addStyles() {
    this.setWidth(DEFAULT_SIZE);
    this.setHeight(DEFAULT_SIZE);
    var marker = new Image(Utils.getSprite("game/marker.png"));
    this.setFill(new ImagePattern(marker));
  }
}
