/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 13.01.22, 16:58 by Carina Latest changes made by Carina on 13.01.22, 16:58 All contents of "Dialog" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

public class Dialog {
  public static void show(@NotNull final String message, @NotNull final String title, @NotNull final DialogType dialogType) {
    var alert = new Alert(dialogType.toAlertType(), message);
    alert.setTitle(title);
    alert.setHeaderText(title);
    alert.showAndWait();
  }

  public enum DialogType {
    INFO(AlertType.INFORMATION),
    WARNING(AlertType.WARNING),
    ERROR(AlertType.ERROR);

    private final AlertType alertType;

    DialogType(AlertType alertType) {
      this.alertType = alertType;
    }

    public AlertType toAlertType() {
      return alertType;
    }
  }
}
