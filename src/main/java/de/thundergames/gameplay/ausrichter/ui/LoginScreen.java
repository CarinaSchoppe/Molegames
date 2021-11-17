/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:43
 * All contents of "LoginScreen" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.GameMasterClient;
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

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private TextField ip;
  @FXML
  private Button login;
  @FXML
  private TextField port;

  public static void main(String[] args) {
    launch(args);
  }

  @FXML
  void onLogin(ActionEvent event) throws IOException {
    String ip = this.ip.getText();
    String port = this.port.getText();

    try {
      if (ip != "" && port != "") {
        Stage stage = (Stage) login.getScene().getWindow();
        MoleGames.getMoleGames()
            .setGameMasterClient(new GameMasterClient(Integer.parseInt(port), ip));
        MoleGames.getMoleGames().getGameMasterClient().create();
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
        if (MoleGames.getMoleGames().getGameMasterClient().getMasterClientThread() != null) {
          var createGame = new CreateGame();
          createGame.create(MoleGames.getMoleGames().getGameMasterClient());
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
        new File("src/main/java/de/thundergames/gameplay/ausrichter/ui/LoginScreen.fxml")
            .toURI()
            .toURL();
    Parent root = FXMLLoader.load(location);
    initialize();
    primaryStage.setTitle("LoginScreen");
    primaryStage.setResizable(true);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
