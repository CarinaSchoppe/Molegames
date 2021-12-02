package de.thundergames.gameplay.player.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SelectionShared {

    protected void createScene(ActionEvent event,String scenePath) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File(scenePath)
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("Maulwurf Company");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
