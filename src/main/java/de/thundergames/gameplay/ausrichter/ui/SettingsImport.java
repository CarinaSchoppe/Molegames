/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "SettingsImport" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */e.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import de.thundergames.filehandling.GameConfiguration;
import de.thundergames.gameplay.ausrichter.ui.floor.Floor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Carina, Eva, Jana
 */
public class SettingsImport {

  private static JsonObject object;

  @FXML
  private ResourceBundle resources;
  @FXML
  private AnchorPane pane;
  @FXML
  private URL location;
  @FXML
  private Text displayText;
  @FXML
  private Button back;
  @FXML
  private Button load;
  @FXML
  private Button save;
  @FXML
  private ImageView window;

  /**
   * @param primaryStage
   * @throws Exception
   * @author Carina, Eva, Jana
   * @use starts the main GUI
   */
  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/ConfigLoader.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Config Laden");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

  @FXML
  void onDragDropped(@NotNull DragEvent event) throws IOException {
    var board = event.getDragboard();
    if (board.hasFiles()) {
      displayText.setText(board.getFiles().get(0).getName());
      object = new GameConfiguration().loadConfiguration(board.getFiles().get(0).getAbsoluteFile());
    }
  }

  @FXML
  void onDragOver(@NotNull DragEvent event) {
    if (event.getGestureSource() != event.getSource() && event.getDragboard().hasFiles()) {
      event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    }
    event.consume();
  }

  @FXML
  void onLoad(ActionEvent event) throws Exception {
    if (object != null) {
      CreateGame.setDrawCardValuesList(
        new Gson()
          .fromJson(
            object.get("pullDiscs"),
            new TypeToken<ArrayList<Integer>>() {
            }.getType()));
      CreateGame.setFloors(
        new Gson()
          .fromJson(
            object.get("levels"),
            new TypeToken<ArrayList<Floor>>() {
            }.getType()));
      CreateGame.setMaxPlayersPrev(
        new Gson().fromJson(object.get("maxPlayers"), String.class));
      CreateGame.setRadiusPrev(
        new Gson().fromJson(object.get("radius"), String.class));
      CreateGame.setMolesAmountPrev(
        new Gson().fromJson(object.get("numberOfMoles"), String.class));
      CreateGame.setPullDiscsOrderedPrev(
        new Gson().fromJson(object.get("pullDiscsOrdered"), Boolean.class));
      CreateGame.setThinkTimePrev(
        new Gson().fromJson(object.get("turnTime"), String.class));
      CreateGame.setVisualEffectsPrev(
        new Gson().fromJson(object.get("visualisationTime"), String.class));
      CreateGame.setPunishmentPrev(
        new Gson().fromJson(object.get("movePenalty"), String.class));
      JOptionPane.showMessageDialog(null, "Configuration geladen!");
      var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      CreateGame.getCreateGameInstance().start(primaryStage);
      object = null;
    } else {
      JOptionPane.showMessageDialog(null, "Keine Konfiguration geladen!");
    }
  }

  @FXML
  void onBack(@NotNull ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    CreateGame.getCreateGameInstance().start(primaryStage);
  }

  @FXML
  void onSave(ActionEvent event) throws IOException {
    var json = new JsonObject();
    json.addProperty(
      "maxPlayers",
      CreateGame.getMaxPlayersPrev() != null
        ? Integer.parseInt(CreateGame.getMaxPlayersPrev())
        : 5);
    json.addProperty(
      "radius",
      CreateGame.getRadiusPrev() != null ? Integer.parseInt(CreateGame.getRadiusPrev()) : 8);
    json.addProperty(
      "numberOfMoles",
      CreateGame.getMolesAmountPrev() != null
        ? Integer.parseInt(CreateGame.getMolesAmountPrev())
        : 4);
    json.add("levels", JsonParser.parseString(new Gson().toJson(CreateGame.getLevel())));
    json.addProperty("pullDiscsOrdered", CreateGame.isPullDiscsOrderedPrev());
    json.add("pullDiscs", JsonParser.parseString(new Gson().toJson(CreateGame.getDrawCardValuesList())));
    json.addProperty(
      "turnTime",
      CreateGame.getThinkTimePrev() != null
        ? Integer.parseInt(CreateGame.getThinkTimePrev())
        : 10);
    json.addProperty(
      "visualisationTime",
      CreateGame.getVisualEffectsPrev() != null
        ? Integer.parseInt(CreateGame.getVisualEffectsPrev())
        : 10);
    json.addProperty(
      "movePenalty",
      CreateGame.getPunishmentPrev() != null ? CreateGame.getPunishmentPrev() : "NOTHING");
    // TODO: grafik und system implementieren  json.addProperty("deductedPoints",
    // Integer.parseInt(CreateGame.getCreateGameInstance().getDeductedPoints()));
    new GameConfiguration().saveSettings(json.toString());
    JOptionPane.showMessageDialog(null, "Konfiguration gespeichert!");
  }

  @FXML
  void initialize() {
    assert back != null
      : "fx:id=\"back\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
    assert displayText != null
      : "fx:id=\"displayText\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
    assert load != null
      : "fx:id=\"load\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
    assert pane != null
      : "fx:id=\"pane\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
    assert save != null
      : "fx:id=\"save\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
    assert window != null
      : "fx:id=\"window\" was not injected: check your FXML file 'ConfigLoader.fxml'.";
  }
}
