/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "MoleModel" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.board;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MoleModel extends Button {
  private static final int DEFAULT_SIZE = 48;
  private final int id;
  private final int size;

  /**
   * @param id
   * @param x
   * @param y
   * @param size
   * @param isActive
   * @use constructor
   * @author Issam, Dila, Alp
   */
  public MoleModel(
    final int id, final double x, final double y, final int size, final boolean isActive) {
    super();
    this.id = id;
    this.size = size;
  }

  /**
   * @param id
   * @param x
   * @param y
   * @param isActive
   * @author Issam, Alp, Dila
   * @use Constructor
   */
  public MoleModel(final int id, final double x, final double y, final boolean isActive) {
    this(id, x, y, DEFAULT_SIZE, isActive);
  }

  public MoleModel(final int id, final double x, final double y) {
    this(id, x, y, DEFAULT_SIZE, false);
  }

  public MoleModel(final int id, final int size) {
    this(id, 0, 0, size, false);
  }

  public MoleModel(final int id) {
    this(id, 0, 0, DEFAULT_SIZE, false);
  }

  public int getMoleId() {
    return this.id;
  }

  public int getSize() {
    return this.size;
  }

  /**
   * @param x
   * @param y
   * @author Issam, Dila, Alp
   * @use updates the position
   */
  public void updatePostion(final double x, final double y) {
    this.setLayoutX(x);
    this.setLayoutY(y);
    this.render();
  }

  /**
   * @author Issam, Dila, Alp
   * @use addeds style to the background
   */
  private void addStyles() {
    // Set size
    this.setMinSize(this.size, this.size);
    // Add sprite as a background
    var backgroundImage =
      new BackgroundImage(
        new Image(Utils.getSprite("mole/mole.png"), this.size, this.size, false, true),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT);
    this.setBackground(new Background(backgroundImage));
  }

  /**
   * @author Issam, Dila, Alp
   * @use renders the mole
   */
  public void render() {
    this.addStyles();
  }
}
