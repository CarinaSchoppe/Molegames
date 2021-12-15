/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 17:42 by Carina Latest changes made by Carina on 15.12.21, 17:41 All contents of "ViewManager" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui;

import de.thundergames.playmechanics.board.Utils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  Image field_hover = new Image(Utils.getSprite("game/path_field_hover.png"), 24, 24, true, true);
  ImageView hover = new ImageView(field_hover);
  Image field_default = new Image(Utils.getSprite("game/path_field.png"), 24, 24, true, true);
  ImageView field_def = new ImageView(field_default);
  Image field_pressed = new Image(Utils.getSprite("game/highlight.png"), 24, 24, true, true);
  ImageView field_pre = new ImageView(field_pressed);

  /**
   * @use the constructor
   */
  public ViewManager() {
    var borderPane = new BorderPane();
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

  /**
   * @param x
   * @param y
   * @use creates the testButtons
   */
  private void createTestButtons(final double x, final double y) { // THIS METHOD WILL BE DELETED IN THE END.
    var field = new Button("Increase Radius!");
    field.setLayoutX(x);
    field.setLayoutY(y);
    mainPane.getChildren().add(field);
    field.setOnMousePressed(event -> {
      R += 1;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
    var field1 = new Button("Reduce Radius!");
    field1.setLayoutX(x);
    field1.setLayoutY(y + 25);
    mainPane.getChildren().add(field1);
    field1.setOnMousePressed(event -> {
      R -= 1;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
    var field2 = new Button("move right");
    field2.setLayoutX(x);
    field2.setLayoutY(y + 50);
    mainPane.getChildren().add(field2);
    field2.setOnMousePressed(event -> {
      anchorPointX += 10;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
    var field3 = new Button("move left");
    field3.setLayoutX(x);
    field3.setLayoutY(y + 75);
    mainPane.getChildren().add(field3);
    field3.setOnMousePressed(event -> {
      anchorPointX -= 10;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
    var field4 = new Button("move down");
    field4.setLayoutX(x);
    field4.setLayoutY(y + 100);
    mainPane.getChildren().add(field4);
    field4.setOnMousePressed(event -> {
      anchorPointY += 10;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
    var field5 = new Button("move up");
    field5.setLayoutX(x);
    field5.setLayoutY(y + 125);
    mainPane.getChildren().add(field5);
    field5.setOnMousePressed(event -> {
      anchorPointY -= 10;
      fieldPane.getChildren().clear();
      renderFields(R);
      createFields(R);
    });
  }

  private void renderBackground(final int level) {
    //Renders the level background according to the level number.
    var ground = new Image(Utils.getSprite("background/ground.png"),
      64, 64, true, true);
    var rock = new Image(Utils.getSprite("background/ug_1.png"),
      64, 64, true, true);
    var dirt = new Image(Utils.getSprite("background/ug_2.png"),
      64, 64, true, true);
    var sand = new Image(Utils.getSprite("background/ug_3.png"),
      64, 64, true, true);
    var bed_rock = new Image(Utils.getSprite("background/final.png"),
      64, 64, true, true);
    var set_ground = new BackgroundImage(ground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
    var set_rock = new BackgroundImage(rock, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
    var set_dirt = new BackgroundImage(dirt, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
    var set_sand = new BackgroundImage(sand, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
    var set_bed_rock = new BackgroundImage(bed_rock, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
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

  private void renderFields(final int r) {
    //Renders the hexagon game board.
    var offsetX = anchorPointX;
    var offsetY = anchorPointY;
    var limit = r;
    //Load images.
    var north_west = new Image(Utils.getSprite("game/path_northwest.png"),
      128, 128, true, true);
    var north = new Image(Utils.getSprite("game/path_north.png")
      , 128, 128, true, true);
    var south_west = new Image(Utils.getSprite("game/path_southwest.png"),
      128, 128, true, true);
    var south = new Image(Utils.getSprite("game/path_south.png"),
      128, 128, true, true);
    var north_east = new Image(Utils.getSprite("game/path_northeast.png"),
      128, 128, true, true);
    var south_east = new Image(Utils.getSprite("game/path_southeast.png"),
      128, 128, true, true);
    //Loop for upper left edge
    for (var i = 0; i < r - 1; i++) {
      ImageView nw = new ImageView();
      nw.setImage(north_west);
      nw.setX(anchorPointX - 48 * i);
      nw.setY(anchorPointY + 79 * i);
      fieldPane.getChildren().add(nw);
    }
    //Loop for upper half.
    for (var j = 0; j < r - 1; j++) {
      for (var i = 0; i < limit - 2; i++) {
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
    limit = r;
    //Loop for lower left edge
    for (var i = 0; i < r - 1; i++) {
      ImageView sw = new ImageView();
      sw.setImage(south_west);
      sw.setX(anchorPointX - 48 * i);
      sw.setY(anchorPointY + (2 * (r - 1) - 1) * 79 - 79 * i);
      fieldPane.getChildren().add(sw);
    }
    //Loop for lower half.
    for (var j = 0; j < r - 1; j++) {
      for (var i = 0; i < limit - 2; i++) {
        ImageView s = new ImageView();
        s.setImage(south);
        s.setX(offsetX + 95 + 95 * i);
        s.setY(offsetY + (2 * (r - 1) - 1) * 79);
        fieldPane.getChildren().add(s);
      }
      offsetY -= 79;
      offsetX -= 48;
      limit += 1;
    }
    // Return parameters to default values for future use.
    offsetY = anchorPointY;
    offsetX = anchorPointX;
    limit = r;
    //loop for upper right edge
    for (var i = 0; i < r - 1; i++) {
      var ne = new ImageView();
      ne.setImage(north_east);
      ne.setX((r - 1) * 95 + anchorPointX + 48 * i);
      ne.setY(anchorPointY + 79 * i);
      fieldPane.getChildren().add(ne);
    }
    //Loop for lower right edge
    for (var i = 0; i < r - 1; i++) {
      var se = new ImageView();
      se.setImage(south_east);
      se.setX(anchorPointX + (r - 1) * 95 + 48 * i);
      se.setY(anchorPointY + (2 * (r - 1) - 1) * 79 - 79 * i);
      fieldPane.getChildren().add(se);
    }
  }

  private void createFields(int r) {
    // Creates interactive fields.
    var offsetY = anchorPointY;
    var offsetX = anchorPointX;
    var limit = r;
    for (var j = 0; j < r; j++) {
      for (var i = 0; i < limit; i++) {
        Button field = new Button();
        field.setLayoutX(offsetX + 44 + 95.5 * i);
        field.setLayoutY(offsetY + 16);
        field.setGraphic(field_def);
        field.setBackground(null);
        field.setOnMouseEntered(event -> field.setGraphic(hover));
        field.setOnMouseExited(event -> field.setGraphic(field_def));
        field.setOnMousePressed(event -> field.setGraphic(field_pre));
        fieldPane.getChildren().add(field);
      }
      offsetY += 79;
      offsetX -= 48;
      limit += 1;
    }
    limit = r;
    offsetX += 48 * 2; // shift two half fields right
    // Loop for lower half
    for (var j = 0; j < r - 1; j++) {// y axis loop
      for (var i = 0; i < limit + r - 2; i++) { // x axis loop. Intuitive formula.
        var field = new Button();
        field.setLayoutX(offsetX + 44 + 95.5 * i);
        field.setLayoutY(offsetY + 16);
        field.setGraphic(field_def);
        field.setBackground(null);
        field.setOnMouseEntered(event -> field.setGraphic(hover));
        field.setOnMouseExited(event -> field.setGraphic(field_def));
        field.setOnMousePressed(event -> field.setGraphic(field_pre));
        fieldPane.getChildren().add(field);
      }
      offsetY += 79;
      offsetX += 48;
      limit -= 1;
    }
  }

  private void createMole() {
    var mole_red = new Image(Utils.getSprite("mole/mole.png"), 64, 64, true, true);
    var mole_r = new ImageView(mole_red);
    var mole = new Button();
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
}

