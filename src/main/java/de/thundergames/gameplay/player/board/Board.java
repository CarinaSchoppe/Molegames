/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "Board" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board extends Group {
  private final int radius;
  // TODO:: make a partial type for nodes that decouples logic from UI
  private final HashSet<Node> nodes;
  private final ArrayList<Edge> edges;
  //private final Map map;
  private HashMap<List<Integer>, NodeType> nodesType;
  private ArrayList<PlayerModel> players;
  private double width;
  private double height;

  /**
   * @param radius
   * @param width
   * @param height
   * @author Issam, Alp, Dila
   * @use generate nodes and edges
   */
  public Board(final int radius, final double width, final double height, HashMap<List<Integer>, NodeType> nodesType,ArrayList<PlayerModel> players) {
    super();
    this.radius = radius;
    this.width = width;
    this.height = height;
    this.nodes = new HashSet<>();
    this.edges = new ArrayList<>();
    this.players = players;
    this.nodesType = nodesType;
  }

  public void setContainerBackground(@NotNull final Pane container, @NotNull final String bgSpritePath) {
    var backgroundImage = new BackgroundImage(new Image(Utils.getSprite(bgSpritePath), 100, 100, false, true),
      BackgroundRepeat.REPEAT,
      BackgroundRepeat.REPEAT,
      BackgroundPosition.CENTER,
      BackgroundSize.DEFAULT);
    container.setBackground(new Background(backgroundImage));
  }

  /**
   * @param node
   * @author Issam, Alp, Dila
   */
  public List<Node> getNodeNeighbors(@NotNull final Node node) {
    var nodeId = node.getNodeId();
    var nodeRow = node.getRow();
    var rowOffset = nodeRow < this.radius + 1 ? this.radius + nodeRow : 3 * this.radius + 2 - nodeRow;
    var maxPossibleId = 3 * (int) Math.pow(this.radius, 2) + 3 * this.radius + 1;
    // Get list of possible neighbors
    var possibleNeighborsIds = new ArrayList<>(List.of(nodeId - 1, nodeId + 1, nodeId + rowOffset));
    if (nodeRow < this.radius + 1) {
      possibleNeighborsIds.add(nodeId + rowOffset + 1);
    } else {
      possibleNeighborsIds.add(nodeId + rowOffset - 1);
    }
    var possibleNeighbors = this.nodes.stream().filter(n -> possibleNeighborsIds.contains(n.getNodeId())).collect(Collectors.toList());
    // Filter out invalid neighbors
    Function<Node, Boolean> isValidId = neighbor -> neighbor.getNodeId() > 0 && neighbor.getNodeId() <= maxPossibleId && neighbor.getNodeId() > nodeId;
    Function<Node, Boolean> isNextEdge = neighbor -> (neighbor.getNodeId() == nodeId + 1 && neighbor.getRow() > nodeRow) || neighbor.getRow() - nodeRow > 1;
    Function<Node, Boolean> isAdjacentSameRow = neighbor -> (neighbor.getNodeId() > nodeId + 1 && neighbor.getRow() == nodeRow);
    return possibleNeighbors.stream().filter(neighbor -> isValidId.apply(neighbor) && !isNextEdge.apply(neighbor) && !isAdjacentSameRow.apply(neighbor)).distinct().collect(Collectors.toList());
  }

  /**
   * @param numberOfNodes
   * @param maxNumberOfNodes
   * @param row
   * @return returns the 2d Points
   * @author Issam, Alp, Dila
   */
  private Point2D[] getNodesPosition(final int numberOfNodes, final int maxNumberOfNodes, final int row) {
    // Determine margin between nodes
    var displayHeight = this.height;
    var maxAreaCoveredByNodes = maxNumberOfNodes * 15; //TODO: change constant to actual node radius
    double verticalMargin = (displayHeight - maxAreaCoveredByNodes - 100) / maxNumberOfNodes;
    double horizentalMargin = verticalMargin / 2;
    var edgeMargins = maxNumberOfNodes - numberOfNodes;
    var points = new Point2D[numberOfNodes];
    var widthSoFar = edgeMargins * horizentalMargin;
    for (var i = 0; i < numberOfNodes; i++) {
      points[i] = new Point2D(widthSoFar, row * verticalMargin + 50);
      widthSoFar += 2 * horizentalMargin;
    }
    return points;
  }

  /**
   * @author Issam, Alp, Dila
   * @use generates the nodes
   */
  public void generateNodes() {

   // var fieldMap = map.getFieldMap();
   // var maxNumberOfNodes = 2 * this.radius + 1;

  //  for (Field node: fieldMap.values()) {
  //      var row = node.getY();
  //      var numberOfNodes = (int)fieldMap.values().stream().filter( field -> field.getX() == node.getX()).count();
  //      var nodesPositions = getNodesPosition(numberOfNodes, maxNumberOfNodes, row);
//
  //      var nodeType = this.nodesType.get(List.of(node.getX(),node.getY())) != null
  //      ? this.nodesType.get(List.of(node.getX(),node.getY()))
  //      : NodeType.DEFAULT;
  //    this.nodes.add(new Node(nodesPositions[j].getX(), nodesPositions[j].getY(), nodeType, i + 1,new Field(i,j)));
  //  }



    var numberOfGridRows = this.radius * 2 + 1;
    var startId = 1;
    for (var i = 0; i < numberOfGridRows; i++) {
      var numberOfGridCols = i <= this.radius ? this.radius + i + 1 : this.radius + numberOfGridRows - i;
      var nodesPositions = getNodesPosition(numberOfGridCols, numberOfGridRows, i);
      var shift = i > this.radius ? numberOfGridRows-numberOfGridCols : 0;
      for (var j = 0; j < numberOfGridCols; j++) {
        var nodeType = this.nodesType.get(List.of(i,j)) != null
          ? this.nodesType.get(List.of(i,j))
          : NodeType.DEFAULT;

        this.nodes.add(new Node(startId + j, nodesPositions[j].getX(), nodesPositions[j].getY(), nodeType, i + 1,new Field(i,j + shift)));

      }
      startId += numberOfGridCols;
    }
  }

  /**
   * @author Issam, Alp, Dila
   * @use generates the Edges
   */
  public void generateEdges() {
    for (var node : nodes) {
      var neighbors = getNodeNeighbors(node);
      for (var neighbor : neighbors) {
        this.edges.add(new Edge(node.getCenterX(), node.getCenterY(), neighbor.getCenterX(), neighbor.getCenterY()));
      }
    }
  }

  /**
   * @author Issam, Alp, Dila
   * @use generates the moles
   */
  public void generateMoles() {
    // Moles need to be set on each state mutation and should have the same id as the corresponding node
    for (var p : this.players) {
      for (var mole : p.getMoles()) {
        var correspondingNode = getNodeByField(mole.getMole().getField());
        mole.setLayoutX(correspondingNode.getCenterX() - mole.getSize() / 2);
        mole.setLayoutY(correspondingNode.getCenterY() - mole.getSize() / 2);
        mole.render();
      }
    }
  }

  public void onResize(final double width, final double height) {
    this.width = width;
    this.height = height;
    // TODO: Debounce rendering to avoid multiple successive renders when resizing
    this.render();
  }

  private void clearBoard() {
    this.nodes.clear();
    this.edges.clear();
    this.getChildren().clear();
  }

  /**
   * @author Issam, Alp, Dila
   * @use renders the board
   */
  public void render() {
    // On each render clear nodes and edges (this is not ideal performance-wise ! )
    this.clearBoard();
    // generate edges and nodes
    this.generateNodes();
    this.generateEdges();
    // display edges and nodes
    this.edges.forEach(edge -> {
      this.getChildren().add(edge);
    });
    this.nodes.forEach(node -> {
      this.getChildren().add(node);
    });
    // display moles
    this.generateMoles();
    var test = this.players;
    this.players.forEach(player -> this.getChildren().addAll(player.getMoles()));
    // display markers
    this.players.forEach(PlayerModel::updateMarker);
    this.players.forEach(player -> this.getChildren().addAll((player.getMarkers())));
  }

  private Node getNodeByField(final Field field) {
      for (var node : nodes) {
        if (node.getField().getX() == field.getX() && node.getField().getY() == field.getY()) {
          return node;
        }
      }
    return null;
  }
}
