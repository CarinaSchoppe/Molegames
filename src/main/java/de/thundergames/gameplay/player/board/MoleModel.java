/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 10.01.22, 22:08 by Carina Latest changes made by Carina on 10.01.22, 22:08 All contents of "MoleModel" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
public class MoleModel extends Group {
  private final static int DEFAULT_SIZE = 48;
  private final int moleID;
  private final int size;
  private final String moleColor;
  private Mole mole;
  private Marker marker;

  /**
   * @param id
   * @param x
   * @param y
   * @param size
   * @param isActive
   * @use constructor
   * @author Issam, Dila, Alp
   */
  public MoleModel(final int id, final double x, final double y, final int size, final boolean isActive, @NotNull final String moleColor) {
    super();
    this.moleID = id;
    this.size = size;
    this.moleColor = moleColor;
    this.marker = new Marker();
  }

  /**
   * @param id
   * @param x
   * @param y
   * @param isActive
   * @author Issam, Alp, Dila
   * @use Constructor
   */
  public MoleModel(final int id, final double x, final double y, final boolean isActive, Mole mole) {
    this(id, x, y, DEFAULT_SIZE, isActive, "red");
  }

  public MoleModel(final int id, final double x, final double y, Mole mole) {
    this(id, x, y, DEFAULT_SIZE, false, "red");
    this.mole = mole;
  }

  public MoleModel(final int id, final int size, Mole mole) {
    this(id, 0, 0, size, false, "red");
  }

  public MoleModel(final int id, Mole mole, String moleColor) {
    this(id, 0, 0, DEFAULT_SIZE, false, moleColor);
    this.mole = mole;
  }

  public MoleModel(Mole mole, String moleColor) {
    this(0, 0, 0, DEFAULT_SIZE, false, moleColor);
    this.mole = mole;
  }

  public int getSize() {
    return this.size;
  }

  /**
   * @author Issam, Dila, Alp
   * @use adds style to the background
   */
  private void addStyles() {
    this.minWidth(this.size);
    this.minWidth(this.size);
    // Set size
    var mole = new ImageView(new Image(Utils.getSprite("mole/mole.png"), this.size - Marker.DEFAULT_SIZE, this.size - Marker.DEFAULT_SIZE, false, true));
    this.getChildren().add(mole);
    this.getChildren().add(MoleHat.getHat(this.moleColor, 8, 0, 1));
    this.getChildren().add(marker);
  }

  /**
   * @author Issam, Dila, Alp
   * @use renders the mole
   */
  public void render() {
    this.addStyles();
  }

  public boolean hasSameField(Field field) {
    return Objects.equals(this.mole.getPosition().getX(), field.getX())
      && Objects.equals(this.mole.getPosition().getY(), field.getY());
  }

  public void showMarker(boolean showFlag) {
    this.marker.show(showFlag);
  }

  public double getComputedMoleSize() {
    return (this.size - Marker.DEFAULT_SIZE) ;
  }
  @Override
  public String toString() {
    return mole.getPosition().toString();
  }
}
