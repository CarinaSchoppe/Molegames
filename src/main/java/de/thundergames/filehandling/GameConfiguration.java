package de.thundergames.filehandling;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class GameConfiguration {

  private static int id = 0;
  final File file;
  JSONObject settings;

  public GameConfiguration(@NotNull final JSONObject json) {
    this.file = new File("config" + id + ".json");
    settings = json;
    id++;
  }

  public void saveSettings(@NotNull final JSONObject settings) throws IOException {
    this.settings = settings;
    var writer = new FileWriter(this.file);
    writer.flush();
    writer.close();
  }

  public JSONObject loadConfiguration(@NotNull final File file) throws IOException {
    return new JSONObject(new String(Files.readAllBytes(file.toPath())));
  }
}
