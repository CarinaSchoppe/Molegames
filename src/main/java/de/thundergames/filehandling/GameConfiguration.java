package de.thundergames.filehandling;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameConfiguration {

  File file;
  String path;
  JSONObject settings = new JSONObject();

  public GameConfiguration(File file, String path) {
    this.file = file;
    this.path = path;
  }

  public void saveSettings(JSONObject settings) throws IOException {
    this.settings = settings;
    var writer = new FileWriter(this.file);
    writer.flush();
    writer.close();
  }
}
