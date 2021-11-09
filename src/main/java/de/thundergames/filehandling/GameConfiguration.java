package de.thundergames.filehandling;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class GameConfiguration {

  private static int id = 0;
  File file;
  JSONObject settings;


  public GameConfiguration(JSONObject json) {
    this.file = new File("config" + id + ".json");
    settings = json;
  }

  public void saveSettings(JSONObject settings) throws IOException {
    this.settings = settings;
    var writer = new FileWriter(this.file);
    writer.flush();
    writer.close();
  }

  public JSONObject loadConfiguration(File file) throws IOException {
    return new JSONObject(new String(Files.readAllBytes(file.toPath())));
  }
}
