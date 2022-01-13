/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 13.01.22, 16:58 by Carina Latest changes made by Carina on 13.01.22, 16:58 All contents of "GameConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.filehandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.thundergames.MoleGames;
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
   * @use call the method add the settings to the json object and then pass that to create a new
   * file that will be saved
   * @see Settings
   */
  public void saveSettings(@NotNull final JsonObject config) throws IOException {
    var file = new File("config" + ID + ".json");
    ID++;
    var writer = new FileWriter(file);
    writer.write(config.toString());
    writer.flush();
    writer.close();
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      System.out.println("Config saved");
      //TODO: hier ein pane einfügen lassen
    }
  }

  /**
   * @param file the file that should be loaded
   * @return the JsonObject that was saved in the File and needs to be converted into the Settings
   * @throws IOException
   * @autor Carina
   * @use pass in the settings file and done
   */
  public JsonObject loadConfiguration(@NotNull final File file) throws IOException {
    return new Gson().fromJson(new String(Files.readAllBytes(file.toPath())), JsonObject.class);
  }
}
