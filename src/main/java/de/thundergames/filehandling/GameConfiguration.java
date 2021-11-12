package de.thundergames.filehandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class GameConfiguration {

  private static int id = 0;
  private final FileWriter writer;
  private JSONObject settings;

  public GameConfiguration(@NotNull final JSONObject json) throws IOException {
    File file = new File("config" + id + ".json");
    settings = json;
    id++;
    writer = new FileWriter(file);
  }

  /**
   * @param settings the settings to save
   * @throws IOException
   * @author Carina
   * @use call the method add the settings to the json object and than pass that to create a new
   *     file that will be saved
   */
  public void saveSettings(@NotNull final JSONObject settings) throws IOException {
    this.settings = settings;
    writer.write(settings.toString());
    writer.flush();
    writer.close();
  }

  /**
   * @param file the file that should be loaded
   * @return the JsonObject that was saved in the File and needs to be converted into the Settings
   * @throws IOException
   * @autor Carina
   * @use pass in the settings file and done
   */
  public JSONObject loadConfiguration(@NotNull final File file) throws IOException {
    return new JSONObject(new String(Files.readAllBytes(file.toPath())));
  }
}
