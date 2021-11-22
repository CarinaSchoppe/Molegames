/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 21.11.21, 18:21 All contents of "CreateGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.GameMasterClient;
import de.thundergames.networking.server.Server;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class CreateGame extends Application {

  private final ArrayList<Integer> drawCardValuesList = new ArrayList<>();
  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button addItem;
  @FXML
  private Button back;
  @FXML
  private Button configureFloors;
  @FXML
  private Button configureMap;
  @FXML
  private Button createGame;
  @FXML
  private TextField drawCardValue;
  @FXML
  private Button loadConfig;
  @FXML
  private TextField molesAmount;
  @FXML
  private TextField playerAmount;
  @FXML
  private ChoiceBox<String> punishment;
  @FXML
  private TextField radius;
  @FXML
  private Button removeAll;
  @FXML
  private TextArea drawCardValues;
  @FXML
  private TextField thinkTime;
  @FXML
  private CheckBox pullDiscsOrdered;
  @FXML
  private TextField visualEffects;

  @FXML
  void addItemButtonEvent(ActionEvent event) {
    if (!drawCardValue.getText().isEmpty() || !drawCardValue.getText().equals("")) {
      drawCardValuesList.add(Integer.valueOf(drawCardValue.getText()));
    }
    if (drawCardValues.getText().equals("") || drawCardValues.getText().equals(null)) {
      drawCardValues.setText(drawCardValue.getText());
    } else {
      drawCardValues.setText(drawCardValues.getText() + "\n" + drawCardValue.getText());
    }
    drawCardValue.clear();
  }

  @FXML
  void backButtonEvent(ActionEvent event) {
  }

  @FXML
  void configureFloorsButtonEvent(ActionEvent event) {
  }

  @FXML
  void configureMapButtonEvent(ActionEvent event) {
  }

  @FXML
  void createGameButtonEvent(ActionEvent event) {
    /*TODO: Hier

       var object = new JSONObject();
    object.put("type", Packets.CREATEGAME.getPacketType());
    var json = new JSONObject();
    json.put("gameID", MoleGames.getMoleGames().getGameMasterClient().getGameID());
    object.put("value", json.toString());
    var packet = new Packet(object);
    if (GameMasterClient.getClientInstance() != null) {
      GameMasterClient.getClientInstance().getMasterClientThread().sendPacket(packet);
    }
    object.put("type", Packets.CONFIGURATION.getPacketType());
    json.put("gameID", MoleGames.getMoleGames().getGameMasterClient().getGameID());
    if (!playerAmount.getText().isEmpty()) {
      json.put("maxPlayers", Integer.parseInt(playerAmount.getText()));
    }
    if (!molesAmount.getText().isEmpty()) {
      json.put("numberOfMoles", Integer.parseInt(molesAmount.getText()));
    }
    if (!thinkTime.getText().isEmpty()) {
      json.put("thinkTime", Integer.parseInt(thinkTime.getText()));
    }
    if (!drawCardValuesList.isEmpty()) {
      json.put("cards", drawCardValuesList);
    }
    if (!visualEffects.getText().isEmpty()) {
      json.put("visualEffects", Integer.parseInt(visualEffects.getText()));
    }
    if (!radius.getText().isEmpty()) {
      json.put("radius", Integer.parseInt(radius.getText()));
    }
    if (punishment.getValue() != null) {
      json.put("movePenalty", Boolean.parseBoolean(punishment.getValue()));
    }
    json.put("pullDiscsOrdered", pullDiscsOrdered.isSelected());
    object.put("value", json.toString());
    GameMasterClient.getClientInstance().getMasterClientThread().sendPacket(new Packet(object));
    MoleGames.getMoleGames()
        .getGameMasterClient()
        .setSystemGameID(MoleGames.getMoleGames().getGameMasterClient().getGameID() + 1);

    clearAllComponents();

    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    MoleGames.getMoleGames()
        .getGameMasterClient().getMasterClientThread().sendPacket(new Packet(new JSONObject().put("type", Packets.GAMESTART.getPacketType()).put("value", new JSONObject().put("gameID", MoleGames.getMoleGames().getGameMasterClient().getGameID() - 1).toString())));
*/
  }

  private void clearAllComponents() {
    drawCardValuesList.clear();
    drawCardValues.clear();
    drawCardValue.clear();
    playerAmount.clear();
    molesAmount.clear();
    thinkTime.clear();
    visualEffects.clear();
    radius.clear();
  }

  @FXML
  void loadConfigButtonEvent(ActionEvent event) {
  }

  @FXML
  void removeAllButtonEvent(ActionEvent event) {
    drawCardValues.clear();
    drawCardValuesList.clear();
  }

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
    assert drawCardValues != null
        : "fx:id=\"drawCardValues\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert thinkTime != null
        : "fx:id=\"thinkTime\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert visualEffects != null
        : "fx:id=\"visualEffects\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert pullDiscsOrdered != null
        : "fx:id=\"pullDiscsOrdered\" was not injected: check your FXML file 'CreateGame.fxml'.";
  }

  public void create(@NotNull final Server server, @NotNull final String... args) {
    MoleGames.getMoleGames()
        .setGameMasterClient(new GameMasterClient(server));
    MoleGames.getMoleGames().getGameMasterClient().test();
    launch(args);

  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    location =
        new File("src/main/java/de/thundergames/gameplay/ausrichter/ui/CreateGame.fxml")
            .toURI()
            .toURL();
    Parent root = FXMLLoader.load(location);
    initialize();
    primaryStage.setTitle("CreateGame");
    primaryStage.setResizable(true);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
