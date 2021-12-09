package de.thundergames.playmechanics.Board;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Mole extends Button {
    private final static int DEFAULT_SIZE = 32;
    private Color color;
    private Boolean isActive;
    private double x;
    private double y;
    private int size;

    public Mole(Color color, Boolean isActive, double x, double y, int size) {
        this.color = color;
        this.isActive = isActive;
        this.x = x;
        this.y = y;
        this.size = size;
        this.setOnAction(e -> this.clickHanlder(e));
    }

    public Mole(double x, double y) {
        this(Color.BLACK, false, x, y, DEFAULT_SIZE);
    }

    private void addStyles() {
        // Set size
        this.setMaxSize(this.size + 8, this.size + 8);
        // Add sprite as a background
        BackgroundImage backgroundImage = new BackgroundImage( new Image( Utils.getSprite("/mole/mole.png"), this.size, this.size, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(backgroundImage));


    }

    private void clickHanlder(ActionEvent e) {
        // Add drop shadow if selected
        if(this.isActive) {
            DropShadow ds = new DropShadow( 20, Color.BLACK );
            this.setEffect(ds);
        } else {
            this.setEffect(null);
        }
        this.render();
        this.isActive = !this.isActive;
    }

    public void render() {
        this.addStyles();
        this.setLayoutX(this.x);
        this.setLayoutY(this.y);

        Rectangle r = new Rectangle();
        r.setHeight(20);
        r.setWidth(20);
        r.setFill(Color.BLACK);
        r.setLayoutX(20);
        r.setLayoutY(20);
    }


}
