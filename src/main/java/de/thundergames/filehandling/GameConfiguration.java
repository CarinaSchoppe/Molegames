/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.filehandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class GameConfiguration {

  private static int id = 0;
  private FileWriter writer;
  private JSONObject settings;

  public GameConfiguration(@NotNull final JSONObject json) throws IOException {
    settings = json;
  }

  /**
   * @param settings the settings to save
   * @throws IOException
   * @author Carina
   * @use call the method add the settings to the json object and than pass that to create a new
   *     file that will be saved
   */
  public void saveSettings(@NotNull final JSONObject settings) throws IOException {
    File file = new File("config" + id + ".json");
    id++;
    writer = new FileWriter(file);
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
