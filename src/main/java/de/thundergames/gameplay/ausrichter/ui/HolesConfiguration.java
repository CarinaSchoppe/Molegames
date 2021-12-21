/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 11:26 by Carina Latest changes made by Carina on 21.12.21, 11:26 All contents of "HolesConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class HolesConfiguration implements Initializable {

  private static HolesConfiguration config;

  public void start(Stage primaryStage) throws IOException {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/HolesConfiguration.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("CreateGame");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @FXML
  void initialize() {
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    config = this;
    initialize();
  }
}
