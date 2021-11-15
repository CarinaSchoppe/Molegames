/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.gameplay.player.ui;

import de.thundergames.networking.client.Client;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreen extends Application {

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private TextField ip;

  @FXML private Button login;

  @FXML private TextField name;

  @FXML private TextField port;

  private boolean loggedIn = false;

  @FXML
  void onLoginButtonClick(ActionEvent event) {
    String ip = this.ip.getText();
    String port = this.port.getText();
    String name = this.name.getText();
    if (ip != "" && port != "" && name != "" && !loggedIn) {
      System.out.println("IP: " + ip + " Port: " + port + " Name: " + name);
      Stage stage = (Stage) login.getScene().getWindow();
      Client client = new Client(Integer.parseInt(port), ip, name);
      loggedIn = true;
      stage.close();
      client.create();
    }
  }

  @FXML
  void initialize() {
    assert ip != null : "fx:id=\"ip\" was not injected: check your FXML file 'LoginScreen.fxml'.";
    assert login != null
        : "fx:id=\"login\" was not injected: check your FXML file 'LoginScreen.fxml'.";
    assert name != null
        : "fx:id=\"name\" was not injected: check your FXML file 'LoginScreen.fxml'.";
    assert port != null
        : "fx:id=\"port\" was not injected: check your FXML file 'LoginScreen.fxml'.";
  }

  public void create(String... args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    location =
        new File("src/main/java/de/thundergames/gameplay/player/ui/Loginscreen.fxml")
            .toURI()
            .toURL();
    Parent root = FXMLLoader.load(location);
    primaryStage.setResizable(false);
    primaryStage.setTitle("LoginScreen");
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
