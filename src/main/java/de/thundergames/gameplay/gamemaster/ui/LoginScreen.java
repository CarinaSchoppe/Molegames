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

import de.thundergames.gameplay.gamemaster.GameMasterClient;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreen extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private TextField ip;

  @FXML private Button login;

  @FXML private TextField port;

  @FXML
  void onLogin(ActionEvent event) throws IOException {
    String ip = this.ip.getText();
    String port = this.port.getText();

    try {
      if (ip != "" && port != "") {
        System.out.println("IP: " + ip + " Port: " + port);
        Stage stage = (Stage) login.getScene().getWindow();
        GameMasterClient gamemaster = new GameMasterClient(Integer.parseInt(port), ip);
        gamemaster.create();
        /*      Stage createGame = new Stage();
        var createLocation =
            new File("src/main/java/de/thundergames/gameplay/gamemaster/ui/CreateGame.fxml")
                .toURI()
                .toURL();
        Parent createGameRot = FXMLLoader.load(createLocation);
        initialize();
        createGame.setTitle("CreateGame");
        createGame.setResizable(false);
        createGame.setScene(new Scene(createGameRot));
        createGame.show();*/
        if (gamemaster.getMasterClientThread() != null) {
          var createGame = new CreateGame();
          createGame.create(gamemaster);
          stage.close();
        }
      }
    } catch (NumberFormatException e) {

    } /*catch (IOException e) {
        e.printStackTrace();
      }*/
  }

  public void create(String... args) {
    launch(args);
  }

  @FXML
  void initialize() {
    assert ip != null : "fx:id=\"ip\" was not injected: check your FXML file 'LoginScreen.fxml'.";
    assert login != null
        : "fx:id=\"login\" was not injected: check your FXML file 'LoginScreen.fxml'.";
    assert port != null
        : "fx:id=\"port\" was not injected: check your FXML file 'LoginScreen.fxml'.";
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    location =
        new File("src/main/java/de/thundergames/gameplay/gamemaster/ui/LoginScreen.fxml")
            .toURI()
            .toURL();
    Parent root = FXMLLoader.load(location);
    initialize();
    primaryStage.setTitle("LoginScreen");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
