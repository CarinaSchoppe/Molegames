/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 15:22 by Carina Latest changes made by Carina on 21.12.21, 15:21 All contents of "GameConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.filehandling;

import com.google.gson.Gson;
import de.thundergames.playmechanics.util.Settings;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@Data
public class GameConfiguration {
  private static int ID = 0;

  /**
   * @param config the settings to save
   * @throws IOException
   * @author Carina
   * @use call the method add the settings to the json object and than pass that to create a new
   * file that will be saved
   * @see de.thundergames.playmechanics.util.Settings
   */
  public void saveSettings(@NotNull final String config) throws IOException {
    var file = new File("config" + ID + ".json");
    ID++;
    var writer = new FileWriter(file);
    writer.write(config);
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
  public Settings loadConfiguration(@NotNull final File file) throws IOException {
    return new Gson().fromJson(new String(Files.readAllBytes(file.toPath())), Settings.class);
  }
}
