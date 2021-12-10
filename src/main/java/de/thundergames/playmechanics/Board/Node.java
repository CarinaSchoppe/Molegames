package de.thundergames.playmechanics.Board;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;


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

    public Node(int id, double x, double y, int radius, NodeType nodeType, int row ) {
        super(x, y, radius);
        this.id = id;
        this.row = row;
        nodeType.styleNode(this);
    }

    public Node(int id, double x, double y, int radius ) {
        this(id, x, y, radius, NodeType.DEFAULT, 0);
    }

    public Node(int id, double x, double y ) {
        this(id, x, y, DEFAULT_RADIUS, NodeType.DEFAULT, 0);
    }

    public int getNodeId() {
        return this.id;
    }

    public int getRow() {
        return this.row;
    }

    @Override
    public String toString() {
        return "(id: " + id + ", x: " + this.getCenterX() + ", y: " + this.getCenterY() + ")";
    }

}
