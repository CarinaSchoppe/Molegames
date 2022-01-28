/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:11 by Carina Latest changes made by Carina on 20.01.22, 17:11 All contents of "DrawAgainConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui.floor;

import de.thundergames.gameplay.ausrichter.ui.CreateGame;
import de.thundergames.playmechanics.util.Dialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Carina
 * @use creates the DrawAgainConfiguration for the CreateGame.class
 * @see CreateGame
 * @see DrawAgain
 */
public class DrawAgainConfiguration implements Initializable {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button add;
  @FXML
  private Button addPoints;
  @FXML
  private TableColumn<Floor, String> amountDrawAgain;
  @FXML
  private TableColumn<Floor, String> amountHoles;
  @FXML
  private Button back;
  @FXML
  private TableColumn<DrawAgain, String> drawAgainNumber;
  @FXML
  private TableView<DrawAgain> drawAgainTable;
  @FXML
  private TableColumn<Floor, String> floorNumber;
  @FXML
  private TableView<Floor> floorTable;
  @FXML
  private Button remove;
  @FXML
  private TextField x;
  @FXML
  private TableColumn<DrawAgain, String> xPosition;
  @FXML
  private TextField y;
  @FXML
  private TextField z;
  @FXML
  private TableColumn<DrawAgain, String> yPosition;
  @FXML
  private TableColumn<Floor, String> points;

  private String Spielmodus;

  public void start(@NotNull final Stage primaryStage, String modus) throws Exception {
    Spielmodus = modus;
    var loader =
      new FXMLLoader(getClass().getResource("/ausrichter/style/floor/DrawAgainConfiguration.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("CreateGame");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    updateTable();
  }

  @FXML
  void onAdd(@NotNull final ActionEvent event) {
    try {
      if (!"".equalsIgnoreCase(x.getText())
        && x.getText() != null
        && !"".equalsIgnoreCase(y.getText())
        && y.getText() != null
        && floorTable.getSelectionModel().getSelectedItem() != null) {
        var floor = floorTable.getSelectionModel().getSelectedItem();
        var drawAgain =
          new DrawAgain(floor, Integer.parseInt(x.getText()), Integer.parseInt(y.getText()));
        floor.getDrawAgainFields().add(drawAgain);
        updateDragAgainTable();
        updateTable();
      } else {
        Dialog.show("Du musst eine Ebene auswählen!", "Achtung!", Dialog.DialogType.WARNING);
      }
      x.setText(null);
      y.setText(null);
    } catch (NumberFormatException exe) {
      Dialog.show("Du musst die X und Y Koordinaten eingeben!", "Achtung!", Dialog.DialogType.WARNING);
    }
  }

  /**
   * @author Eva,Jana
   */
  @FXML
  void onAddPoints(@NotNull final ActionEvent event) {
    try {
      if (
        !"".equalsIgnoreCase(z.getText())
          && z.getText() != null) {
        var floor = new Floor(Integer.parseInt(z.getText()));
        CreateGame.getFloors().add(floor);
        updateTable();
      }
      z.setText(null);
    } catch (NumberFormatException exe) {
      Dialog.show("Du musst eine Zahl eingeben!", "Achtung!", Dialog.DialogType.WARNING);
    }
  }

  @FXML
  void onBack(@NotNull final ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    CreateGame.getCreateGameInstance().start(primaryStage, Spielmodus);
  }

  @FXML
  void onRemove(@NotNull final ActionEvent event) {
    if (floorTable.getSelectionModel().getSelectedItem() != null
      && drawAgainTable.getSelectionModel().getSelectedItem() == null) {
      CreateGame.getFloors().remove(floorTable.getSelectionModel().getSelectedItem());
      updateTable();
    } else if (drawAgainTable.getSelectionModel().getSelectedItem() != null) {
      var floor = floorTable.getSelectionModel().getSelectedItem();
      floor.getDrawAgainFields().remove(drawAgainTable.getSelectionModel().getSelectedItem());
      updateTable();
      floorTable.getSelectionModel().select(null);
    } else {
      Dialog.show("Du musst eine Zeile auswählen!", "Achtung!", Dialog.DialogType.WARNING);
    }
  }


  public void updateTable() {
    floorTable.getItems().clear();
    drawAgainTable.getItems().clear();
    floorTable.getItems().addAll(CreateGame.getFloors());
  }

  public void updateDragAgainTable() {
    drawAgainTable.getItems().clear();
    var selectedItem = floorTable.getSelectionModel().getSelectedItem();
    drawAgainTable.getItems().addAll(selectedItem.getDrawAgainFields());
  }

  @FXML
  void initialize() {
    assert add != null
      : "fx:id=\"add\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert addPoints != null
      : "fx:id=\"addPoints\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert amountDrawAgain != null
      : "fx:id=\"amountDrawAgain\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert amountHoles != null
      : "fx:id=\"amountHoles\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert back != null
      : "fx:id=\"back\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert drawAgainNumber != null
      : "fx:id=\"drawAgainNumber\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert drawAgainTable != null
      : "fx:id=\"drawAgainTable\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert floorNumber != null
      : "fx:id=\"floorNumber\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert floorTable != null
      : "fx:id=\"floorTable\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert remove != null
      : "fx:id=\"remove\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert x != null
      : "fx:id=\"x\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert points != null
      : "fx:id=\"points\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert xPosition != null
      : "fx:id=\"xPosition\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert y != null
      : "fx:id=\"y\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert z != null
      : "fx:id=\"z\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert yPosition != null
      : "fx:id=\"yPosition\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    // create a listener on selecting a property for the floorTable
    floorTable
      .getSelectionModel()
      .selectedItemProperty()
      .addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            if (floorTable.getSelectionModel().getSelectedItem() != null) {
              updateDragAgainTable();
            }
          }
        });
  }

  @Override
  public void initialize(@NotNull final URL location, @NotNull final ResourceBundle resources) {
    amountDrawAgain.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().drawAgainFieldsAmountString()));
    amountHoles.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().holeAmountString()));
    floorNumber.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().floorNumberString()));
    drawAgainNumber.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().getDrawAgainValueString()));
    xPosition.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().getXPositionString()));
    yPosition.setCellValueFactory(
      data -> new SimpleStringProperty(data.getValue().getYPositionString()));
    points.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPointsString()));
    initialize();
  }
}
