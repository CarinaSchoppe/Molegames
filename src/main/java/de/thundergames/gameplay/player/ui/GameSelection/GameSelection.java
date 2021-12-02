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
import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.gameplay.player.ui.SelectionShared;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
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

public class GameSelection extends SelectionShared implements Initializable {


    private static Client client;
    @FXML
    private Text PlayerName;
    @FXML
    private TableView<NetworkGame> gameTable;
    @FXML
    private TableColumn<NetworkGame, Integer> game_Id;
    @FXML
    private TableColumn<NetworkGame, String> game_Player_Count;
    @FXML
    private TableColumn<NetworkGame, String> game_State;

    private static GameSelection gameSelection;

    public static GameSelection getGameSelection(){
        return  gameSelection;
    }

    private ClientPacketHandler clientPacketHandler;

    public void create(ActionEvent event) throws IOException {
        createScene(event,"src/main/java/de/thundergames/gameplay/player/ui/GameSelection/GameSelection.fxml");
        gameSelection = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();
        clientPacketHandler= new ClientPacketHandler();

        //show username at scene
        PlayerName.setText("Spieler: " + client.name);

        // set value for each row
        game_Id.setCellValueFactory(new PropertyValueFactory("HashtagWithGameID"));
        game_Player_Count.setCellValueFactory(new PropertyValueFactory("CurrentPlayerCount_MaxCount"));
        game_State.setCellValueFactory(new PropertyValueFactory("status"));

        // load data for tableview
        updateTable();
    }

    /*
    Refresh the games of tableview
     */
    public void updateTable() {
        //clear tableview and get games from server and add all to table view
        gameTable.getItems().clear();
        gameTable.getItems().addAll(client.getGames());
    }

    @FXML
    void onBackClick(ActionEvent event) throws IOException {
        createScene(event,"src/main/java/de/thundergames/gameplay/player/ui/PlayerMenu/PlayerMenu.fxml");
    }

    @FXML
    void onWatchGameClick(ActionEvent event) {
        NetworkGame selectedItem = gameTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "Es wurde kein Spiel selektiert!", "Spiel beobachten", JOptionPane.OK_OPTION);
            return;
        }
        clientPacketHandler.joinGamePacket(client,selectedItem.getGameID(),false);
        //Todo:Falls game offen dann. Ansonsten direkt ins Spiel.

    }
}
