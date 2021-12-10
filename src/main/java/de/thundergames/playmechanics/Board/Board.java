package de.thundergames.playmechanics.Board;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;



public class Board extends Group{
    private int radius;
    // TODO:: make a partial type for nodes that decouples logic from UI
    private ArrayList<Node> nodes;
    private ArrayList<NodeType> nodesType;
    private ArrayList<Edge> edges;
    private ArrayList<Player> players;
    private double width;
    private double height;


    public Board(int radius, double width, double height) {
        super();
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.players = new ArrayList<>();
        // This is probably not the best way of handling click events on node elements
        // TODO:: implement the observer pattern and notify the subscribers once a node is clicked
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeClickEventHandler);
    }

    public void setPlayers(ArrayList<Player> players ) {
        this.players = players;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }


    public void setContainerBackground(Pane container, String bgSpritePath) {
        BackgroundImage backgroundImage = new BackgroundImage( new Image( Utils.getSprite(bgSpritePath),  100, 100, false, true),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        container.setBackground(new Background(backgroundImage));
    }


    public void setNodeTypes(ArrayList<NodeType> nodeTypes) {
        this.nodesType = nodeTypes;
    }


    public List<Node> getNodeNeighbors(Node n) {
        int nodeId = n.getNodeId();
        int nodeRow = n.getRow();
        int rowOffset = nodeRow <  this.radius + 1 ? this.radius + nodeRow : 3* this.radius + 2 - nodeRow;
        int maxPossibleId = 3 * (int) Math.pow(this.radius, 2) + 3 * this.radius + 1;

        // Get list of possible neighbors
        List<Integer> possibleNeighborsIds = new ArrayList<>(List.of(nodeId - 1 , nodeId + 1, nodeId + rowOffset));

        if(nodeRow <  this.radius + 1) {
            possibleNeighborsIds.add(nodeId + rowOffset + 1);
        } else {
            possibleNeighborsIds.add( nodeId + rowOffset - 1);
        }

        List<Node> possibleNeighbors = this.nodes.stream().filter(node -> possibleNeighborsIds.contains(node.getNodeId())).collect(Collectors.toList());

        // Filter out invalid neighbors
        Function<Node, Boolean> isValidId = neighbor -> neighbor.getNodeId()  > 0 && neighbor.getNodeId()  <= maxPossibleId && neighbor.getNodeId()  > nodeId;
        Function<Node, Boolean> isNextEdge = neighbor ->  (neighbor.getNodeId()  == nodeId + 1 && neighbor.getRow() > nodeRow) || neighbor.getRow() - nodeRow > 1 ;
        Function<Node, Boolean> isAdjacentSameRow = neighbor ->  (neighbor.getNodeId() > nodeId + 1 && neighbor.getRow() == nodeRow);
        return possibleNeighbors.stream().filter(neighbor -> isValidId.apply(neighbor) && !isNextEdge.apply(neighbor) && !isAdjacentSameRow.apply(neighbor)).distinct().collect(Collectors.toList());
    }

    private Node getNodeById(int id) {
        for(Node node : nodes) {
            if(node.getNodeId() == id) {
                return node;
            }
        }
        return null;
    }


    private Point2D[] getNodesPosition(int numberOfNodes, int maxNumberOfNodes, int row) {
        // Determine margin between nodes
        double displayHeight = this.height;
        int maxAreaCoveredByNodes = maxNumberOfNodes * 15; //TODO: change constant to actual node radius
        double verticalMargin = (displayHeight - maxAreaCoveredByNodes - 100) / maxNumberOfNodes ;
        double horizentalMargin = verticalMargin / 2;

        int edgeMargins = maxNumberOfNodes - numberOfNodes;
        Point2D[] points = new Point2D[numberOfNodes];

        double widthSoFar = edgeMargins * horizentalMargin ;
        for(int i=0; i < numberOfNodes; i++) {
            points[i] = new Point2D(widthSoFar, row * verticalMargin + 50);
            widthSoFar += 2 * horizentalMargin;
        }
        return points;
    }

    public List<Integer> getOccupiedNodes() {
        return this.players.stream().map(player -> player.getOccupiedIds()).flatMap(List::stream).collect(Collectors.toList());
    }

    public void generateNodes() {
        int numberOfGridRows = this.radius * 2 + 1;
        List<Integer> occupiedNodes = getOccupiedNodes();
        int startId = 1;

        for(int i = 0; i < numberOfGridRows; i++) {
            int numberOfGridCols = i <=  this.radius ? this.radius + i + 1 : this.radius + numberOfGridRows - i;
            Point2D[] nodesPositions = getNodesPosition(numberOfGridCols, numberOfGridRows, i);

            for(int j= 0;  j < numberOfGridCols; j++) {
                this.nodes.add(new Node(startId + j, nodesPositions[j].getX() , nodesPositions[j].getY(),this.nodesType.get(startId + j), i+1, occupiedNodes.contains(startId + j)));
            }

            startId += numberOfGridCols;
        }
    }


    public void generateEdges() {
        int cnt = 0;
        for(var node: nodes) {
            List<Node> neighbors = getNodeNeighbors(node);
            for(Node neighbor: neighbors) {
                this.edges.add(new Edge(node.getCenterX(), node.getCenterY(), neighbor.getCenterX(),neighbor.getCenterY()));
            }
        }
    }

    public void generateMoles() {
        // Moles need to be set on each state mutation and should have the same id as the corresponding node
        for(Player p: this.players) {
            for(Mole mole : p.getMoles()) {
                Node correspondingNode = getNodeById(mole.getMoleId());
                mole.setLayoutX(correspondingNode.getCenterX() - mole.getSize() / 2 );
                mole.setLayoutY(correspondingNode.getCenterY() - mole.getSize() / 2);
                mole.render();
            }
        }

    }

    public void onResize(double width, double height) {
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
        this.players.forEach(player -> this.getChildren().addAll(player.getMoles()));

        // display markers
        this.players.forEach(player -> this.getChildren().add(player.getMarker()));

    }


    EventHandler<MouseEvent> nodeClickEventHandler = e -> {
        if(e.getTarget() instanceof Node) {
            this.players.forEach(player -> player.notifyNodeClick(((Node) e.getTarget())));
            e.consume();
        }
    };

}
