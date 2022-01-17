/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 11.01.22, 20:01 by Carina Latest changes made by Carina on 11.01.22, 19:39 All contents of "Marker" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Marker extends Rectangle{
  private boolean show;
  public static int  DEFAULT_SIZE = 16;
  public Marker() {
    this.show = false;
    this.addStyles();
  }


  /**
   * @author Issam
   * @use add styles to the marker
   */
  private void addStyles() {
    this.setWidth(0);
    this.setHeight(0);
    var marker = new Image(Utils.getSprite("game/marker.png"));
    this.setFill(new ImagePattern(marker));
    this.setLayoutX(8);
    this.setLayoutY(-16);
  }

  public void show(boolean showFlag) {
    this.show = showFlag;
    this.setWidth(showFlag ? DEFAULT_SIZE : 0);
    this.setHeight(showFlag ? DEFAULT_SIZE : 0);
  }

}
