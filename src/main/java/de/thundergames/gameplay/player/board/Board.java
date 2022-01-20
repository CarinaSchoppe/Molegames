/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:01 by Carina Latest changes made by Carina on 20.01.22, 17:00 All contents of "Board" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Player;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board extends Group {
  private final int radius;
  private final HashSet<Node> nodes;
  private final HashMap<List<Field>, Edge> edges;
  private final HashMap<List<Integer>, NodeType> nodesType;
  private final ArrayList<PlayerModel> players;
  private double width;
  private double height;

  /**
   * @param radius
   * @param width
   * @param height
   * @author Issam, Alp, Dila
   * @use generate nodes and edges
   */
  public Board(final int radius, final double width, final double height, @NotNull final HashMap<List<Integer>, NodeType> nodesType, @NotNull final ArrayList<PlayerModel> players) {
    super();
    this.radius = radius;
    this.width = width;
    this.height = height;
    this.nodes = new HashSet<>();
    this.edges = new HashMap<>();
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
    var nodeID = node.getNodeID();
    var nodeRow = node.getRow();
    var rowOffset = nodeRow < this.radius + 1 ? this.radius + nodeRow : 3 * this.radius + 2 - nodeRow;
    var maxPossibleID = 3 * (int) Math.pow(this.radius, 2) + 3 * this.radius + 1;
    // Get list of possible neighbors
    var possibleNeighborsIDs = new ArrayList<>(List.of(nodeID - 1, nodeID + 1, nodeID + rowOffset));
    if (nodeRow < this.radius + 1) {
      possibleNeighborsIDs.add(nodeID + rowOffset + 1);
    } else {
      possibleNeighborsIDs.add(nodeID + rowOffset - 1);
    }
    var possibleNeighbors = this.nodes.stream().filter(n -> possibleNeighborsIDs.contains(n.getNodeID())).collect(Collectors.toList());
    // Filter out invalid neighbors
    var isValidID = (Function<Node, Boolean>) neighbor -> neighbor.getNodeID() > 0 && neighbor.getNodeID() <= maxPossibleID && neighbor.getNodeID() > nodeID;
    var isNextEdge = (Function<Node, Boolean>) neighbor -> (neighbor.getNodeID() == nodeID + 1 && neighbor.getRow() > nodeRow) || neighbor.getRow() - nodeRow > 1;
    var isAdjacentSameRow = (Function<Node, Boolean>) neighbor -> (neighbor.getNodeID() > nodeID + 1 && neighbor.getRow() == nodeRow);
    return possibleNeighbors.stream().filter(neighbor -> isValidID.apply(neighbor) && !isNextEdge.apply(neighbor) && !isAdjacentSameRow.apply(neighbor)).distinct().collect(Collectors.toList());
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
    var verticalMargin = (displayHeight - maxAreaCoveredByNodes - 100) / maxNumberOfNodes;
    var horizontalMargin = verticalMargin / 2;
    var edgeMargins = maxNumberOfNodes - numberOfNodes;
    var points = new Point2D[numberOfNodes];
    var widthSoFar = edgeMargins * horizontalMargin;
    for (var i = 0; i < numberOfNodes; i++) {
      points[i] = new Point2D(widthSoFar, row * verticalMargin + 50);
      widthSoFar += 2 * horizontalMargin;
    }
    return points;
  }

  /**
   * @author Issam, Alp, Dila
   * @use generates the nodes
   */
  public void generateNodes() {
    var numberOfGridRows = this.radius * 2 + 1;
    var startID = 1;
    for (var i = 0; i < numberOfGridRows; i++) {
      var numberOfGridCols = i <= this.radius ? this.radius + i + 1 : this.radius + numberOfGridRows - i;
      var nodesPositions = getNodesPosition(numberOfGridCols, numberOfGridRows, i);
      var shift = i > this.radius ? numberOfGridRows - numberOfGridCols : 0;
      for (var j = 0; j < numberOfGridCols; j++) {
        var nodeType = this.nodesType.get(List.of(i, j + shift)) != null
          ? this.nodesType.get(List.of(i, j + shift))
          : NodeType.DEFAULT;
        this.nodes.add(new Node(startID + j, nodesPositions[j].getX(), nodesPositions[j].getY(), nodeType, i + 1, new Field(i, j + shift)));
      }
      startID += numberOfGridCols;
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
        this.edges.put(Arrays.asList(node.getField(), neighbor.getField()), new Edge(node.getCenterX(), node.getCenterY(), neighbor.getCenterX(), neighbor.getCenterY()));
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
        var correspondingNode = getNodeByField(mole.getMole().getPosition());
        assert correspondingNode != null;
        mole.setLayoutX(correspondingNode.getCenterX() - Marker.DEFAULT_SIZE);
        mole.setLayoutY(correspondingNode.getCenterY() - Marker.DEFAULT_SIZE);
        mole.render();
      }
    }
  }

  public void onResize(final double width, final double height) {
    this.width = width;
    this.height = height;
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
    this.edges.forEach((k, v) -> this.getChildren().add(v));
    this.nodes.forEach(node -> this.getChildren().add(node));
    // display moles
    this.generateMoles();
    this.players.forEach(player -> this.getChildren().addAll(player.getMoles()));
  }

  private Node getNodeByField(final Field field) {
    for (var node : nodes) {
      if (node.getField().getX() == field.getX() && node.getField().getY() == field.getY()) {
        return node;
      }
    }
    return null;
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public PlayerModel getCurrentPlayerModel(int currentPlayerID) {
    return this.players.stream().filter(playerModel -> playerModel.getPlayer().getClientID() == currentPlayerID).findFirst().get();
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void moveMole(Field from, Field to, int currentPlayerId, int pullDisc) {
    var currentPlayerModel = getCurrentPlayerModel(currentPlayerId);
    var moleToBeMoved = currentPlayerModel.getMoles().stream().filter(_mole -> _mole.hasSameField(from)).findFirst().get();
    var nodeFrom = getNodeByField(from);
    var nodeTo = getNodeByField(to);
    assert nodeTo != null;
    assert nodeFrom != null;
    var moveDistance = new Point2D(nodeTo.getCenterX() - nodeFrom.getCenterX(), nodeTo.getCenterY() - nodeFrom.getCenterY());
    var endPosition = new Point2D(nodeTo.getCenterX() - Marker.DEFAULT_SIZE, nodeTo.getCenterY() - Marker.DEFAULT_SIZE);
    // Update mole position
    currentPlayerModel.getMoles().remove(moleToBeMoved);
    moleToBeMoved.getMole().setPosition(to);
    currentPlayerModel.getMoles().add(moleToBeMoved);
    // Find path to new node
    PathSearch pathSearch = new PathSearch(this.nodes);
    var nodePath = pathSearch.getPathBetweenWithLength(nodeFrom, nodeTo, pullDisc);
    moleToBeMoved.showMarker(true);
    // Highlight path to new node
    beforeTransition(nodePath);
    // Apply transition to mole
    PathTransition pathTransition = MoleTransition.transitionMole(moleToBeMoved, moveDistance);
    // Cleanup after transition end
    pathTransition.setOnFinished(finish -> {
      moleToBeMoved.showMarker(false);
      moleToBeMoved.setLayoutX(endPosition.getX());
      moleToBeMoved.setLayoutY(endPosition.getY());
      moleToBeMoved.setTranslateX(0);
      moleToBeMoved.setTranslateY(0);
      afterTransition(nodePath);
    });
  }

  public void placeMole(MoleModel mole) {
    var currentPlayerModel = getCurrentPlayerModel(mole.getMole().getPlayer().getClientID());
    if (!currentPlayerModel.getMoles().contains(mole)) {
      currentPlayerModel.getMoles().add(mole);
      var nodeTo = getNodeByField(mole.getMole().getPosition());
      assert nodeTo != null;
      mole.setLayoutX(nodeTo.getCenterX() - mole.getComputedMoleSize() / 2);
      mole.setLayoutY(nodeTo.getCenterY() - mole.getComputedMoleSize() / 2);
      mole.render();
      this.getChildren().add(mole);
      // Show placed mole transition
      MoleTransition.placeMole(mole, nodeTo);
    }
  }

  private void beforeTransition(List<Node> nodePath) {
    followNodePath(nodePath, true);
  }

  private void afterTransition(List<Node> nodePath) {
    followNodePath(nodePath, false);
  }

  private void followNodePath(List<Node> nodePath, boolean follow) {
    // Highlight all nodes in nodePath
    nodePath.forEach(node -> node.highlightNode(follow));
    // Highlight matching edges
    for (int i = 0; i < nodePath.size(); i++) {
      boolean hasNext = i + 1 < nodePath.size();
      if (hasNext) {
        for (var edge : this.edges.entrySet()) {
          var fieldFrom = edge.getKey().get(0);
          var fieldTo = edge.getKey().get(1);
          var isSameField = (Field.isSameField(fieldFrom, nodePath.get(i).getField()) && Field.isSameField(fieldTo, nodePath.get(i + 1).getField()))
            || (Field.isSameField(fieldFrom, nodePath.get(i + 1).getField()) && Field.isSameField(fieldTo, nodePath.get(i).getField()));
          if (isSameField) {
            edge.getValue().highlightEdge(follow);
          }
        }
      }
    }
  }

  public void removePlayer(Player player) {
    for (var p : players) {
      if (p.getPlayer().getClientID() == player.getClientID()) {
        for (var mole : p.getMoles()) {
          this.getChildren().remove(mole);
        }
        players.remove(p);
        break;
      }
    }
  }
}
