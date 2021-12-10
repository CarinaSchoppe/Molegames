package de.thundergames.gameplay.util;

import javafx.fxml.FXMLLoader;

public class SceneController {

  public synchronized static FXMLLoader loadFXML(String path) {
    return new FXMLLoader(SceneController.class.getResource(path));
  }
}
