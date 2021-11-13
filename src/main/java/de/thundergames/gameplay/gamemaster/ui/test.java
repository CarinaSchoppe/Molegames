/*
 *
 *  *     / **
 *  *      *   Copyright Notice                                             *
 *  *      *   Copyright (c) SwtPra10 | ThunderGames 2021                         *
 *  *      *   Created: 05.05.2018 / 11:59                                  *
 *  *      *   All contents of this source text are protected by copyright. *
 *  *      *   The copyright law, unless expressly indicated otherwise, is  *
 *  *      *   at SwtPra10 | ThunderGames. All rights reserved                    *
 *  *      *   Any type of duplication, distribution, rental, sale, award,  *
 *  *      *   Public accessibility or other use                            *
 *  *      *   Requires the express written consent of SwtPra10 | ThunderGames.   *
 *  *      **
 *  *
 */

package de.thundergames.gameplay.gamemaster.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class test extends Application {

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private Button StartGame;

  @FXML private Button ViewGame;

  @FXML private Button startButton;

  public static void main(String[] args) {
    launch(args);
  }

  @FXML
  void OpenSomeThing(ActionEvent event) {

  }

  @Override
  public void start(@NotNull final Stage primaryStage) throws Exception {
    location = new File("src/main/resources/gamemaster/test.fxml").toURI().toURL();
    Parent root = FXMLLoader.load(location);
    primaryStage.setTitle("test");
    primaryStage.setScene(new Scene(root, 900, 600));
    primaryStage.show();
  }

  public void do2(ActionEvent actionEvent) {
    System.out.println("do5");
  }

  public void do3(ActionEvent actionEvent) {
    System.out.println("do3");
  }
}
