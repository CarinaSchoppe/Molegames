package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Dialog;
import de.thundergames.playmechanics.util.Player;
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
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class AddPlayer implements Initializable {

  private static AddPlayer AddPlayerInstance;

  private String Spielmodus;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button addObserver;

  @FXML
  private Button addPlayer;

  @FXML
  private TableView<ServerThread> availablePlayer;

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
  private TableView<Player> playerTable;

  @FXML
  private Button removePlayer;

  @FXML
  private Button score;

  private Game game;

  public AddPlayer(Game game) {
    this.game = game;
  }


  public static AddPlayer getAddPlayerInstance() {
    return AddPlayerInstance;
  }


  @FXML
  void onAddObserver(ActionEvent event) {

  }

  @FXML
  void onAddPlayer(ActionEvent event) throws NotAllowedError {
    var selectedPlayer = availablePlayer.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      if (selectedPlayer.getPlayer().getGame() != null) {
        Dialog.show("Der Spieler ist bereits in einem Spiel!", "Fehler!", Dialog.DialogType.ERROR);
        return;
      }
      if (game.getCurrentGameState() == GameStates.OVER) {
        Dialog.show("Das Spiel ist bereits vorbei!", "Fehler!", Dialog.DialogType.ERROR);
        return;
      } else if (game.getCurrentGameState() != GameStates.NOT_STARTED) {
        Dialog.show("Der Spieler kann keinem laufenden Spiel als Spieler beitreten nur als Beobachter!", "Fehler!", Dialog.DialogType.ERROR);
        return;
      }
      if (game.getPlayers().size() + 1 <= game.getSettings().getMaxPlayers()) {
        var object = new JsonObject();
        var json = new JsonObject();
        json.addProperty("gameID", game.getGameID());
        json.addProperty("participant", false);
        object.addProperty("type", Packets.JOINGAME.getPacketType());
        object.add("value", json);
        var packet = new Packet(object);
        if (MoleGames.getMoleGames()
                .getServer()
                .getPacketHandler()
                .handleJoinGamePacket(selectedPlayer, packet)) {
          MoleGames.getMoleGames().getServer().getPacketHandler().welcomeGamePacket(selectedPlayer);
          updateTable();
        } else {
          Dialog.show("Das hinzufügen des Spielers hat nicht geklappt.", "Fehler!", Dialog.DialogType.ERROR);
        }
      } else {
        Dialog.show("Das Spiel ist bereits voll!", "Fehler!", Dialog.DialogType.ERROR);
      }
    } else {
      Dialog.show("Du musst einen Spieler auswaehlen!", "Fehler!", Dialog.DialogType.ERROR);
    }
  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    if(Spielmodus.equalsIgnoreCase("TurnierModus")){
      TournamentEditor.getTournamentEditorInstance().start(primaryStage);
      TournamentEditor.getTournamentEditorInstance().updateTable();
    }else {
      Games.getGamesInstance().start(primaryStage);
      Games.getGamesInstance().updateTable();
    }
  }

  @FXML
  void onRemovePlayer(ActionEvent event) {
    var selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
    if (selectedPlayer != null) {
      if (selectedPlayer.getGame() == null) {
        Dialog.show("Der Spieler ist nicht in einem Spiel!", "Fehler!", Dialog.DialogType.ERROR);
        return;
      }
      if (selectedPlayer.getGame().getCurrentGameState() != GameStates.OVER) {
        MoleGames.getMoleGames().getServer().getPacketHandler().handlePlayerLeavePacket((ServerThread) selectedPlayer.getServerClient());
        Dialog.show("Der Spieler wurde aus dem Spiel entfernt!", "Erfolg beim entfernen!", Dialog.DialogType.INFO);
      }
      updateTable();
    } else {
      Dialog.show("Du musst einen Spieler auswaehlen!", "Fehler!", Dialog.DialogType.ERROR);
    }
  }

  @FXML
  void onScore(ActionEvent event) {

  }

  @FXML
  void initialize() {
    assert addObserver != null : "fx:id=\"addObserver\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert addPlayer != null : "fx:id=\"addPlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert availablePlayer != null : "fx:id=\"availablePlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerName != null : "fx:id=\"playerName\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNameSelection != null : "fx:id=\"playerNameSelection\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNumber != null : "fx:id=\"playerNumber\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNumberSelection != null : "fx:id=\"playerNumberSelection\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerStatus != null : "fx:id=\"playerStatus\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerTable != null : "fx:id=\"playerTable\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert removePlayer != null : "fx:id=\"removePlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file 'AddPlayer.fxml'.";

    playerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
    playerNameSelection.setCellValueFactory(
            data -> new SimpleStringProperty(data.getValue().getClientName()));
    playerNumber.setCellValueFactory(
            data -> new SimpleStringProperty(Integer.toString(data.getValue().getClientID())));
    playerNumberSelection.setCellValueFactory(
            data -> new SimpleStringProperty(Integer.toString(data.getValue().getThreadID())));
    playerStatus.setCellValueFactory(
            data -> new SimpleStringProperty(data.getValue().isSpectator() ? "Spectator" : "Player"));
    updateTable();
  }

  public void updateTable() {
    var availablePlayersSelection = availablePlayer.getSelectionModel().getSelectedItem();
    var playerSelection = playerTable.getSelectionModel().getSelectedItem();
    availablePlayer.getItems().clear();
    playerTable.getItems().clear();
    availablePlayer.getItems().addAll(MoleGames.getMoleGames().getServer().getLobbyThreads());
    playerTable.getItems().addAll(this.game.getPlayers());
    playerTable.getItems().addAll(this.game.getSpectators());
    playerTable.getSelectionModel().select(playerSelection);
    availablePlayer.getSelectionModel().select(availablePlayersSelection);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    AddPlayerInstance = this;
    initialize();
  }

  public void start(@NotNull final Stage primaryStage, String modus) throws Exception {
    Spielmodus = modus;
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/AddPlayer.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spieler hinzufügen");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}

