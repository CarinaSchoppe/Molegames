package de.thundergames.playmechanics.Board;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;




public class Mole extends Button {
    private final static int DEFAULT_SIZE = 48;
    private int id;
    private int size;

    public Mole(int id, double x, double y, int size, Boolean isActive ) {
        super();
        this.id = id;
        this.size = size;
    }

    public Mole(int id, double x, double y, Boolean isActive ) { this(id,x,y, DEFAULT_SIZE, isActive);}

    public Mole(int id, double x, double y) {
        this(id, x,y , DEFAULT_SIZE, false);
    }

    public Mole(int id, int size) {
        this(id, 0,0 , size, false);
    }

    public Mole(int id) {
        this(id, 0,0 , DEFAULT_SIZE, false);
    }

    public int getMoleId() {
        return this.id;
    }

    public int getSize( ) {
        return this.size;
    }

    public void updatePostion(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.render();
    }

    private void addStyles() {
        // Set size
        this.setMinSize(this.size , this.size);
        // Add sprite as a background
        BackgroundImage backgroundImage = new BackgroundImage( new Image( Utils.getSprite("mole/mole.png"), this.size, this.size, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(backgroundImage));
    }

    public void render() {
        this.addStyles();
    }



}
