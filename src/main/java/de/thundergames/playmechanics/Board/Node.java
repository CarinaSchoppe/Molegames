package de.thundergames.playmechanics.Board;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


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
            n.setFill(Color.YELLOW);
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
    private final static int DEFAULT_RADIUS = 15;
    private int id;
    private NodeType nodeType;
    private double x;
    private double y;
    private int radius;


    public Node(int id, double x, double y, int radius, NodeType nodeType ) {
        super(x, y, radius);
        this.id = id;
        nodeType.styleNode(this);
    }

    public Node(int id, double x, double y, int radius ) {
        this(id, x, y, radius, NodeType.DEFAULT);
    }

    public Node(int id, double x, double y ) {
        this(id, x, y, DEFAULT_RADIUS, NodeType.DEFAULT);
    }

    @Override
    public String toString() {
        return "(id: " + id + ", x: " + this.getCenterX() + ", y: " + this.getCenterY() + ")";
    }

}
