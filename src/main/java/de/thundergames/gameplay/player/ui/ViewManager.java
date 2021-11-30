package de.thundergames.gameplay.player.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class ViewManager {
    private final Pane fieldPane;
    private final Pane mainPane;
    private final Stage mainStage;
    private final int level = 1;
    private final int LASTLEVEL = 100;
    public Scene mainScene;
    public int R = 2;
    int WIDTH = 800;
    int HEIGHT = 600;
    int anchorPointX = 150;
    int anchorPointY = 150;
    //Load field dot images.
    Image field_hover = new Image(getSprite("game/path_field_hover.png"), 24, 24, true, true);
    ImageView hover = new ImageView(field_hover);
    Image field_default = new Image(getSprite("game/path_field.png"), 24, 24, true, true);
    ImageView field_def = new ImageView(field_default);
    Image field_pressed = new Image(getSprite("game/highlight.png"), 24, 24, true, true);
    ImageView field_pre = new ImageView(field_pressed);

    public ViewManager() {
        BorderPane borderPane = new BorderPane();

        mainPane = new AnchorPane();
        fieldPane = new AnchorPane();
        mainPane.getChildren().add(fieldPane); // add fields to mainpane
        borderPane.setCenter(mainPane);


        mainScene = new Scene(borderPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);

        createTestButtons(0, 0);
        renderBackground(level);
        renderFields(R);
        createFields(R);
        createMole();

        //Screen Drag trial.


    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void incR(ActionEvent e) {
        R += 1;
    }

    public void decR(ActionEvent e) {
        R -= 1;
    }

    private void createTestButtons(double x, double y) { // THIS METHOD WILL BE DELETED IN THE END.
        Button field = new Button("Increase Radius!");
        field.setLayoutX(x);
        field.setLayoutY(y);
        mainPane.getChildren().add(field);
        field.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                R += 1;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);
            }
        });
        Button field0 = new Button("Reduce Radius!");
        field0.setLayoutX(x);
        field0.setLayoutY(y + 25);
        mainPane.getChildren().add(field0);
        field0.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                R -= 1;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);

            }
        });
        Button field2 = new Button("move right");
        field2.setLayoutX(x);
        field2.setLayoutY(y + 50);
        mainPane.getChildren().add(field2);
        field2.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorPointX += 10;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);

            }
        });

        Button field3 = new Button("move left");
        field3.setLayoutX(x);
        field3.setLayoutY(y + 75);
        mainPane.getChildren().add(field3);
        field3.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorPointX -= 10;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);

            }
        });
        Button field4 = new Button("move down");
        field4.setLayoutX(x);
        field4.setLayoutY(y + 100);
        mainPane.getChildren().add(field4);
        field4.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorPointY += 10;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);
            }
        });
        Button field5 = new Button("move up");
        field5.setLayoutX(x);
        field5.setLayoutY(y + 125);
        mainPane.getChildren().add(field5);
        field5.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorPointY -= 10;
                fieldPane.getChildren().clear();
                renderFields(R);
                createFields(R);
            }
        });
    }

    private void renderBackground(int level) {
        //Renders the level background according to the level number.

        Image ground = new Image(this.getSprite("background/ground.png"),
                64, 64, true, true);
        Image rock = new Image(this.getSprite("background/ug_1.png"),
                64, 64, true, true);
        Image dirt = new Image(this.getSprite("background/ug_2.png"),
                64, 64, true, true);
        Image sand = new Image(this.getSprite("background/ug_3.png"),
                64, 64, true, true);
        Image bed_rock = new Image(this.getSprite("background/final.png"),
                64, 64, true, true);

        BackgroundImage set_ground = new BackgroundImage(ground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        BackgroundImage set_rock = new BackgroundImage(rock, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        BackgroundImage set_dirt = new BackgroundImage(dirt, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        BackgroundImage set_sand = new BackgroundImage(sand, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        BackgroundImage set_bed_rock = new BackgroundImage(bed_rock, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);

        if (level == 1) {
            mainPane.setBackground(new Background(set_ground));
        }
        if (level != 1) {
            if (level % 3 == 1) {
                mainPane.setBackground(new Background(set_rock));
            }
            if (level % 3 == 2) {
                mainPane.setBackground(new Background(set_dirt));
            }
            if (level % 3 == 0) {
                mainPane.setBackground(new Background(set_sand));
            }
            if (level == LASTLEVEL) { // Needs to read game size.
                mainPane.setBackground(new Background(set_bed_rock));
            }
        }
    }

    private void renderFields(int R) {
        //Renders the hexagon game board.
        int offsetX = anchorPointX;
        int offsetY = anchorPointY;
        int limit = R;

        //Load images.
        Image north_west = new Image(this.getSprite("game/path_northwest.png"),
                128, 128, true, true);
        Image north = new Image(this.getSprite("game/path_north.png")
                , 128, 128, true, true);
        Image south_west = new Image(this.getSprite("game/path_southwest.png"),
                128, 128, true, true);
        Image south = new Image(this.getSprite("game/path_south.png"),
                128, 128, true, true);
        Image north_east = new Image(this.getSprite("game/path_northeast.png"),
                128, 128, true, true);
        Image south_east = new Image(this.getSprite("game/path_southeast.png"),
                128, 128, true, true);
        //Loop for upper left edge
        for (int i = 0; i < R - 1; i++) {
            ImageView nw = new ImageView();
            nw.setImage(north_west);
            nw.setX(anchorPointX - 48 * i);
            nw.setY(anchorPointY + 79 * i);
            fieldPane.getChildren().add(nw);
        }
        //Loop for upper half.
        for (int j = 0; j < R - 1; j++) {


            for (int i = 0; i < limit - 2; i++) {
                ImageView n = new ImageView();
                n.setImage(north);
                n.setX(offsetX + 95 + 95 * i);
                n.setY(offsetY);
                fieldPane.getChildren().add(n);
            }
            offsetY += 79;
            offsetX -= 48;
            limit += 1;
        }
        // Return parameters to default values.
        offsetY = anchorPointY;
        offsetX = anchorPointX;
        limit = R;
        //Loop for lower left edge
        for (int i = 0; i < R - 1; i++) {
            ImageView sw = new ImageView();
            sw.setImage(south_west);
            sw.setX(anchorPointX - 48 * i);
            sw.setY(anchorPointY + (2 * (R - 1) - 1) * 79 - 79 * i);
            fieldPane.getChildren().add(sw);
        }
        //Loop for lower half.
        for (int j = 0; j < R - 1; j++) {
            for (int i = 0; i < limit - 2; i++) {
                ImageView s = new ImageView();
                s.setImage(south);
                s.setX(offsetX + 95 + 95 * i);
                s.setY(offsetY + (2 * (R - 1) - 1) * 79);
                fieldPane.getChildren().add(s);
            }
            offsetY -= 79;
            offsetX -= 48;
            limit += 1;
        }
        // Return parameters to default values for future use.
        offsetY = anchorPointY;
        offsetX = anchorPointX;
        limit = R;
        //loop for upper right edge
        for (int i = 0; i < R - 1; i++) {
            ImageView ne = new ImageView();
            ne.setImage(north_east);
            ne.setX((R - 1) * 95 + anchorPointX + 48 * i);
            ne.setY(anchorPointY + 79 * i);
            fieldPane.getChildren().add(ne);
        }
        //Loop for lower right edge
        for (int i = 0; i < R - 1; i++) {
            ImageView se = new ImageView();
            se.setImage(south_east);
            se.setX(anchorPointX + (R - 1) * 95 + 48 * i);
            se.setY(anchorPointY + (2 * (R - 1) - 1) * 79 - 79 * i);
            fieldPane.getChildren().add(se);
        }

    }

    private void createFields(int R) {
        // Creates interactive fields.
        int offsetY = anchorPointY;
        int offsetX = anchorPointX;
        int limit = R;
        for (int j = 0; j < R; j++) {
            for (int i = 0; i < limit; i++) {
                Button field = new Button();
                field.setLayoutX(offsetX + 44 + 95.5 * i);
                field.setLayoutY(offsetY + 16);
                field.setGraphic(field_def);
                field.setBackground(null);

                field.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(hover);
                    }
                });
                field.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(field_def);
                    }
                });
                field.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(field_pre);
                    }
                });
                fieldPane.getChildren().add(field);
            }
            offsetY += 79;
            offsetX -= 48;
            limit += 1;

        }
        limit = R;
        offsetX += 48 * 2; // shift two half fields right

        // Loop for lower half
        for (int j = 0; j < R - 1; j++) {// y axis loop
            for (int i = 0; i < limit + R - 2; i++) { // x axis loop. Intuitive formula.
                Button field = new Button();
                field.setLayoutX(offsetX + 44 + 95.5 * i);
                field.setLayoutY(offsetY + 16);
                field.setGraphic(field_def);
                field.setBackground(null);

                field.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(hover);
                    }
                });
                field.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(field_def);
                    }
                });
                field.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        field.setGraphic(field_pre);
                    }
                });
                fieldPane.getChildren().add(field);
            }
            offsetY += 79;
            offsetX += 48;
            limit -= 1;

        }
    }

    private void createMole() {
        Image mole_red = new Image(getSprite("mole/mole.png"), 64, 64, true, true);
        ImageView mole_r = new ImageView(mole_red);
        Button mole = new Button();
        mole.setLayoutX(250);
        mole.setLayoutY(250);
        mole.setGraphic(mole_r);
        mole.setBackground(null);
        fieldPane.getChildren().add(mole);
        //drag n drop method, can be deleted.
        mole.setOnMouseDragged(e -> {
            mole.setLayoutX(e.getSceneX() - 30);
            mole.setLayoutY(e.getSceneY() - 30);
        });


    }


    private String getSprite(String spriteName) {
        return getClass().getResource("/sprites/" + spriteName).toString();
    }


}

