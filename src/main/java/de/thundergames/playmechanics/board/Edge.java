package de.thundergames.playmechanics.board;

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
}
