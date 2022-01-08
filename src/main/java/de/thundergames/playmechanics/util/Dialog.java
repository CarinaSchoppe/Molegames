package de.thundergames.playmechanics.util;

import de.thundergames.gameplay.player.board.Node;
import de.thundergames.gameplay.player.board.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.nio.charset.StandardCharsets;

public class Dialog {
    public enum DialogType {
        INFO(AlertType.INFORMATION),
        WARNING(AlertType.WARNING),
        ERROR(AlertType.ERROR);

        private final AlertType alertType;

        private DialogType(AlertType alertType) {
            this.alertType = alertType;
        }

        public AlertType toAlertType() {
            return alertType;
        }
    }

    public static void show(String message, String title, DialogType dialogType) {
        Alert alert = new Alert(dialogType.toAlertType(), message);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();
    }


}