package de.thundergames.playmechanics.Board;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Marker extends Rectangle {
    private int DEFAULT_SIZE = 16;

    public Marker() {
        super();
        this.addStyles();
    }

    public void addStyles() {
        this.setWidth(DEFAULT_SIZE);
        this.setHeight(DEFAULT_SIZE);
        Image marker = new Image(Utils.getSprite("game/marker.png"));
        this.setFill(new ImagePattern(marker));
    }
}
