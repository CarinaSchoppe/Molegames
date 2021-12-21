/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 11:26 by Carina Latest changes made by Carina on 21.12.21, 11:26 All contents of "DrawAgainConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class DrawAgainConfiguration implements Initializable {
  private static DrawAgainConfiguration config;
  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button add;
  @FXML
  private TableColumn<?, ?> amountDrawAgain;
  @FXML
  private TableColumn<?, ?> amountHoles;
  @FXML
  private Button back;
  @FXML
  private TableColumn<?, ?> drawAgainNumber;
  @FXML
  private TableView<?> drawAgainTable;
  @FXML
  private TableColumn<?, ?> floorNumber;
  @FXML
  private TableView<?> floorTable;
  @FXML
  private Button remove;
  @FXML
  private TextField x;
  @FXML
  private TableColumn<?, ?> xPosition;
  @FXML
  private TextField y;
  @FXML
  private TableColumn<?, ?> yPosition;

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/DrawAgainConfiguration.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("CreateGame");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @FXML
  void onAdd(ActionEvent event) {
  }

  @FXML
  void onBack(ActionEvent event) {
  }

  @FXML
  void onRemove(ActionEvent event) {
  }

  @FXML
  void onX(ActionEvent event) {
  }

  @FXML
  void onY(ActionEvent event) {
  }

  @FXML
  void initialize() {
    assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert amountDrawAgain != null : "fx:id=\"amountDrawAgain\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert amountHoles != null : "fx:id=\"amountHoles\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert drawAgainNumber != null : "fx:id=\"drawAgainNumber\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert drawAgainTable != null : "fx:id=\"drawAgainTable\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert floorNumber != null : "fx:id=\"floorNumber\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert floorTable != null : "fx:id=\"floorTable\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert remove != null : "fx:id=\"remove\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert x != null : "fx:id=\"x\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert xPosition != null : "fx:id=\"xPosition\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert y != null : "fx:id=\"y\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
    assert yPosition != null : "fx:id=\"yPosition\" was not injected: check your FXML file 'DrawAgainConfiguration.fxml'.";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    config = this;
    initialize();
  }
}
