package de.thundergames.playmechanics.Board;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Arrays;


enum NodeType {
    HOLE {
        @Override
        public void styleNode(Node n) {
            Image hole = new Image(Utils.getSprite("game/hole.png"));
            n.setFill(new ImagePattern(hole));
        }
    },
    DRAW_AGAIN {
        @Override
        public void styleNode(Node n) {
            Image node = new Image(Utils.getSprite("game/highlight.png"));
            n.setFill(new ImagePattern(node));
        }
    },
    DEFAULT {
        @Override
        public void styleNode(Node n) {
            Image node = new Image(Utils.getSprite("game/node.png"));
            n.setFill(new ImagePattern(node));
        }
    };

    public abstract void styleNode(Node n);
}

public class Node extends Circle {
    public final static int DEFAULT_RADIUS = 15;
    private int id;
    private int row;
    private int radius;
    private boolean isOccupied;

    public Node(int id, double x, double y, int radius, NodeType nodeType, int row, boolean isOccupied ) {
        super(x, y, radius);
        this.id = id;
        this.row = row;
        nodeType.styleNode(this);
        this.isOccupied = isOccupied;
    }

    public Node(int id, double x, double y, NodeType nodeType, int row, boolean isOccupied ) {
        this(id, x, y, DEFAULT_RADIUS, nodeType, row, false);
    }

    public Node(int id, double x, double y, int radius ) {
        this(id, x, y, radius, NodeType.DEFAULT, 0, false);
    }

    public Node(int id, double x, double y ) {
        this(id, x, y, DEFAULT_RADIUS, NodeType.DEFAULT, 0, false);
    }

    public int getNodeId() {
        return this.id;
    }

    public int getRow() {
        return this.row;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    public boolean isNodeOccupied(int[] ocuppieNodeIds) {
        return Arrays.asList(ocuppieNodeIds).contains(this.id);
    }


    @Override
    public String toString() {
        return "(id: " + id + ", x: " + this.getCenterX() + ", y: " + this.getCenterY() + ")";
    }

}
