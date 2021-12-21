/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 14:35 by Carina Latest changes made by Carina on 21.12.21, 14:35 All contents of "CreateGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.ui.floorui.DrawAgainConfiguration;
import de.thundergames.gameplay.ausrichter.ui.floorui.Floor;
import de.thundergames.gameplay.ausrichter.ui.floorui.HolesConfiguration;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Punishments;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

@Getter
public class CreateGame extends Application implements Initializable {

  private static final ArrayList<Integer> drawCardValuesList = new ArrayList<>();
  //Liste von Floor welche DrawAgain<Field> und Hole<Field> enthält
  private static final ArrayList<Floor> floors = new ArrayList<>();
  private static CreateGame createGameInstance;
  private static String molesAmountPrev;
  private static String playerAmountPrev;
  private static String punishmentPrev;
  private static String radiusPrev;
  private static String thinkTimePrev;
  private static boolean pullDiscsOrderedPrev;
  private static String visualEffectsPrev;
  @FXML
  private Button configureDrawAgain;
  @FXML
  private Button configureHoles;
  private Map map;
  @FXML
  private ChoiceBox<Punishments> movePenalty;
  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button addItem;
  @FXML
  private Button back;
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

  public static CreateGame getCreateGameInstance() {
    return createGameInstance;
  }

  public static ArrayList<Floor> getFloors() {
    return floors;
  }

  @FXML
  void addItemButtonEvent(ActionEvent event) {
    if (drawCardValue.getText() != null && !"".equalsIgnoreCase(drawCardValue.getText())) {
      drawCardValuesList.add(Integer.valueOf(drawCardValue.getText()));
    }
    if (drawCardValues.getText() == null || "".equalsIgnoreCase(drawCardValues.getText())) {
      drawCardValues.setText(drawCardValue.getText());
    } else {
      drawCardValues.setText(drawCardValues.getText() + "\n" + drawCardValue.getText());
    }
    drawCardValue.clear();
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
  void removeAllButtonEvent(ActionEvent event) {
    drawCardValues.clear();
    drawCardValuesList.clear();
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/CreateGame.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("CreateGame");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    loadPrevSettings();
  }

  @FXML
  void backButtonEvent(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    punishmentPrev = null;
    playerAmountPrev = null;
    molesAmountPrev = null;
    thinkTimePrev = null;
    visualEffectsPrev = null;
    radiusPrev = null;
    drawCardValuesList.clear();
    MoleGames.getMoleGames().getGui().start(primaryStage);
    MoleGames.getMoleGames().getGui().updateTable();
  }

  @FXML
  void createGameButtonEvent(ActionEvent event) throws Exception {
    var drawAgains = new HashSet<Field>();
    var holes = new HashSet<Field>();
    var floorMap = new ArrayList<Map>();
    for (var floor : floors) {
      for (var fields : floor.getDrawAgain()) {
        var field = new Field(fields.getXPosition(), fields.getYPosition());
        drawAgains.add(field);
      }
      for (var fields : floor.getHoles()) {
        var field = new Field(fields.getXPosition(), fields.getYPosition());
        holes.add(field);
      }
      var newFloor = new Map(drawAgains, holes, floor.getPoints());
      floorMap.add(newFloor);
    }
    if (drawAgains.isEmpty() || holes.isEmpty() || floors.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Du musst erst das Spiel voll konfigurieren!", "Fehler!", JOptionPane.ERROR_MESSAGE);
      return;
    }
    var id = MoleGames.getMoleGames().getGameHandler().getGames().size();
    if (!isLegalConfiguration()) {
      JOptionPane.showMessageDialog(null, "Das Spiel ist nicht richtig konfiguriert!", "Fehler!", JOptionPane.ERROR_MESSAGE);
      return;
    }
    MoleGames.getMoleGames().getGameHandler().createNewGame(id);
    var game = MoleGames.getMoleGames().getGameHandler().getIDGames().get(id);
    game.getSettings().getFloors().addAll(floorMap);
    game.getSettings().setMaxPlayers(playerAmount.getText() != null ? Integer.valueOf(playerAmount.getText()) : 4);
    game.getSettings().setRadius(radius.getText() != null ? Integer.valueOf(radius.getText()) : 8);
    game.getSettings().setNumberOfMoles(molesAmount.getText() != null ? Integer.valueOf(molesAmount.getText()) : 4);
    game.getSettings().setPullDiscsOrdered(pullDiscsOrdered.isSelected());
    game.getSettings().setTurnTime(thinkTime.getText() != null ? Integer.valueOf(thinkTime.getText()) * 1000 : 15000);
    if (!drawCardValuesList.isEmpty()) {
      game.getSettings().getPullDiscs().clear();
      game.getSettings().getPullDiscs().addAll(drawCardValuesList);
    }
    game.getSettings().setVisualizationTime(visualEffects.getText() != null ? Integer.valueOf(visualEffects.getText()) * 1000 : 5000);
    game.getSettings().setMovePenalty(movePenalty.getSelectionModel().getSelectedItem() != null ? movePenalty.getSelectionModel().getSelectedItem().getName() : Punishments.NOTHING.getName());
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).updateGameState();
    floors.clear();
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    MoleGames.getMoleGames().getGui().start(primaryStage);
  }

  @FXML
  void loadConfigButtonEvent(ActionEvent event) {
    //TODO: load config from file
  }

  @FXML
  void onConfigureDrawAgain(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    savePrevSettings();
    new DrawAgainConfiguration().start(primaryStage);
  }

  @FXML
  void onConfigureHoles(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    savePrevSettings();
    new HolesConfiguration().start(primaryStage);
  }

  /**
   * @author Carina
   * @use checks if a configuration was legal or not
   */
  private boolean isLegalConfiguration() {
    //TODO: implement
    return true;
  }

  public void loadPrevSettings() {
    molesAmount.setText(molesAmountPrev);
    playerAmount.setText(playerAmountPrev);
    radius.setText(radiusPrev);
    thinkTime.setText(thinkTimePrev);
    visualEffects.setText(visualEffectsPrev);
    pullDiscsOrdered.setSelected(pullDiscsOrderedPrev);
    for (var value : drawCardValuesList) {
      if (drawCardValues.getText() == null || "".equalsIgnoreCase(drawCardValues.getText())) {
        drawCardValues.setText(value.toString());
      } else {
        drawCardValues.setText(drawCardValues.getText() + "\n" + value.toString());
      }
    }
    if (punishmentPrev != null)
      movePenalty.setValue(Punishments.valueOf(punishmentPrev));
  }

  private void savePrevSettings() {
    molesAmountPrev = molesAmount.getText();
    playerAmountPrev = playerAmount.getText();
    radiusPrev = radius.getText();
    thinkTimePrev = thinkTime.getText();
    visualEffectsPrev = visualEffects.getText();
    pullDiscsOrderedPrev = pullDiscsOrdered.isSelected();
    try {
      punishmentPrev = movePenalty.getSelectionModel().getSelectedItem().toString();
    } catch (Exception e) {
    }
  }

  @FXML
  void initialize() {
    assert addItem != null : "fx:id=\"addItem\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert configureDrawAgain != null : "fx:id=\"configureDrawAgain\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert configureHoles != null : "fx:id=\"configureHoles\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert createGame != null : "fx:id=\"createGame\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert drawCardValue != null : "fx:id=\"drawCardValue\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert drawCardValues != null : "fx:id=\"drawCardValues\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert loadConfig != null : "fx:id=\"loadConfig\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert molesAmount != null : "fx:id=\"molesAmount\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert movePenalty != null : "fx:id=\"movePenalty\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert playerAmount != null : "fx:id=\"playerAmount\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert pullDiscsOrdered != null : "fx:id=\"pullDiscsOrdered\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert radius != null : "fx:id=\"radius\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert removeAll != null : "fx:id=\"removeAll\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert thinkTime != null : "fx:id=\"thinkTime\" was not injected: check your FXML file 'CreateGame.fxml'.";
    assert visualEffects != null : "fx:id=\"visualEffects\" was not injected: check your FXML file 'CreateGame.fxml'.";
    movePenalty.getItems().addAll(Punishments.values());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    createGameInstance = this;
    initialize();
  }
}
