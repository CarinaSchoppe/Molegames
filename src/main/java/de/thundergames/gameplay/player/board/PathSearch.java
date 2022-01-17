/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 19:10 by Carina Latest changes made by Carina on 17.01.22, 19:10 All contents of "PathSearch" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class PathSearch {
  private HashSet<Node> nodes;

  public PathSearch(HashSet<Node> nodes) {
    this.nodes = nodes;
  }

  private ArrayList<Node> findSimplePathLengthN(Node source, Node destination, int pathLength) {
    ArrayList<Node> path = new ArrayList<>();
    path.add(source);
    if (source == destination) {
      path.add(destination);
      return path;
    }
    var sourceField = source.getField();
    var destinationField = destination.getField();
    if (sourceField.getY() == destinationField.getY()) {
      var movementDirection = sourceField.getX() > destinationField.getX();
      for (int i = 1; i <= pathLength; i++) {
        var newFieldX = movementDirection ? sourceField.getX() - i : sourceField.getX() + i;
        path.add(getNodeFromField(new Field(newFieldX, sourceField.getY())));
      }
    } else if (source.getField().getX() == destinationField.getX()) {
      var movementDirection = sourceField.getY() > destinationField.getY();
      for (int i = 1; i <= pathLength; i++) {
        var newFieldY = movementDirection ? sourceField.getY() - i : sourceField.getY() + i;
        path.add(getNodeFromField(new Field(sourceField.getX(), newFieldY)));
      }
    } else {
      for (int i = 1; i <= pathLength; i++) {
        var newFieldX = sourceField.getX() > destinationField.getX() ? sourceField.getX() - i : sourceField.getX() + i;
        var newFieldY = sourceField.getY() > destinationField.getY() ? sourceField.getY() - i : sourceField.getY() + i;
        path.add(getNodeFromField(new Field(newFieldX, newFieldY)));
      }
    }
    return path;
  }

  private Node getNodeFromField(Field field) {
    return this.nodes.stream().filter(node -> node.getField().getX() == field.getX() && node.getField().getY() == field.getY()).findFirst().orElse(null);
  }

  public List<Node> getPathBetweenWithLength(Node source, Node target, int length) {
    var path = findSimplePathLengthN(source, target, length);
    return new ArrayList<>(path);
  }
}
