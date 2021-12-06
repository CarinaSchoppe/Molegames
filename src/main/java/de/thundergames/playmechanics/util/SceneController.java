package de.thundergames.playmechanics.util;

import javafx.fxml.FXMLLoader;

import java.io.File;
import java.net.MalformedURLException;

public class SceneController {

  public static FXMLLoader loadFXML(String path) throws MalformedURLException {
    return new FXMLLoader(new File("src/main/resources/" + path).toURI().toURL());
  }
}
