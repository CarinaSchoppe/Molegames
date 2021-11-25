/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 14:59 by Carina latest changes made by Carina on 23.11.21, 14:59 All contents of "GameConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.filehandling;

import com.google.gson.Gson;
import de.thundergames.networking.util.interfaceItems.NetworkConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;

public class GameConfiguration {

  private static int id = 0;
  private FileWriter writer;


  /**
   * @param config the settings to save
   * @throws IOException
   * @author Carina
   * @use call the method add the settings to the json object and than pass that to create a new file that will be saved
   * @see NetworkConfiguration
   */
  public void saveSettings(@NotNull final NetworkConfiguration config) throws IOException {
    File file = new File("config" + id + ".json");
    id++;
    writer = new FileWriter(file);
    writer.write(config.toString());
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
  public NetworkConfiguration loadConfiguration(@NotNull final File file) throws IOException {
    return new Gson().fromJson(new String(Files.readAllBytes(file.toPath())), NetworkConfiguration.class);
  }
}
