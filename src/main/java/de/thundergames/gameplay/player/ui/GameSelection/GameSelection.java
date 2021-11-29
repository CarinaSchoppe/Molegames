/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 22:01 by MarcW
 * Latest changes made by MarcW on 21.11.21, 22:01
 * All contents of "PlayerWorkspace" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.GameSelection;

import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.util.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSelection implements Initializable {

    @FXML
    private static Client client;
    @FXML
    private Text PlayerName;
    @FXML
    private TableView<Object> gameTable;
    @FXML
    private TableColumn<NetworkGame, Integer> game_Id;
    @FXML
    private TableColumn<NetworkGame, String> game_Player_Count;
    @FXML
    private TableColumn<NetworkGame, String> game_State;
    @FXML
    private TableColumn<NetworkGame, String> game_Type;

    public void create(ActionEvent event) throws IOException {
        createScene(event);
    }

    private void createScene(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/java/de/thundergames/gameplay/player/ui/GameSelection/GameSelection.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("Maulwurf Company");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();

        //show username at scene
        PlayerName.setText("Spieler: " + client.name);

        // set value for each row
        game_Id.setCellValueFactory(new PropertyValueFactory("HashtagWithGameID"));
        game_Player_Count.setCellValueFactory(new PropertyValueFactory("CurrentPlayerCount_MaxCount"));
        game_State.setCellValueFactory(new PropertyValueFactory("status"));
        game_Type.setCellValueFactory(new PropertyValueFactory("gameType"));

        UpdateTable();

        //Maybe usefull for later
        //  //open a listener to observe changes of selected item of list
        //  GameList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        //      //Selected item of list
        //      GameList.getSelectionModel().getSelectedItems();
        //   });
    }

    /*
    Refresh the games of tableview
     */
    private void UpdateTable() {
        //clear tableview
        gameTable.getItems().clear();

        //get all games from server
        ObservableList<NetworkGame> gameList = FXCollections.observableArrayList(client.getGames());
        ObservableList<Tournament> tournamentList = FXCollections.observableArrayList(client.getTournaments());


        //Add all games to tableview
        gameTable.getItems().addAll(gameList);
        gameTable.getItems().addAll(tournamentList);
    }

    @FXML
    void onSignOutClick(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/java/de/thundergames/gameplay/player/ui/LoginScreen.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //Todo: client.disconnect();
    }

    @FXML
    void onUpdateClick(ActionEvent event) {
        UpdateTable();
    }

    @FXML
    void onWatchGameClick(ActionEvent event) {
        var selectedItem = gameTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "Es wurde kein Spiel selektiert!", "Spiel beobachten", JOptionPane.OK_OPTION);
            return;
        }

        //Todo:Falls game offen dann. Ansonsten direkt ins Spiel.
    }
}
