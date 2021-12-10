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
