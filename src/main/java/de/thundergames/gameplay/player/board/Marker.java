/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "Marker" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */avafx.scene.shape.Rectangle;

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
