/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 17:42 by Carina Latest changes made by Carina on 15.12.21, 17:41 All contents of "TestGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class TestGame extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    new FXMLLoader();
    try {
      var manager = new ViewManager();
      primaryStage = manager.getMainStage();
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
