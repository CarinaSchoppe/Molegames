package de.thundergames.playmechanics.Board;

import javafx.scene.shape.Line;

public class Edge extends Line{
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    public Edge(int x1, int x2, int y1, int y2) {
        super(x1, x2, y1, y2);
        this.getStrokeDashArray().addAll(3d);
    }

}
