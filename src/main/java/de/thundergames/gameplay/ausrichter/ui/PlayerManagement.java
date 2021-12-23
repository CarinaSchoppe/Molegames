/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.12.21, 16:55 by Carina Latest changes made by Carina on 23.12.21, 16:55 All contents of "PlayerManagement" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Carina, Eva, Jana
 */
@Getter
public class PlayerManagement implements Initializable {

  private static PlayerManagement playerManagement;
  private final Game game;
  @FXML
  private ResourceBundle resources;
  @FXML
  private CheckBox spectator;
  @FXML
  private URL location;
  @FXML
  private Button add;
  @FXML
  private TableView<ServerThread> aviablePlayersTable;
  @FXML
  private TableView<Player> playerTable;
  @FXML
  private Button back;
  @FXML
  private TableColumn<Player, String> playerName;
  @FXML
  private TableColumn<ServerThread, String> playerNameSelection;
  @FXML
  private TableColumn<Player, String> playerNumber;
  @FXML
  private TableColumn<ServerThread, String> playerNumberSelection;
  @FXML
  private TableColumn<Player, String> playerStatus;
  @FXML
  private Button remove;

  public PlayerManagement(@NotNull final Game game) {
    this.game = game;
  }

  public static PlayerManagement getPlayerManagement() {
    return playerManagement;
  }

  @FXML
  void onAdd(ActionEvent event) throws NotAllowedError {
    var selectedPlayer = aviablePlayersTable.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      if (selectedPlayer.getPlayer().getGame() != null) {
        JOptionPane.showMessageDialog(null, "Der Spieler ist bereits in einem Spiel!", "Fehler!", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (game.getPlayers().size() + 1 <= game.getSettings().getMaxPlayers()) {
        var object = new JsonObject();
        var json = new JsonObject();
        json.addProperty("gameID", game.getGameID());
        json.addProperty("participant", !spectator.isSelected());
        object.addProperty("type", Packets.JOINGAME.getPacketType());
        object.add("value", json);
        var packet = new Packet(object);
        if (MoleGames.getMoleGames().getServer().getPacketHandler().handleJoinPacket(selectedPlayer, packet)) {
          MoleGames.getMoleGames().getServer().getPacketHandler().welcomeGamePacket(selectedPlayer);
          updateTable();
        } else {
          JOptionPane.showMessageDialog(null, "Du musst einen Spieler auswählen!", "Auswählen!", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(null, "Das Spiel ist bereits voll!", "Fehler!", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(null, "Du musst einen Spieler auswählen!", "Auswählen!", JOptionPane.ERROR_MESSAGE);
    }
  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    MoleGames.getMoleGames().getGui().start(primaryStage);
    MoleGames.getMoleGames().getGui().updateTable();
  }

  @FXML
  void onRemove(ActionEvent event) {
    var selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      if (selectedPlayer.getGame() == null) {
        JOptionPane.showMessageDialog(null, "Der Spieler ist nicht in einem Spiel!", "Fehler!", JOptionPane.ERROR_MESSAGE);
        return;
      }
      MoleGames.getMoleGames().getServer().getPacketHandler().handlePlayerLeavePacket((ServerThread) selectedPlayer.getServerClient());
      updateTable();
    } else {
      JOptionPane.showMessageDialog(null, "Du musst einen Spieler auswählen!", "Auswählen!", JOptionPane.ERROR_MESSAGE);
    }
  }

  @FXML
  void initialize() {
    assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert aviablePlayersTable != null : "fx:id=\"aviablePlayersTable\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerName != null : "fx:id=\"playerName\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerNameSelection != null : "fx:id=\"playerNameSelection\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerNumber != null : "fx:id=\"playerNumber\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerNumberSelection != null : "fx:id=\"playerNumberSelection\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerStatus != null : "fx:id=\"playerStatus\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert playerTable != null : "fx:id=\"playerTable\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert remove != null : "fx:id=\"remove\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    assert spectator != null : "fx:id=\"spectator\" was not injected: check your FXML file 'PlayerManagement.fxml'.";
    playerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
    playerNameSelection.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClientName()));
    playerNumber.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getClientID())));
    playerNumberSelection.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getThreadID())));
    playerStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isSpectator() ? "Spectator" : "Player"));
    updateTable();
  }

  public void updateTable() {
    aviablePlayersTable.getItems().clear();
    playerTable.getItems().clear();
    aviablePlayersTable.getItems().addAll(MoleGames.getMoleGames().getServer().getLobbyThreads());
    playerTable.getItems().addAll(this.game.getPlayers());
    playerTable.getItems().addAll(this.game.getSpectators());
  }

  public void start(@NotNull final Stage stage) throws IOException {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/PlayerManagement.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    stage.setTitle("Player Management");
    stage.setScene(new javafx.scene.Scene(root));
    initialize();
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    playerManagement = this;
  }
}
