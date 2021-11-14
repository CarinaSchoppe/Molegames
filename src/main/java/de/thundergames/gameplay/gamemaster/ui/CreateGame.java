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
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

public class CreateGame {

  private GameMasterClient client;
  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private Button addItem;

  @FXML private Button back;

  @FXML private Button configureFloors;

  @FXML private Button configureMap;

  @FXML private Button createGame;

  @FXML private TextField drawCardValue;

  @FXML private Button loadConfig;

  @FXML private TextField molesAmount;

  @FXML private TextField playerAmount;

  @FXML private ChoiceBox<?> punishment;

  @FXML private TextField radius;

  @FXML private Button removeAll;

  @FXML private TextArea textInputArea;

  @FXML private TextField thinkTime;

  @FXML private TextField visualEffects;

  @FXML
  void addItemButtonEvent(ActionEvent event) {}

  @FXML
  void backButtonEvent(ActionEvent event) {}

  @FXML
  void configureFloorsButtonEvent(ActionEvent event) {}

  @FXML
  void configureMapButtonEvent(ActionEvent event) {}

  private JSONObject object;

  @FXML
  void createGameButtonEvent(ActionEvent event) {
    object = new JSONObject();
    object.put("type", Packets.CREATEGAME.getPacketType());
    var packet = new Packet(object);
    if (GameMasterClient.getClientInstance() != null)
      GameMasterClient.getClientInstance().getMasterClientThread().sendPacket(packet);
    object = new JSONObject();
    object.put("type", Packets.CONFIGURATION.getPacketType());
    object.put("gameID", GameMasterClient.getGameID());
    //TODO: read the inputs from the files
    GameMasterClient.getClientInstance().getMasterClientThread().sendPacket(new Packet(object));
    GameMasterClient.setSystemGameID(GameMasterClient.getGameID() + 1);
  }

  @FXML
  void loadConfigButtonEvent(ActionEvent event) {}

  @FXML
  void removeAllButtonEvent(ActionEvent event) {}

  @FXML
  void initialize() {
    assert addItem != null
        : "fx:id=\"addItem\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert back != null
        : "fx:id=\"back\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert configureFloors != null
        : "fx:id=\"configureFloors\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert configureMap != null
        : "fx:id=\"configureMap\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert createGame != null
        : "fx:id=\"createGame\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert drawCardValue != null
        : "fx:id=\"drawCardValue\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert loadConfig != null
        : "fx:id=\"loadConfig\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert molesAmount != null
        : "fx:id=\"molesAmount\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert playerAmount != null
        : "fx:id=\"playerAmount\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert punishment != null
        : "fx:id=\"punishment\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert radius != null
        : "fx:id=\"radius\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert removeAll != null
        : "fx:id=\"removeAll\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert textInputArea != null
        : "fx:id=\"textInputArea\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert thinkTime != null
        : "fx:id=\"thinkTime\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert visualEffects != null
        : "fx:id=\"visualEffects\" was not injected: check your FXML file 'CreateGame.fxml'.";
  }

  public void create(GameMasterClient client) {
    GameMasterClient.setClientInstance(client);
    System.out.println(GameMasterClient.getClientInstance());
    try {
      start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void start() throws IOException {
    Stage primaryStage = new Stage();
    location =
        new File("src/main/java/de/thundergames/gameplay/gamemaster/ui/CreateGame.fxml")
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
