/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 12.01.22, 17:30 by Carina Latest changes made by Carina on 12.01.22, 17:22 All contents of "LobbyObserverGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.gameselection;

import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.board.GameBoard;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class LobbyObserverGame implements Initializable {

  @FXML
  private static Client CLIENT;
  private static LobbyObserverGame OBSERVER;
  @FXML
  private Text PlayerName;
  @FXML
  private Text PlayerJoined;
  @FXML
  private Text JoinedSuccessfully;
  @FXML
  private TableView<SettingsTable> settingsTable;
  @FXML
  private TableColumn<SettingsTable, String> settingName;
  @FXML
  private TableColumn<SettingsTable, String> settingValue;

  @FXML
  private TableView<Player> playerTable;
  @FXML
  private TableColumn<Player, String> playerTableName;

  private Game game;

  private Integer gameID;

  private ObservableList<SettingsTable> settingsData;

  private Stage primaryStage;

  public static LobbyObserverGame getObserver() {
    return OBSERVER;
  }

  public void create(Stage event, Integer gameID) throws IOException {
    CLIENT = Client.getClientInstance();
    OBSERVER = this;
    this.gameID = gameID;
    createScene(event);
  }

  private void createScene(Stage primaryStage) throws IOException {
    this.primaryStage = primaryStage;
    var loader = SceneController.loadFXML("/player/style/LobbyObserverGame.fxml");
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Lobby Observing");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    // region Create button events
    // set event for back button
    var btnBack = (Button) (primaryStage.getScene().lookup("#backToGameSelection"));
    btnBack.setOnAction(
      e -> {
        try {
          onBackClick(e);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    // endregion
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    playerTableName.setCellValueFactory(new PropertyValueFactory<>("PlayerIDAndName"));
    settingName.setCellValueFactory(new PropertyValueFactory<>("setting"));
    settingValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    PlayerName.setText("Spieler: " + CLIENT.name);
    System.out.println("GameID: " + CLIENT.getGameID());
    for (var game : CLIENT.getGames()) {
      if (game.getGameID() == this.gameID) {
        this.game = game;
        break;
      }
    }
    settingsData = FXCollections.observableArrayList();
    updatePlayerTable();
    updateSettingsTable();
  }

  /**
   * @author Nick
   * @use processes the click on the back button, loads previous scene GameSelection and informs
   * server player has left via leaveGame Packet (method inspired by "onSignOutClick()" -> see
   * GameSelection
   */
  @FXML
  void onBackClick(ActionEvent event) throws IOException {
    CLIENT.getClientPacketHandler().leaveGamePacket();
    new GameSelection().create(event);
  }

  /**
   * Is called when the close button is clicked. Logout user.
   *
   * @param stage current stage
   */
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    CLIENT.getClientPacketHandler().leaveGamePacket();
    stage.close();
  }

  /**
   * @author Nick, Philipp
   * @use Changes the opacity of a text field with the content "Ein weiterer Spieler ist
   * beigetreten" thus making it visible for 3 seconds when another player has joined
   * respectively when the client has received playerJoined packet.
   */
  public void showNewPlayer() {
    updateNumberOfPlayers();
    updatePlayerTable();
    PlayerJoined.setOpacity(1.0);
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    PlayerJoined.setOpacity(0.0);
  }

  /**
   * @author Philipp
   * @use Refreshes the table view, if the number of players in the room changes
   */
  public void updateNumberOfPlayers() {
    settingsData.get(0).setValue(getNumberOfPlayers());
    settingsTable.refresh();
  }

  /**
   * @return Returns a string consisting of the currently connected number of players and the max player
   * count of the room
   * @author Philipp
   */
  public String getNumberOfPlayers() {
    var numberOfPlayers = CLIENT.getGameState().getActivePlayers().size();
    return numberOfPlayers + "/" + game.getMaxPlayerCount();
  }

  /**
   * @author Philipp
   * @use Writes the playing Client names to a table view to display it in the GUI
   */
  public void updatePlayerTable() {
    HashSet<Player> playerList = CLIENT.getGameState().getActivePlayers();
    ObservableList<Player> players = FXCollections.observableArrayList();
    players.clear();
    players.addAll(playerList);
    if (!players.isEmpty()) {
      playerTable.setItems(players);
    } else {
      playerTable.getItems().clear();
    }
  }

  /**
   * @author Philipp
   * @use Writes the game settings to a table view to display it in the GUI
   */
  public void updateSettingsTable() {
    settingsData.add(new SettingsTable("Spieler", getNumberOfPlayers()));
    settingsData.add(new SettingsTable("Radius", Integer.toString(this.game.getRadius())));
    settingsData.add(new SettingsTable("Maulwurfanzahl", Integer.toString(this.game.getMoleCount())));
    settingsData.add(new SettingsTable("Level", Integer.toString(this.game.getLevelCount())));
    settingsData.add(new SettingsTable("Karten geordnet", Boolean.toString(this.game.isPullDiscsOrdered())));
    settingsData.add(new SettingsTable("Karten", this.game.getPullDiscs().toString()));
    settingsData.add(new SettingsTable("Zug Zeit", Long.toString(this.game.getTurnTime())));
    settingsData.add(new SettingsTable("Visualisierungszeit", Long.toString(this.game.getVisualizationTime())));
    settingsData.add(new SettingsTable("Bestrafung", this.game.getMovePenalty()));
    settingsTable.setItems(settingsData);
  }

  /**
   * @author Nick
   * @use Changes the opacity of a text field with the content "Beitritt zum Spiel war erfolgreich!
   * Bitte warten." thus making it visible for 5 seconds when the client has received
   * AssignToGame packet.
   */
  public void showJoiningSuccessfully() {
    JoinedSuccessfully.setOpacity(1.0);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    JoinedSuccessfully.setOpacity(0.0);
  }

  /**
   * @author Nick
   * @use Create scene and spectate the game
   */
  public void spectateGame() {
    Platform.runLater(() -> new GameBoard().create(primaryStage));
  }
}
