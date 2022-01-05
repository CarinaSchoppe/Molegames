/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "NodeType" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

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

@Getter
@Setter
public class Node extends Circle {
  public final static int DEFAULT_RADIUS = 20;
  private final int nodeId;
  private final int row;
  private Field field;
  private int nodeRadius;

  public Node(final int id, final double x, final double y, final int radius, @NotNull final NodeType nodeType, final int row, Field field) {
    super(x, y, radius);
    this.nodeId = id;
    this.row = row;
    nodeType.styleNode(this);
    this.field = field;
  }

  public Node(final int id, final double x, final double y, @NotNull final NodeType nodeType, final int row, Field field) {
    this(id, x, y, DEFAULT_RADIUS, nodeType, row, field);
  }

  @Override
  public String toString() {
    return "(id: " + nodeId + ", x: " + this.getCenterX() + ", y: " + this.getCenterY() + ")";
  }
}
