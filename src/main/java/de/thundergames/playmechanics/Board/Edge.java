package de.thundergames.playmechanics.Board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge extends Line{
    private double x1;
    private double x2;
    private double y1;
    private double y2;

    public Edge(double x1, double y1, double x2, double y2) {
        super(x2, y2, x1, y1);
        this.getStrokeDashArray().addAll(8d);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
    }


}
