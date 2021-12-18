/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.12.21, 16:37 by Carina Latest changes made by Carina on 18.12.21, 16:35
 * All contents of "NodeType" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.board;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

enum NodeType {
  HOLE {
    @Override
    public void styleNode(Node n) {
      var hole = new Image(Utils.getSprite("game/hole.png"));
      n.setFill(new ImagePattern(hole));
    }
  },
  DRAW_AGAIN {
    @Override
    public void styleNode(Node n) {
      var node = new Image(Utils.getSprite("game/highlight.png"));
      n.setFill(new ImagePattern(node));
    }
  },
  DEFAULT {
    @Override
    public void styleNode(Node n) {
      var node = new Image(Utils.getSprite("game/node.png"));
      n.setFill(new ImagePattern(node));
    }
  };

  public abstract void styleNode(Node n);
}

public class Node extends Circle {
  public static final int DEFAULT_RADIUS = 15;
  private final int id;
  private final int row;
  private int radius;
  private boolean isOccupied;

  public Node(
      final int id,
      final double x,
      final double y,
      final int radius,
      @NotNull final NodeType nodeType,
      final int row,
      final boolean isOccupied) {
    super(x, y, radius);
    this.id = id;
    this.row = row;
    nodeType.styleNode(this);
    this.isOccupied = isOccupied;
  }

  public Node(
      final int id,
      final double x,
      final double y,
      @NotNull final NodeType nodeType,
      final int row,
      final boolean isOccupied) {
    this(id, x, y, DEFAULT_RADIUS, nodeType, row, false);
  }

  public Node(final int id, final double x, final double y, final int radius) {
    this(id, x, y, radius, NodeType.DEFAULT, 0, false);
  }

  public Node(final int id, final double x, final double y) {
    this(id, x, y, DEFAULT_RADIUS, NodeType.DEFAULT, 0, false);
  }

  public int getNodeId() {
    return this.id;
  }

  public int getRow() {
    return this.row;
  }

  public boolean getIsOccupied() {
    return this.isOccupied;
  }

  public void setIsOccupied(boolean isOccupied) {
    this.isOccupied = isOccupied;
  }

  public boolean isNodeOccupied(int[] ocuppieNodeIds) {
    return Arrays.asList(ocuppieNodeIds).contains(this.id);
  }

  @Override
  public String toString() {
    return "(id: " + id + ", x: " + this.getCenterX() + ", y: " + this.getCenterY() + ")";
  }
}
